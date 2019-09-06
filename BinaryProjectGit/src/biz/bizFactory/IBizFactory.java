package biz.bizFactory;

import biz.dto.IDistributionDto;
import biz.dto.ITournamentDto;
import biz.dto.ITradeDto;
import biz.dto.IUserDto;
import biz.impl.User;

public interface IBizFactory {

    IUserDto getUserDto();

    ITournamentDto getTournamentDto();

    IDistributionDto getDistributionDto();

    ITradeDto getTradeDto();
}
