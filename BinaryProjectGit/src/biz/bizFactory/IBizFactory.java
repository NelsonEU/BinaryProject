package biz.bizFactory;

import biz.dto.ITournamentDto;
import biz.dto.IUserDto;
import biz.impl.User;

public interface IBizFactory {

    IUserDto getUserDto();

    ITournamentDto getTournamentDto();
}
