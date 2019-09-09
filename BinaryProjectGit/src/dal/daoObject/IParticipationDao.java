package dal.daoObject;

import biz.dto.IParticipationDto;

import java.util.List;

public interface IParticipationDao {

    List<IParticipationDto> getRanking(int tournamentId);

    double getUserTournamentBalance(int tournamentID, int userId);
}
