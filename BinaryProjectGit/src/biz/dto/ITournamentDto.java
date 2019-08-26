package biz.dto;

import java.util.List;

public interface ITournamentDto {

    List<Integer> getParty();

    void setParty(List<Integer> party);

    void addPlayerFromDao(int userId);

    int getTournamentId();

    void setTournamentId(int tournamentId);

    int getBid();

    void setBid(int bid);

    int getPlayingSum();

    void setPlayingSum(int playing_sum);

    String getStartingDate();

    void setStartingDate(String formattedDate);

    String getEndingDate();

    void setEndingDate(String formattedDate);

    String getDuration();

    void setDuration(String durarion);

    String getState();

    void setState(String state);

    String toString();

    void setRegistered(boolean registered);

    boolean isRegistered();

    int getDistribution();

    void setDistribution(int distribution);

    IDistributionDto getDistributionString();

    void setDistributionString(IDistributionDto distributionString);

    int getMinPlayers();

    void setMinPlayers(int minPlayers);
}
