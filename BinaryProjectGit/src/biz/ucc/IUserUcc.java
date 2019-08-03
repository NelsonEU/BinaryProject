package biz.ucc;

import biz.dto.IUserDto;

public interface IUserUcc {
    /**
     * Renvoie l'UserDto correspondant au login et mot de passe, renvoie null si il ne le trouve pas.
     *
     * @param username login
     * @param passwd paswd
     * @return un UserDto s'il le trouve dans la Db, null sinon
     */
    IUserDto login(String username, String passwd);

    /**
     * Renvoie l'UserDto inséré dans la DB, renvoie null si il n'a pas réussi à l'insérer.
     *
     * @param email, password, username
     * @return un UserDto s'il l'a inséré dans la DB, null sinon
     */
    IUserDto register(String email, String password, String username);
}
