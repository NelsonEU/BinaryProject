package biz.ucc;

import biz.bizFactory.BizFactory;
import biz.bizFactory.IBizFactory;
import biz.bizObject.ITournamentBiz;
import biz.dto.IDistributionDto;
import biz.dto.ITournamentDto;
import dal.daoImpl.DistributionDao;
import dal.daoImpl.TournamentDao;
import dal.daoImpl.UserDao;
import dal.daoObject.IDistributionDao;
import dal.daoObject.ITournamentDao;
import dal.daoObject.IUserDao;
import dal.services.DalServices;
import dal.services.IDalServices;
import exceptions.BizException;
import exceptions.FatalException;
import org.joda.time.DateTime;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TournamentUcc implements ITournamentUcc {

    private IDalServices dalServices;
    private IBizFactory bizFactory;
    private ITournamentDao  tournamentDao;
    private IDistributionDao distributionDao;

    public TournamentUcc(){
        bizFactory = new BizFactory();
        try {
            dalServices = new DalServices();
            tournamentDao = new TournamentDao();
            distributionDao = new DistributionDao();
        } catch (SQLException | FileNotFoundException e) {
            System.out.println("ERREUR DB: " + e.getMessage());
        }
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
    public void register(int userId, int tournamentId, double playingSum) {
        try{
            this.dalServices.startTransaction();
            tournamentDao.registerUser(userId, tournamentId, playingSum);
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
            tournamentDao.addTournament(tournamentDto);
        } catch(FatalException | ParseException e){
            this.dalServices.rollbackTransaction();
            throw new FatalException(e.getMessage());
        } finally {
            this.dalServices.commitTransaction();
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
        if(1 < 0){
            throw new BizException("Données incorrectes");
        }
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
