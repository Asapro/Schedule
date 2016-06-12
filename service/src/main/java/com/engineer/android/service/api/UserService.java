package com.engineer.android.service.api;

/**
 *
 * Created by L.J on 2016/6/10.
 */
public class UserService {
    private static UserService instance = new UserService();

    public static UserService getInstance(){
        return instance;
    }

    private UserService(){

    }


}
