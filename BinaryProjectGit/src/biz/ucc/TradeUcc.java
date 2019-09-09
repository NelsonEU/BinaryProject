package biz.ucc;

import biz.dto.ITradeDto;
import biz.impl.Trade;
import com.google.gson.Gson;
import dal.daoImpl.ParticipationDao;
import dal.daoImpl.TradeDao;
import dal.daoObject.IParticipationDao;
import dal.daoObject.ITradeDao;
import dal.services.DalServices;
import dal.services.IDalServices;
import exceptions.BizException;
import exceptions.FatalException;
import ihm.Config;


import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TradeUcc implements ITradeUcc {

    private ScheduledExecutorService ses;
    private Gson genson;
    private IDalServices dalServices;
    private ITradeDao tradeDao;
    private Config config;
    private IParticipationDao participationDao;

    public TradeUcc() {
        this.genson = new Gson();
        try {
            dalServices = new DalServices();
            tradeDao = new TradeDao();
            participationDao = new ParticipationDao();
            this.config = new Config("properties/prod.properties");
        } catch (SQLException | FileNotFoundException e) {
            System.out.println("ERREUR DB: " + e.getMessage());
        }
        ses = Executors.newScheduledThreadPool(10000);
    }

    @Override
    public void newTrade(String tradeJson, int userId) {

        try{
            this.dalServices.startTransaction();
            ITradeDto trade = checkAndCreateTrade(tradeJson, userId);
            int tradeId = this.tradeDao.newTrade(trade);
            trade.setTradeId(tradeId);
            launchScheduler(trade);

        }catch (BizException e){
            this.dalServices.rollbackTransaction();
            throw new BizException(e.getMessage());
        }finally {
            this.dalServices.commitTransaction();
        }
    }

    private void ExecuteTrade(ITradeDto trade){
        double endingPrice = getPrice(trade.getPair());
        double prize = 0;
        char state = 'l';
        char realMove = getRealMove(endingPrice, trade.getStrikingPrice());
        if(trade.getMove() == realMove){
            state = 'w';
            prize = trade.getAmount() + Double.parseDouble(this.config.getValueOfKey("winRate"))*trade.getAmount();
        }
        try{
            this.tradeDao.executeTrade(endingPrice, state, trade);
            if(prize > 0){
                this.tradeDao.updateBalance(prize, trade);
            }
        }catch(FatalException e){
            throw new FatalException(e.getMessage());
        }
        //ICI ON UPDATE LE ENDING PRICE ET LE STATE


    }

    private double getPrice(String pair){
        double price = 0;
        try {
            /* Start of Fix */
            TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
                public void checkClientTrusted(X509Certificate[] certs, String authType) { }
                public void checkServerTrusted(X509Certificate[] certs, String authType) { }

            } };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) { return true; }
            };
            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
            /* End of the fix*/
            URL url = new URL(this.config.getValueOfKey("priceApiUrl") + pair);
            System.out.println("URL: " + url.toString());
            InputStreamReader reader = new InputStreamReader(url.openStream());
            Price priceObject = genson.fromJson(reader, Price.class);
            price = priceObject.getPrice();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return price;
    }

    private char getRealMove(double endingPrice, double strikingPrice) {
        char realMove;
        if(endingPrice-strikingPrice > 0){
            realMove = 'u';
        }else{
            realMove = 'd';
        }
        return realMove;
    }

    private void launchScheduler(ITradeDto trade) {
        Runnable realizeTradeTask = () -> {
            ExecuteTrade(trade);
        };

        long millisEnding = trade.getEndingDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long millisStarting = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        ses.schedule(realizeTradeTask, millisEnding-millisStarting, TimeUnit.MILLISECONDS);

    }


    private ITradeDto checkAndCreateTrade(String tradeJson, int userId) {
        ITradeDto trade;
        try {
            trade = genson.fromJson(tradeJson, Trade.class);
        }catch(NumberFormatException e){
            throw new BizException("Invalid data");
        }
        double userTournamentBalance = this.participationDao.getUserTournamentBalance(trade.getTournamentID(), userId);
        if(userTournamentBalance == -1){
            throw new BizException("No registration");
        }
        if(trade.getAmount() > userTournamentBalance){
            throw new BizException("Insufficient balance");
        }
        try {
            createDates(trade);
        }catch (BizException e){
            throw new BizException(e.getMessage());
        }
        trade.setUserId(userId);
        trade.setState('o');
        return trade;

    }

    private void createDates(ITradeDto trade) {
        LocalDateTime startingDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(trade.getTradeStartingDate()), TimeZone
                .getDefault().toZoneId());
        String hoursString = trade.getTime().split(":")[0];
        String minutesString = trade.getTime().split(":")[1];
        int hours = Integer.parseInt(hoursString);
        int minutes = Integer.parseInt(minutesString);
        if(hours == 0 && minutes < 1){
            throw new BizException("Timing error");
        }
        LocalDateTime endingDate = startingDate.plusHours(hours).plusMinutes(minutes);
        LocalTime duration = LocalTime.parse(trade.getTime());
        trade.setStartingDate(startingDate);
        trade.setEndingDate(endingDate);
        trade.setDuration(duration);
    }

    private class Price{

        private double bid;
        private double ask;

        public Price(){

        }

        public void setAsk(double ask) {
            this.ask = ask;
        }

        public void setBid(double bid) {
            this.bid = bid;
        }

        public double getAsk() {
            return ask;
        }

        public double getBid() {
            return bid;
        }

        public double getPrice(){
            return (bid+ask)/2;
        }
    }
}
