package biz.bizFactory;

import biz.dto.IUserDto;
import biz.impl.User;

public class BizFactory implements IBizFactory {

    @Override
    public IUserDto getUserDto() {
        return new User();
    }
}
