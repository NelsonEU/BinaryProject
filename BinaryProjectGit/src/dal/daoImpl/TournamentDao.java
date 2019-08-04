package dal.daoImpl;

import biz.bizFactory.BizFactory;
import biz.dto.ITournamentDto;
import biz.dto.IUserDto;
import dal.daoObject.ITournamentDao;
import dal.services.DalServices;
import exceptions.FatalException;
import ihm.Config;
import org.apache.log4j.PropertyConfigurator;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.io.FileNotFoundException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public List<ITournamentDto> get24hTournaments() {
        List<ITournamentDto> listTournaments = new ArrayList<ITournamentDto>();
        try {
            PropertyConfigurator.configure(config.getLog4jProperties());
            Map<Integer, ITournamentDto> mapTournaments = new HashMap<Integer, ITournamentDto>();
            PreparedStatement preparedStatement =
                    dalBackendServices.getPreparedStatement(config.getValueOfKey("get24hTournaments"));
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    ITournamentDto tournament = createTournament(rs);
                    if(!mapTournaments.containsKey(tournament.getTournamentId())){
                        mapTournaments.put(tournament.getTournamentId(), tournament);
                    }
//                    tournament.addPlayerFromDao(rs.getInt(8));
//                    logger.info("Utilisateur trouvé selon son login avec succès.");
                }

//                listTournaments = new ArrayList<>(mapTournaments.values());
            }
            List<Integer> listTournamentsIds = new ArrayList<>(mapTournaments.keySet());
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
            String query = config.getValueOfKey("getRegisteredPlayers");
            if(!listTournamentsIds.isEmpty()){
                query += sb.toString();
            }
            System.out.println("QUERY: " + query.toString());
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
//                    tournament.addPlayerFromDao(rs.getInt(8));
//                    logger.info("Utilisateur trouvé selon son login avec succès.");
                }

//
            }
            for(Map.Entry<Integer,List<Integer>> entry : mapTournamentsParticipants.entrySet()){
                ITournamentDto tournament = mapTournaments.get(entry.getKey());
                tournament.setParty(entry.getValue());
            }

            listTournaments = new ArrayList<>(mapTournaments.values());

        } catch (SQLException exception) {
//            logger.info("Exception lancée lors de l'appel de la methode: findUserByLogin : "
//                    + exception.getMessage() + "----" + exception.getErrorCode() + "-----");
            System.out.println("Erreur get24hTournaments" + exception.getMessage());
            throw new FatalException("Erreur :" + exception.getMessage());
        }

        return listTournaments;
    }

    private ITournamentDto createTournament(ResultSet rs) throws SQLException {
        ITournamentDto tournament = bizFactory.getTournamentDto();
        tournament.setTournamentId(rs.getInt(1));
        tournament.setBid(rs.getInt(2));
        tournament.setPlayingSum(rs.getInt(3));

        Timestamp startingTimestamp = rs.getTimestamp(4);
        Date startingDate;
        String startingDateFormatted;
        if(startingTimestamp != null){
            startingDate = new Date(startingTimestamp.getTime());
            SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy 'at' h:mm a");
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
            SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy 'at' h:mm a");
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
        return tournament;
    }
}
