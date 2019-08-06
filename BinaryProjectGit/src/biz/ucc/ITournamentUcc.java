package biz.ucc;

import biz.dto.ITournamentDto;
import java.util.List;

public interface ITournamentUcc {
    List<ITournamentDto> get24hTournaments(int userId);

    void register(int userId, int tournamentId);
}
