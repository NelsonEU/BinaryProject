package biz.impl;

import biz.bizObject.ITournamentBiz;
import biz.dto.ITournamentDto;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Tournament implements ITournamentDto, ITournamentBiz {

    private int tournamentId;
    private int bid;
    private int playingSum;
    private String startingDate;
    private String endingDate;
    private String durarion;
    private String state;
    private List<Integer> party;
    private boolean registered;

    public Tournament(){
        super();
        party = new ArrayList<Integer>();
    }



    @Override
    public List<Integer> getParty(){
        return this.party;
    }

    @Override
    public void setParty(List<Integer> party) {
        this.party = party;
    }

    @Override
    public void addPlayerFromDao(int userId){
        party.add(userId);
    }

    @Override
    public int getTournamentId() {
        return tournamentId;
    }

    @Override
    public void setTournamentId(int tournamentId) {
        this.tournamentId = tournamentId;
    }

    @Override
    public int getBid() {
        return bid;
    }

    @Override
    public void setBid(int bid) {
        this.bid = bid;
    }

    @Override
    public int getPlayingSum() {
        return playingSum;
    }

    @Override
    public void setPlayingSum(int playingSum) {
        this.playingSum = playingSum;
    }

    @Override
    public String getStartingDate() {
        return startingDate;
    }

    @Override
    public void setStartingDate(String startingDate) {
        this.startingDate = startingDate;
    }

    @Override
    public String getEndingDate() {
        return endingDate;
    }

    @Override
    public void setEndingDate(String endingDate) {
        this.endingDate = endingDate;
    }

    @Override
    public String getDuration() {
        return durarion;
    }

    @Override
    public void setDuration(String durarion) {
        this.durarion = durarion;
    }

    @Override
    public String getState() {
        return state;
    }

    @Override
    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Tournament{" +
                "tournamentId=" + tournamentId +
                ", bid=" + bid +
                ", playingSum=" + playingSum +
                ", startingDateUTC=" + startingDate +
                ", endingDateUTC=" + endingDate +
                ", durarion=" + durarion +
                ", state='" + state + '\'' +
                ", party=" + party +
                ", registered=" + registered +
                '}';
    }

    @Override
    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    @Override
    public boolean isRegistered() {
        return this.registered;
    }


}
