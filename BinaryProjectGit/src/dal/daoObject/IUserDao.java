package dal.daoObject;

import biz.dto.IUserDto;
import exceptions.FatalException;

public interface IUserDao {

    IUserDto getUserByEmail(String email) throws FatalException;

    IUserDto insertNewUser(IUserDto cryptedUser);
}
