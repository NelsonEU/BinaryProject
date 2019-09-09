package biz.ucc;

import biz.bizFactory.BizFactory;
import biz.bizFactory.IBizFactory;
import biz.bizObject.ITournamentBiz;
import biz.dto.IDistributionDto;
import biz.dto.IParticipationDto;
import biz.dto.ITournamentDto;
import biz.dto.IUserDto;
import com.owlike.genson.Genson;
import dal.daoImpl.DistributionDao;
import dal.daoImpl.ParticipationDao;
import dal.daoImpl.TournamentDao;
import dal.daoImpl.UserDao;
import dal.daoObject.IDistributionDao;
import dal.daoObject.IParticipationDao;
import dal.daoObject.ITournamentDao;
import dal.daoObject.IUserDao;
import dal.services.DalServices;
import dal.services.IDalServices;
import exceptions.BizException;
import exceptions.FatalException;
import ihm.Config;
import org.joda.time.DateTime;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TournamentUcc implements ITournamentUcc {

    private final ScheduledExecutorService ses;
    private Genson genson;
    private IDalServices dalServices;
    private IBizFactory bizFactory;
    private ITournamentDao  tournamentDao;
    private IDistributionDao distributionDao;
    private IUserDao userDao;
    private IParticipationDao participationDao;
    private Config config;

    public TournamentUcc(){
        bizFactory = new BizFactory();
        try {
            dalServices = new DalServices();
            tournamentDao = new TournamentDao();
            distributionDao = new DistributionDao();
            userDao = new UserDao();
            participationDao = new ParticipationDao();
            this.config = new Config("properties/prod.properties");
        } catch (SQLException | FileNotFoundException e) {
            System.out.println("ERREUR DB: " + e.getMessage());
        }
        genson = new Genson();
        ses = Executors.newScheduledThreadPool(1000);
    }

    @Override
    public List<ITournamentDto> get24hTournaments(int userId) {
        List<ITournamentDto> listTournaments = new ArrayList<>();
        try{
            this.dalServices.startTransaction();
            listTournaments = tournamentDao.getIntervalTournaments("24h");
            if(userId != -1) {
                this.checkUserRegistration(userId, listTournaments);
            }
            getDistributions(listTournaments);
        }catch(FatalException exception){
            this.dalServices.rollbackTransaction();
            throw new FatalException("Erreur fatale: " + exception.getMessage());
        }finally{
            this.dalServices.commitTransaction();
        }
        return listTournaments;
    }

    @Override
    public List<ITournamentDto> getWeeklyTournaments(int userId){
        List<ITournamentDto> listTournaments = new ArrayList<>();
        try{
            this.dalServices.startTransaction();
            listTournaments = tournamentDao.getIntervalTournaments("Weekly");
            if(userId != -1) {
                this.checkUserRegistration(userId, listTournaments);
            }
            getDistributions(listTournaments);
        }catch(FatalException exception){
            this.dalServices.rollbackTransaction();
            throw new FatalException("Erreur fatale: " + exception.getMessage());
        }finally{
            this.dalServices.commitTransaction();
        }
        return listTournaments;
    }

    private void getDistributions(List<ITournamentDto> listTournaments) {

        List<IDistributionDto> distributionDtoList = distributionDao.getDistributions();
        Map<String, IDistributionDto>  mapIdDistributionDto = new HashMap<>();
        for(IDistributionDto distributionDto : distributionDtoList){
            if(!mapIdDistributionDto.containsKey(distributionDto.getId())){
                mapIdDistributionDto.put(distributionDto.getId(), distributionDto);
            }
        }
        for(ITournamentDto tournamentDto : listTournaments){
            int dist = tournamentDto.getDistribution();
            if(mapIdDistributionDto.containsKey(String.valueOf(dist))){
                tournamentDto.setDistributionString(mapIdDistributionDto.get(String.valueOf(dist)));
            }
//            System.out.println("KING TEST: " + tournamentDto);
        }
    }

    @Override
    public void register(IUserDto user, int tournamentId, double playingSum, double bid) {
        try{
            this.dalServices.startTransaction();
            if(user.getBalance() < bid){
                throw new BizException("Not enough fund");
            }
            tournamentDao.registerUser(user.getUserId(), tournamentId, playingSum);
            user.setBalance(user.getBalance() - bid);
            userDao.updateUserBalance(user.getUserId(), user.getBalance());
        }catch(FatalException e){
            this.dalServices.rollbackTransaction();
            throw new FatalException(e.getMessage());
        }finally {
            this.dalServices.commitTransaction();
        }
    }

    @Override
    public void addTournament(ITournamentDto tournamentDto) {
        try{
            this.dalServices.startTransaction();
            tournamentDto = checkNewTournament(tournamentDto);
            //CREATE NEW TOURNAMENT AND SET RETURNING ID INTO DTO
            tournamentDto.setTournamentId(tournamentDao.addTournament(tournamentDto));
            launchScheduler(tournamentDto);
        } catch(FatalException | ParseException e){
            this.dalServices.rollbackTransaction();
            throw new FatalException(e.getMessage());
        } finally {
            this.dalServices.commitTransaction();
        }
    }

    private void launchScheduler(ITournamentDto tournamentDto) {
        Runnable finishTournamentTask = () -> {
            finishTournament(tournamentDto);
        };

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        try {
            Date startDate = format.parse(tournamentDto.getStartingDate());
            Date endDate = format.parse(tournamentDto.getEndingDate());
            long millisEnding = endDate.getTime();
            long millisStarting = startDate.getTime();
            System.out.println("DIFFERENCE IN MILLIS:" + (millisEnding-millisStarting));
            ses.schedule(finishTournamentTask, millisEnding-millisStarting, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            throw new BizException("Error parsing date when launching scheduler");
        }
    }

    private void finishTournament(ITournamentDto tournamentDto) {
        try{
            this.dalServices.startTransaction();

            System.out.println("TOURNAMENT: " + tournamentDto);

            List<IParticipationDto> rankingList = this.participationDao.getRanking(tournamentDto.getTournamentId());
            IDistributionDto distributionDto = this.distributionDao.getDistributionById(tournamentDto.getDistribution());

            if(distributionDto == null){
                this.dalServices.rollbackTransaction();
                throw new BizException("Could not find distributionDto for tournament " + tournamentDto.getTournamentId());
            }

            double prizePool = Double.parseDouble(config.getValueOfKey("tournamentRatio"))*rankingList.size()*tournamentDto.getBid();
            Map mapDistribution = genson.deserialize(distributionDto.getDistribution(), Map.class);
            giveCashPrize(prizePool, mapDistribution, rankingList);

            tournamentDto.setState("f");
            this.tournamentDao.finishTournament(tournamentDto.getTournamentId());
        }catch(FatalException e){
            this.dalServices.rollbackTransaction();
            throw new FatalException(e.getMessage());
        }finally{
            this.dalServices.commitTransaction();
        }

    }

    private void giveCashPrize(double prizePool, Map mapDistribution, List<IParticipationDto> rankingList) {
        Set keys = mapDistribution.keySet();
        for(Object key : keys){
            String keyString = (String)key;
            int keyInt = Integer.parseInt(keyString);
            int userId;
            try {
                System.out.println("ON RENTRE DANS LE TRY: keyINT: " + keyInt);
                userId = rankingList.get(keyInt - 1).getUserId();
                double percentWin = (Double.parseDouble(mapDistribution.get(key).toString()))/100;
                System.out.println("PERCENT WIN: " + percentWin);
                double amountWon = percentWin*prizePool;
                System.out.println("AMOUNT WON: " + amountWon);
                this.userDao.giveCashPrize(amountWon, userId);
                System.out.println("ON A UPDATE LE CASHPRIZE DE " + userId);
            }catch (IndexOutOfBoundsException e){
                System.out.println("ERREUR GIVECASHPRIZE: " + e.getMessage());
                continue;
            }
        }
    }

    @Override
    public void deleteTournament(String id) {
        try{
            this.dalServices.startTransaction();
            tournamentDao.deleteTournament(id);
        }catch(FatalException e){
            this.dalServices.rollbackTransaction();
            throw new FatalException(e.getMessage());
        }finally {
            this.dalServices.commitTransaction();
        }
    }

    private ITournamentDto checkNewTournament(ITournamentDto tournamentDto) throws ParseException {
        ITournamentDto toReturn = tournamentDto;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date date = format.parse(toReturn.getStartingDate());
        DateTime dateTime = new DateTime(date);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date hours = sdf.parse(tournamentDto.getDuration());
        dateTime = dateTime.plusHours(hours.getHours());
        dateTime = dateTime.plusMinutes(hours.getMinutes());
        String dateString = dateTime.toLocalDateTime().toString();
        toReturn.setEndingDate(dateString);
        return toReturn;
    }

    private void checkUserRegistration(int userId, List<ITournamentDto> listTournaments) {
        for(ITournamentDto tournamentDto : listTournaments){
            ITournamentBiz tournamentBiz = (ITournamentBiz)tournamentDto;
            if(tournamentBiz.getParty().contains(userId)){
                tournamentDto.setRegistered(true);
            }
        }
    }
}
