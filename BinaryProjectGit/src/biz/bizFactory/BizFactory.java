package biz.bizFactory;

import biz.dto.*;
import biz.impl.*;

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

    @Override
    public IParticipationDto getParticipationDto(){
        return new Participation();
    }
}
