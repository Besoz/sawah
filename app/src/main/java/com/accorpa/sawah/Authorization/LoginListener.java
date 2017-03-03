package com.accorpa.sawah.Authorization;

import com.accorpa.sawah.models.User;

/**
 * Created by Bassem on 16/01/17.
 */

public interface LoginListener {

    public void loginSuccess(User user);
    public void loginFailed(String message);
    public void loginError();
}
