package biz.ucc;

import biz.dto.IParticipationDto;

import java.util.List;

public interface IParticipationUcc {

    List<IParticipationDto> getRanking(int tournamentId);
}
