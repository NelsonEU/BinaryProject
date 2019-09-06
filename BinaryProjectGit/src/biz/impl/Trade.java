package biz.impl;

import biz.dto.ITradeDto;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class Trade implements ITradeDto {
    private int userId;
    private int tournamentID;
    private int tradeId;
    private String time;
    private LocalTime duration;
    private double amount;
    private char move;
    private long tradeStartingDate;
    private LocalDateTime startingDate;
    private LocalDateTime endingDate;
    private double strikingPrice;
    private double strikingPriceDouble;
    private double endingPrice;
    private String pair;
    //'o' if ongoing, 'r' if realized, 'l' if lost
    private char state;

    @Override
    public int getUserId() {
        return userId;
    }

    @Override
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public int getTournamentID() {
        return tournamentID;
    }

    @Override
    public void setTournamentID(int tournamentID) {
        this.tournamentID = tournamentID;
    }

    @Override
    public int getTradeId() {
        return tradeId;
    }

    @Override
    public void setTradeId(int tradeId) {
        this.tradeId = tradeId;
    }

    @Override
    public String getTime() {
        return time;
    }

    @Override
    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public char getMove() {
        return move;
    }

    @Override
    public void setMove(char move) {
        this.move = move;
    }

    @Override
    public LocalDateTime getStartingDate() {
        return startingDate;
    }

    @Override
    public void setStartingDate(LocalDateTime startingDate) {
        this.startingDate = startingDate;
    }

    @Override
    public LocalDateTime getEndingDate() {
        return endingDate;
    }

    @Override
    public void setEndingDate(LocalDateTime endingDate) {
        this.endingDate = endingDate;
    }

    @Override
    public double getStrikingPrice() {
        return strikingPrice;
    }

    @Override
    public void setStrikingPrice(double strikingPrice) {
        this.strikingPrice = strikingPrice;
    }

    @Override
    public double getEndingPrice() {
        return endingPrice;
    }

    @Override
    public void setEndingPrice(double endingPrice) {
        this.endingPrice = endingPrice;
    }

    @Override
    public String getPair() {
        return pair;
    }

    @Override
    public void setPair(String pair) {
        this.pair = pair;
    }

    @Override
    public char getState() {
        return state;
    }

    @Override
    public void setState(char state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "userId=" + userId +
                ", tournamentId=" + tournamentID +
                ", tradeId=" + tradeId +
                ", time=" + time +
                ", duration=" + duration +
                ", amount=" + amount +
                ", move=" + move +
                ", tradeStartingDate=" + tradeStartingDate +
                ", startingDate=" + startingDate +
                ", endingDate=" + endingDate +
                ", strikingPrice=" + strikingPrice +
                ", strikingPriceDouble=" + strikingPriceDouble +
                ", endingPrice=" + endingPrice +
                ", pair='" + pair + '\'' +
                ", state=" + state +
                '}';
    }

    @Override
    public long getTradeStartingDate() {
        return tradeStartingDate;
    }

    @Override
    public void setTradeStartingDate(long tradeStartingDate) {
        this.tradeStartingDate = tradeStartingDate;
    }

    @Override
    public LocalTime getDuration() {
        return duration;
    }

    @Override
    public void setDuration(LocalTime duration) {
        this.duration = duration;
    }

    @Override
    public double getStrikingPriceDouble() {
        return strikingPriceDouble;
    }

    @Override
    public void setStrikingPriceDouble(double strikingPriceDouble) {
        this.strikingPriceDouble = strikingPriceDouble;
    }
}
