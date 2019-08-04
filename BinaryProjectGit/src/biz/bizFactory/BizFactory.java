package biz.bizFactory;

import biz.dto.ITournamentDto;
import biz.dto.IUserDto;
import biz.impl.Tournament;
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
}
