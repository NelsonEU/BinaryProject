package biz.bizFactory;

import biz.dto.IUserDto;
import biz.impl.User;

public interface IBizFactory {

    IUserDto getUserDto();
}
