package dal.daoObject;

import biz.dto.ITournamentDto;

import java.util.List;

public interface ITournamentDao {
    List<ITournamentDto> get24hTournaments();
}
