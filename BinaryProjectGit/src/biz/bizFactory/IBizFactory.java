package biz.bizFactory;

import biz.dto.*;
import biz.impl.User;

public interface IBizFactory {

    IUserDto getUserDto();

    ITournamentDto getTournamentDto();

    IDistributionDto getDistributionDto();

    ITradeDto getTradeDto();

    IParticipationDto getParticipationDto();
}
