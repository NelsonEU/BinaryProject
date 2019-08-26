package biz.ucc;

import biz.dto.ITournamentDto;
import java.util.List;

public interface ITournamentUcc {
    List<ITournamentDto> get24hTournaments(int userId);

    List<ITournamentDto> getWeeklyTournaments(int userId);

    void register(int userId, int tournamentId);

    void addTournament(ITournamentDto tournamentDto);

    void deleteTournament(String id);
}
