package biz.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;

public interface ITradeDto {
    int getUserId();

    void setUserId(int userId);

    int getTournamentID();

    void setTournamentID(int tournamentID);

    int getTradeId();

    void setTradeId(int tradeId);

    String getTime();

    void setTime(String time);

    double getAmount();

    void setAmount(double amount);

    char getMove();

    void setMove(char move);

    LocalDateTime getStartingDate();

    void setStartingDate(LocalDateTime startingDate);

    LocalDateTime getEndingDate();

    void setEndingDate(LocalDateTime endingDate);

    double getStrikingPrice();

    void setStrikingPrice(double strikingPrice);

    double getEndingPrice();

    void setEndingPrice(double endingPrice);

    String getPair();

    void setPair(String pair);

    char getState();

    void setState(char state);

    long getTradeStartingDate();

    void setTradeStartingDate(long tradeStartingDate);

    LocalTime getDuration();

    void setDuration(LocalTime duration);

    double getStrikingPriceDouble();

    void setStrikingPriceDouble(double strikingPriceDouble);
}
