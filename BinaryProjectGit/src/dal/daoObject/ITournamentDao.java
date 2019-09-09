package dal.daoObject;

import biz.dto.ITournamentDto;

import java.sql.SQLException;
import java.util.List;

public interface ITournamentDao {
    List<ITournamentDto> getIntervalTournaments(String interval);

    void registerUser(int userId, int tournamentId, double playingSum);

    int addTournament(ITournamentDto tournamentDto);

    void deleteTournament(String id);

    void finishTournament(int tournamentId);
}
