package dal.daoObject;

import biz.dto.ITournamentDto;

import java.sql.SQLException;
import java.util.List;

public interface ITournamentDao {
    List<ITournamentDto> getIntervalTournaments(String interval);

    void registerUser(int userId, int tournamentId);

    void addTournament(ITournamentDto tournamentDto);

    void deleteTournament(String id);
}
