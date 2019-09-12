package biz.ucc;

import biz.dto.ITradeDto;

import java.util.List;

public interface ITradeUcc {
    List<ITradeDto> getTournamentOpenTrades(int userId, int tournamentId);

    List<ITradeDto> getTournamentHistoryTrades(int userId, int tournamentId);

    int getTournamentTradesCount(int userId, int tournamentId);

    void newTrade(String tradeJson, int userId);
}
