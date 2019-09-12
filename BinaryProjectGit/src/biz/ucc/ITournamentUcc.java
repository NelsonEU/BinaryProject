package biz.ucc;

import biz.dto.ITournamentDto;
import biz.dto.IUserDto;

import java.util.List;

public interface ITournamentUcc {
    List<ITournamentDto> get24hTournaments(int userId);

    List<ITournamentDto> getWeeklyTournaments(int userId);

    double getUserTournamentBalance(int tournamentId, int userId);

    void register(IUserDto user, int tournamentId, double playingSum, double bid);

    void addTournament(ITournamentDto tournamentDto);

    void deleteTournament(String id);
}
