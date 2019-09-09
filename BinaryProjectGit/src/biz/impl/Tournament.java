package biz.impl;

import biz.bizObject.ITournamentBiz;
import biz.dto.IDistributionDto;
import biz.dto.ITournamentDto;

import java.util.ArrayList;
import java.util.List;

public class Tournament implements ITournamentDto, ITournamentBiz {

    private int tournamentId;
    private int bid;
    private int playingSum;
    private int distribution;
    private IDistributionDto distributionString;
    private String startingDate;
    private String endingDate;
    private String duration;
    private String state;
    private List<Integer> party;
    //TODO BUGGY DE OUF CA JE PENSE
    private boolean registered;
    private int minPlayers;

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
        return duration;
    }

    @Override
    public void setDuration(String durarion) {
        this.duration = durarion;
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
                ", startingDate=" + startingDate +
                ", endingDateUTC=" + endingDate +
                ", duration=" + duration +
                ", state='" + state + '\'' +
                ", party=" + party +
                ", registered=" + registered +
                ", distributionId=" + distribution +
                ", distribution=" + distributionString +
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

    @Override
    public int getDistribution() {
        return distribution;
    }

    @Override
    public void setDistribution(int distribution) {
        this.distribution = distribution;
    }

    @Override
    public IDistributionDto getDistributionString() {
        return distributionString;
    }

    @Override
    public void setDistributionString(IDistributionDto distributionString) {
        this.distributionString = distributionString;
    }

    @Override
    public int getMinPlayers() {
        return this.minPlayers;
    }

    @Override
    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

}
