package dal.services;

import exceptions.FatalException;

public interface IDalServices {
    /**
     * Cette méthode se connecte à la DB si cette connection n'est pas faite.
     *
     * @throws FatalException s'il n'arrive pas à se connecter.
     */
    void establishConnection() throws FatalException;

    /**
     * Cette méthode permet de commencer une transaction.
     *
     * @throws FatalException s'il n'arrive pas à créer une transaction.
     */
    void startTransaction() throws FatalException;

    /**
     * Cette méthode permet de commit une transaction. Si la sémaphore est != 0, la transaction n'est
     * pas commit et rend la main à la transaction parent.
     *
     * @throws FatalException s'il n'arrive pas à commit la transaction.
     */
    void commitTransaction() throws FatalException;

    /**
     * Cette méthode permet de rollback si la transaction s'est mal produite.
     *
     * @throws FatalException s'il n'arrive pas à rollback la transaction.
     */
    void rollbackTransaction() throws FatalException;

    /**
     * Ferme la connexion.
     *
     * @throws FatalException s'il n'arrive pas à fermer
     */
    void closeConnection() throws FatalException;
}
