package biz.dto;

public interface IParticipationDto {
    int getUserId();

    void setUserId(int userId);

    int getTournamentId();

    void setTournamentId(int tournamentId);

    double getBalance();

    void setBalance(double balance);

    String getUsername();

    void setUsername(String username);
}
