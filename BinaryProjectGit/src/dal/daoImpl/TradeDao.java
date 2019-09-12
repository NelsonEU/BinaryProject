package dal.daoImpl;

import biz.bizFactory.BizFactory;
import biz.dto.ITradeDto;
import dal.daoObject.ITradeDao;
import dal.services.DalServices;
import exceptions.FatalException;
import ihm.Config;

import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class TradeDao implements ITradeDao {

    private Config config;
    private DalServices dalBackendServices;
    private BizFactory bizFactory;

    public TradeDao(){
        this.bizFactory = new BizFactory();
        try {
            this.dalBackendServices = new DalServices();
            this.config = new Config("properties/prod.properties");
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int newTrade(ITradeDto tradeDto) {
        int id = 0;
        try {
            // Si il n'y pas de user pour ce login, on peut l'inscrire
            PreparedStatement ps =
                    dalBackendServices.getPreparedStatementReturningId(config.getValueOfKey("newTrade"));


            ps.setInt(1, tradeDto.getUserId());
            ps.setInt(2, tradeDto.getTournamentID());
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            long ms = sdf.parse(tradeDto.getTime()).getTime();
            ps.setTime(3, new Time(ms));
            ps.setDouble(4, tradeDto.getAmount());
            ps.setString(5, String.valueOf(tradeDto.getMove()));
            ps.setDouble(6, tradeDto.getStrikingPrice());
            ps.setDouble(7, tradeDto.getStrikingPrice());
            ps.setString(8, tradeDto.getPair());
            ps.setTimestamp(9, new java.sql.Timestamp(java.util.Date.from(tradeDto.getStartingDate().atZone(ZoneId.systemDefault()).toInstant()).getTime()));
            ps.setTimestamp(10, new java.sql.Timestamp(java.util.Date.from(tradeDto.getEndingDate().atZone(ZoneId.systemDefault()).toInstant()).getTime()));
            ps.setString(11, String.valueOf(tradeDto.getState()));
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                System.out.println("ON A UN ID:");
                id = rs.getInt(1);
                System.out.println(id);
            }

            updateBalance(-tradeDto.getAmount(), tradeDto);


        } catch (SQLException | ParseException exception) {
            throw new FatalException("Erreur: " + exception.getMessage());
        }
        return id;
    }

    @Override
    public void executeTrade(double endingPrice, char state, ITradeDto trade) {
        PreparedStatement ps =
                dalBackendServices.getPreparedStatement(config.getValueOfKey("executeTrade"));
        try {
            ps.setDouble(1, endingPrice);
            ps.setString(2, String.valueOf(state));
            ps.setInt(3,trade.getTradeId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new FatalException(e.getMessage());
        }
    }

    @Override
    public void updateBalance(double prize, ITradeDto trade) {
        PreparedStatement ps =
                dalBackendServices.getPreparedStatement(config.getValueOfKey("updateBalance"));
        try {
            ps.setDouble(1, prize);
            ps.setInt(2, trade.getTournamentID());
            ps.setInt(3, trade.getUserId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new FatalException(e.getMessage());
        }
    }

    @Override
    public List<ITradeDto> getTournamentOpenTrades(int userId, int tournamentId){
        PreparedStatement ps = dalBackendServices.getPreparedStatement(config.getValueOfKey("tournamentOpenTrades"));
        try {
            return getListFromSelectPS(ps, userId, tournamentId);
        } catch (SQLException e) {
            throw new FatalException(e.getMessage());
        }
    }

    @Override
    public List<ITradeDto> getTournamentHistoryTrades(int userId, int tournamentId){
        PreparedStatement ps = dalBackendServices.getPreparedStatement(config.getValueOfKey("tournamentHistoryTrades"));
        try {
            return getListFromSelectPS(ps, userId, tournamentId);
        } catch (SQLException e) {
            throw new FatalException(e.getMessage());
        }
    }

    @Override
    public int getTradeTournamentCount(int userId, int tournamentId){
        PreparedStatement ps = dalBackendServices.getPreparedStatement(config.getValueOfKey("getTournamentUserCount"));
        try {
            ps.setInt(1, userId);
            ps.setInt(2, tournamentId);
            ResultSet rs = ps.executeQuery();
            System.out.println(ps.toString());
            if(rs.next()){
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new FatalException(e.getMessage());
        }
    }

    private List<ITradeDto> getListFromSelectPS(PreparedStatement ps, int userId, int tournamentId) throws SQLException {
        List<ITradeDto> list = new ArrayList<>();
        ps.setInt(1, userId);
        ps.setInt(2, tournamentId);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            list.add(createTradeDto(rs));
        }
        return list;

    }

    private ITradeDto createTradeDto(ResultSet rs) throws SQLException {
        ITradeDto tradeDto = bizFactory.getTradeDto();
        tradeDto.setTradeId(rs.getInt(1));
        tradeDto.setUserId(rs.getInt(2));
        tradeDto.setTournamentID(rs.getInt(3));
        tradeDto.setDuration(rs.getTime(4).toLocalTime());
        tradeDto.setAmount(rs.getDouble(5));
        tradeDto.setMove(rs.getString(6).charAt(0));
        tradeDto.setStrikingPrice(rs.getDouble(7));
        tradeDto.setEndingPrice(rs.getDouble(8));
        tradeDto.setPair(rs.getString(9));
        tradeDto.setStartingDate(rs.getTimestamp(10).toLocalDateTime());
        tradeDto.setEndingDate(rs.getTimestamp(11).toLocalDateTime());
        tradeDto.setState(rs.getString(12).charAt(0));
        return tradeDto;
    }
}
