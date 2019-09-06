package dal.daoObject;

import biz.dto.ITradeDto;

public interface ITradeDao {
    int newTrade(ITradeDto tradeDto);

    void executeTrade(double endingPrice, char state, ITradeDto trade);

    void updateBalance(double prize, ITradeDto trade);
}
