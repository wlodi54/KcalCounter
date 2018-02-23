package com.example.sywlia.licznikkaloryczny.interfaces;

import com.example.sywlia.licznikkaloryczny.models.UserDTO;

import java.util.ArrayList;

/**
 * Created by sywlia on 2017-05-16.
 */

public interface IUserDAO {
    UserDTO getUserLoged();

    UserDTO getUser(long id);

    void deleteUser(long id);

    ArrayList<UserDTO> getUsersList();

    long insertUser(UserDTO user) throws Exception;

    void loginUser(long idUser);

    void increaseMedals(long idUser,int odznaka);

}
