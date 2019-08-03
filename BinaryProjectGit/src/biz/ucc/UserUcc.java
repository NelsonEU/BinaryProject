package biz.ucc;

import biz.bizFactory.BizFactory;
import biz.bizFactory.IBizFactory;
import biz.bizObject.IUserBiz;
import biz.dto.IUserDto;
import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIXDom;
import dal.daoImpl.UserDao;
import dal.daoObject.IUserDao;
import dal.services.DalServices;
import dal.services.IDalServices;
import exceptions.BizException;
import exceptions.FatalException;

import java.io.FileNotFoundException;
import java.sql.SQLException;

public class UserUcc implements IUserUcc {

    private IDalServices dalServices;
    private IBizFactory bizFactory;
    private IUserDao userDao;

    public UserUcc(){
        bizFactory = new BizFactory();
        try {
            dalServices = new DalServices();
            userDao = new UserDao();
        } catch (SQLException | FileNotFoundException e) {
            System.out.println("ERREUR DB: " + e.getMessage());
        }
    }


    @Override
    public IUserDto login(String email, String password) {
        IUserDto storedUser = this.bizFactory.getUserDto();
        try{
            this.dalServices.startTransaction();
            storedUser = userDao.getUserByEmail(email);
            IUserBiz userBiz = (IUserBiz) storedUser;
            //Check if user exists
            if(storedUser != null){
                //Test password
                if(userBiz.checkPassword(storedUser, password)){
                    return storedUser;
                }else{
                    this.dalServices.rollbackTransaction();
                    throw new BizException("invalid_password");
                }
            }else{
                this.dalServices.rollbackTransaction();
                throw new BizException("not_existing_user");
            }

        }catch(FatalException exception){
            this.dalServices.rollbackTransaction();
            throw new FatalException("Erreur fatale: " + exception.getMessage());
        }finally{
            this.dalServices.commitTransaction();
        }

    }

    @Override
    public IUserDto register(String email, String password, String username) {
        IUserDto newUser = bizFactory.getUserDto();
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setUsername(username);
        try{
            ((IUserBiz) newUser).checkDataSignIn();
        }catch(BizException e){
            System.out.println(e.getMessage());
            throw new BizException(e.getMessage());
        }
        try{
            this.dalServices.startTransaction();
            if(userDao.getUserByEmail(newUser.getEmail()) != null){
                this.dalServices.rollbackTransaction();
                throw new BizException("email_taken");
            }
            IUserDto cryptedUser = ((IUserBiz) newUser).encryptPassword(newUser);
//            logger.info("signIn : Mot de passe crypté.");
            newUser = userDao.insertNewUser(cryptedUser);
            newUser = userDao.getUserByEmail(cryptedUser.getEmail());
        }catch(FatalException e){
            this.dalServices.rollbackTransaction();
            throw new FatalException("Erreur fatale: " + e.getMessage());
        }finally {
            this.dalServices.commitTransaction();
        }

        return newUser;
    }
}
