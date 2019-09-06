package biz.bizFactory;

import biz.dto.IDistributionDto;
import biz.dto.ITournamentDto;
import biz.dto.ITradeDto;
import biz.dto.IUserDto;
import biz.impl.Distribution;
import biz.impl.Tournament;
import biz.impl.Trade;
import biz.impl.User;

public class BizFactory implements IBizFactory {

    @Override
    public IUserDto getUserDto() {
        return new User();
    }

    @Override
    public ITournamentDto getTournamentDto() {
        return new Tournament();
    }

    @Override
    public IDistributionDto getDistributionDto() {
        return new Distribution();
    }

    @Override
    public ITradeDto getTradeDto() {
        return new Trade();
    }
}
