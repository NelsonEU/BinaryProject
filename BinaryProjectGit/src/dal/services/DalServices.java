package dal.services;

import exceptions.FatalException;
import ihm.Config;
import org.apache.log4j.PropertyConfigurator;

import java.io.FileNotFoundException;
import java.sql.*;

public class DalServices implements IDalServices, IBackendDalServices {

    private String driverName;
    private String url;
    private String username;
    private String password;
    private Connection conn;
    private Config config;

    public DalServices() throws SQLException, FileNotFoundException {
        this.config = new Config("properties/prod.properties");
        PropertyConfigurator.configure(config.getLog4jProperties());
        this.driverName = this.config.getValueOfKey("driverClassName");
        this.url = this.config.getValueOfKey("url");
        this.username = this.config.getValueOfKey("username");
        this.password = this.config.getValueOfKey("password");
    }

    @Override
    public PreparedStatement getPreparedStatement(String query) throws FatalException {
        PreparedStatement newPrepareStatement;
        try {
            conn = DriverManager.getConnection(this.url, this.username, this.password);
            newPrepareStatement = conn.prepareStatement(query);
            return newPrepareStatement;
        } catch (SQLException exception) {
//            logger.info("Exception lancée lors de l'appel de la methode: getPreparedStatement : "
//                    + exception.getMessage() + "----" + exception.getErrorCode() + "-----");
            System.out.println("Erreur getPS");
            throw new FatalException("Fatal error: " + exception.getMessage());
        }
    }

    @Override
    public PreparedStatement getPreparedStatementReturningId(String query) throws FatalException {
        PreparedStatement newPrepareStatement;
        try {
            conn = DriverManager.getConnection(this.url, this.username, this.password);
            newPrepareStatement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            return newPrepareStatement;
        } catch (SQLException exception) {
//            logger.info("Exception lancée lors de l'appel de la methode: getPreparedStatement : "
//                    + exception.getMessage() + "----" + exception.getErrorCode() + "-----");
            System.out.println("Erreur getPS");
            throw new FatalException("Fatal error: " + exception.getMessage());
        }
    }

    @Override
    public Array getArrayId(Object[] array) throws FatalException {
        try {
            return this.conn.createArrayOf("INT", array);
        }catch (SQLException e){
            throw new FatalException("Fatal error: " + e.getMessage());
        }
    }

    @Override
    public void establishConnection() throws FatalException {
//        if(this.conn.isClosed()){
//            throw new FatalException();
//        }
        System.out.println("ON ESTABLISH LA CONNECTION");
        try{
            conn = DriverManager.getConnection(this.url, this.username, this.password);
            conn.setAutoCommit(true);
        }catch(SQLException e){
            System.out.println("Erreur establish");
            throw new FatalException("Fatal error: " + e.getMessage());
        }

    }

    @Override
    public void startTransaction() throws FatalException {
        System.out.println("ON START TRANSACTION");
        try {
            if (this.conn == null || this.conn.isClosed()) {
                establishConnection();
            }
            if (conn.getAutoCommit()) {
                conn.setAutoCommit(false);
            }
        }catch (SQLException e){
            rollbackTransaction();
            closeConnection();
            System.out.println("Erreur start: " + e.getMessage());
            throw new FatalException("Fatal error: " + e.getMessage());
        }
    }

    @Override
    public void commitTransaction() throws FatalException {
        try{
            if(!conn.isClosed()) {
                conn.commit();
                conn.setAutoCommit(true);
            }
        }catch (SQLException e){
            rollbackTransaction();
            System.out.println("Erreur commit: " + e.getMessage());
            throw new FatalException("Fatal error: " + e.getMessage());
        }finally{
            closeConnection();
        }
    }

    @Override
    public void rollbackTransaction() throws FatalException {
        try{
            if(!conn.isClosed()) {
                conn.rollback();
                conn.setAutoCommit(true);
                conn.close();
            }
        }catch (SQLException e){
            System.out.println("Erreur rollback: " + e.getMessage());
            throw new FatalException("Fatal error: " + e.getMessage());
        }
    }

    @Override
    public void closeConnection() throws FatalException {
        try{
            if(!conn.isClosed()) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }catch (SQLException e){
            System.out.println("Erreur close");
            throw new FatalException("Fatal error: " + e.getMessage());
        }
    }
}
