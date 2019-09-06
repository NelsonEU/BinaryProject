package dal.services;

import exceptions.FatalException;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface IBackendDalServices {
    /**
     * Cette méthode vérifie s'il y a une connexion,en crée une s'il n'y en a pas et renvoie le
     * PrepareStatement à partir du query.
     *
     * @param query à retourner
     * @return un PrepareStatement
     * @throws FatalException s'il n'arrive pas à se connecter
     */
    PreparedStatement getPreparedStatement(String query) throws FatalException;

    PreparedStatement getPreparedStatementReturningId(String query) throws FatalException;

    Array getArrayId(Object[] array) throws SQLException;
}
