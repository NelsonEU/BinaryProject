package biz.dto;

public interface IUserDto {
    boolean isAdmin();

    void setAdmin(boolean admin);

    int getUserId();

    void setUserId(int userId);

    void setEmail(String email);

    void setPassword(String password);

    String getUsername();

    void setUsername(String username);

    double getBalance();

    void setBalance(double balance);

    String getEmail();

    String getPassword();

    String getSalt();

    void setSalt(String salt);

}
