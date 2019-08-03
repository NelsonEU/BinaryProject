package biz.bizObject;

import biz.dto.IUserDto;

public interface IUserBiz {
    /**
     * Vérifie si le mot de passe de l'utilisateur avec celui entré.
     *
     * @param user utilisateur
     * @param password mot de passe
     * @return true si le password concorde, false sinon
     */
    boolean checkPassword(IUserDto user, String password);

    /**
     * Vérifie les données entrées par l'utilisateur avant d'effectuer l'inscription (sauf le mot de
     * passe).
     */
    public void checkData();

    /**
     * Vérifie les données entrées par l'utilisateur avant d'effectuer l'inscription (avec le mot de
     * passe).
     */
    void checkDataSignIn();

    /**
     * Renvoie l'UserDto passé en paramètre en cryptant son mot de passe.
     *
     * @param userDto le userDto
     * @return l'UserDto avec son mot de passe crypté
     */
    IUserDto encryptPassword(IUserDto userDto);
}
