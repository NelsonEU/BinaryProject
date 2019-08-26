package biz.impl;

import biz.bizObject.IUserBiz;
import biz.dto.IUserDto;
import exceptions.BizException;
import org.mindrot.jbcrypt.BCrypt;

import java.util.regex.Pattern;

public class User implements IUserDto, IUserBiz {

    private int userId;
    private String email;
    private String password;
    private String username;
    private double balance;
    private String salt;
    private boolean admin;

    public User(){

    }


    public User(int userId, String email, String password, String username, double balance, boolean admin){
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.username = username;
        this.balance = balance;
        this.admin = admin;
    }

    @Override
    public boolean isAdmin() {
        return admin;
    }

    @Override
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    @Override
    public int getUserId() {
        return userId;
    }

    @Override
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString(){
        return "Email: " + email;

    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getSalt() {
        return this.salt;
    }

    @Override
    public void setSalt(String salt) {

        this.salt = salt;
    }


    @Override
    public boolean checkPassword(IUserDto user, String password) {
        return BCrypt.checkpw(password, user.getPassword());
    }

    @Override
    public void checkData() {
        Pattern patternLogin = Pattern.compile("^[a-zA-Z0-9]+$");

        if (!patternLogin.matcher(this.getUsername()).find()) {
            throw new BizException("invalid_username");
        }

        Pattern patternMail =
                Pattern.compile("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$");

        if (!patternMail.matcher(this.getEmail()).find()) {
            throw new BizException("invalid_email");
        }
    }

    @Override
    public void checkDataSignIn() {
        checkData();

        Pattern patternPassword =
                Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,15}$");

        Pattern patternPasswordBis = Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,15}$");


        if (!patternPassword.matcher(this.getPassword()).find() && !patternPasswordBis.matcher(this.getPassword()).find()) {
            throw new BizException("invalid_password_register");
        }
    }

    @Override
    public IUserDto encryptPassword(IUserDto userDto) {
        String salt = BCrypt.gensalt();
        String pswd = BCrypt.hashpw(userDto.getPassword(), salt);
        userDto.setSalt(salt);
        userDto.setPassword(pswd);
        return userDto;
    }
}
