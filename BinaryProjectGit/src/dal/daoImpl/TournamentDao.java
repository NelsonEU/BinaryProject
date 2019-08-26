package dal.daoImpl;

import biz.bizFactory.BizFactory;
import biz.dto.ITournamentDto;
import dal.daoObject.ITournamentDao;
import dal.services.DalServices;
import exceptions.FatalException;
import ihm.Config;
import org.apache.log4j.PropertyConfigurator;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TournamentDao implements ITournamentDao {

    private BizFactory bizFactory;
    private DalServices dalBackendServices;
    private Config config;

    public TournamentDao() throws FileNotFoundException, SQLException {
        this.bizFactory = new BizFactory();
        this.dalBackendServices = new DalServices();
        this.config = new Config("properties/prod.properties");
    }

    @Override
    public List<ITournamentDto> getIntervalTournaments(String interval) {
        List<ITournamentDto> listTournaments = new ArrayList<ITournamentDto>();
        try {
            PropertyConfigurator.configure(config.getLog4jProperties());
            Map<Integer, ITournamentDto> mapTournaments = new HashMap<Integer, ITournamentDto>();
            PreparedStatement preparedStatement =
                    dalBackendServices.getPreparedStatement(config.getValueOfKey("get" + interval + "Tournaments"));
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    ITournamentDto tournament = createTournamentDto(rs);
                    if(!mapTournaments.containsKey(tournament.getTournamentId())){
                        mapTournaments.put(tournament.getTournamentId(), tournament);
                    }
//                    logger.info("Utilisateur trouvé selon son login avec succès.");
                }
            }
            List<Integer> listTournamentsIds = new ArrayList<>(mapTournaments.keySet());
            String query = getQueryUsersTournaments(listTournamentsIds);
            Map<Integer, List<Integer>> mapTournamentsParticipants = new HashMap<Integer, List<Integer>>();
            preparedStatement = dalBackendServices.getPreparedStatement(query);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    List<Integer> listPlayers;
                    if(mapTournamentsParticipants.containsKey(rs.getInt(2))){
                        listPlayers = mapTournamentsParticipants.get(rs.getInt(2));
                    }else{
                        listPlayers = new ArrayList<>();
                        mapTournamentsParticipants.put(rs.getInt(2), listPlayers);
                    }
                    listPlayers.add(rs.getInt(1));
//                    logger.info("Utilisateur trouvé selon son login avec succès.");
                }
            }
            for(Map.Entry<Integer,List<Integer>> entry : mapTournamentsParticipants.entrySet()){
                ITournamentDto tournament = mapTournaments.get(entry.getKey());
                if(tournament != null && entry.getValue() != null) {
                    tournament.setParty(entry.getValue());
                }
            }

            listTournaments = new ArrayList<>(mapTournaments.values());
            listTournaments.sort((t1, t2) -> t1.getStartingDate().compareTo(t2.getStartingDate()));

        } catch (SQLException exception) {
//            logger.info("Exception lancée lors de l'appel de la methode: findUserByLogin : "
//                    + exception.getMessage() + "----" + exception.getErrorCode() + "-----");
            System.out.println("Erreur get24hTournaments" + exception.getMessage());
            throw new FatalException("Erreur :" + exception.getMessage());
        }
        return listTournaments;
    }


    private String getQueryUsersTournaments(List<Integer> listTournamentsIds) {
        String query = config.getValueOfKey("getRegisteredPlayers");
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < listTournamentsIds.size(); i++){
            if(i == 0){
                sb.append(" WHERE UT.tournament_id IN (");
            }
            sb.append(listTournamentsIds.get(i));
            if(i == listTournamentsIds.size()-1){
                sb.append(")");
            }else{
                sb.append(",");
            }
        }
        return query + sb.toString();
    }

    @Override
    public void registerUser(int userId, int tournamentId) {
        try {
            // Si il n'y pas de user pour ce login, on peut l'inscrire
            PreparedStatement ps =
                    dalBackendServices.getPreparedStatement(config.getValueOfKey("registerPlayer"));

            ps.setInt(1, userId);
            ps.setInt(2, tournamentId);

            ps.executeUpdate();

        } catch (SQLException exception) {
            System.out.println("Erreur registerPlayer");
            throw new FatalException("Erreur: " + exception.getMessage());
        }
    }

    @Override
    public void addTournament(ITournamentDto tournamentDto) {
        try {
            // Si il n'y pas de user pour ce login, on peut l'inscrire
            PreparedStatement ps =
                    dalBackendServices.getPreparedStatement(config.getValueOfKey("addTournament"));

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            java.util.Date startDate = format.parse(tournamentDto.getStartingDate());
            java.util.Date endDate = format.parse(tournamentDto.getEndingDate());
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            long ms = sdf.parse(tournamentDto.getDuration()).getTime();
            Time t = new Time(ms);
            ps.setInt(1, tournamentDto.getBid());
            ps.setInt(2, tournamentDto.getPlayingSum());
//            Date testt = Date.valueOf(tournamentDto.getStartingDate());
            ps.setTimestamp(3, new java.sql.Timestamp(startDate.getTime()));
            ps.setTimestamp(4, new java.sql.Timestamp(endDate.getTime()));
            ps.setTime(5, t);
            ps.setInt(6, tournamentDto.getDistribution());
            ps.setInt(7, tournamentDto.getMinPlayers());
            System.out.println("QUERY: " + ps.toString());
            ps.executeUpdate();

        } catch (SQLException | ParseException exception) {
            System.out.println("Erreur addTournament");
            throw new FatalException("Erreur: " + exception.getMessage());
        }
    }

    @Override
    public void deleteTournament(String id) {
        try {
            PreparedStatement ps = dalBackendServices.getPreparedStatement(config.getValueOfKey("deleteTournament"));
            ps.setInt(1, Integer.parseInt(id));
            ps.executeUpdate();
        }catch(SQLException e){
            throw new FatalException(e.getMessage());
        }
    }

    private ITournamentDto createTournamentDto(ResultSet rs) throws SQLException {
        ITournamentDto tournament = bizFactory.getTournamentDto();
        tournament.setTournamentId(rs.getInt(1));
        tournament.setBid(rs.getInt(2));
        tournament.setPlayingSum(rs.getInt(3));

        Timestamp startingTimestamp = rs.getTimestamp(4);
        Date startingDate;
        String startingDateFormatted;
        if(startingTimestamp != null){
            startingDate = new Date(startingTimestamp.getTime());
            SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm");
            startingDateFormatted = ft.format(startingDate);
        }else{
            startingDateFormatted = null;
        }
        tournament.setStartingDate(startingDateFormatted);

        Timestamp endingTimestamp = rs.getTimestamp(5);
        Date endingDate;
        String endingDateFormatted;
        if(endingTimestamp != null){
            endingDate = new Date(endingTimestamp.getTime());
            SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm");
            endingDateFormatted = ft.format(endingDate);
        }else{
            endingDateFormatted = null;
        }
        tournament.setEndingDate(endingDateFormatted);
        Time durationTime = rs.getTime(6);
        Date duration;
        String durationString;
        if (durationTime != null){
            duration = new Date(durationTime.getTime());
            SimpleDateFormat ft = new SimpleDateFormat("hh:mm");
            durationString = ft.format(duration);
        }else{
            durationString = null;
        }
        tournament.setDuration(durationString);
        tournament.setState(rs.getString(7));
        tournament.setDistribution(rs.getInt(8));
        tournament.setMinPlayers(rs.getInt(9));
        return tournament;
    }
}
