package dal.daoObject;

import biz.dto.ITradeDto;

import java.util.List;

public interface ITradeDao {
    int newTrade(ITradeDto tradeDto);

    void executeTrade(double endingPrice, char state, ITradeDto trade);

    void updateBalance(double prize, ITradeDto trade);

    List<ITradeDto> getTournamentOpenTrades(int userId, int tournamentId);

    List<ITradeDto> getTournamentHistoryTrades(int userId, int tournamentId);

    int getTradeTournamentCount(int userId, int tournamentId);
}
