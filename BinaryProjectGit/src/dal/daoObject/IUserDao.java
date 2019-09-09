package dal.daoObject;

import biz.dto.IUserDto;
import exceptions.FatalException;

public interface IUserDao {

    IUserDto getUserByEmail(String email) throws FatalException;

    IUserDto insertNewUser(IUserDto cryptedUser);

    void updateUserBalance(int userId, double balance);

    void giveCashPrize(double amountWon, int userId);
}
