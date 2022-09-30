package edu.byu.cs.tweeter.client.presenter;

import android.text.Editable;

import java.security.Provider;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.view.login.LoginFragment;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter {
    UserService service;
    private View view;
    public LoginPresenter(View view) {
        this.view = view;
        service = new UserService();
    }

    public interface View {
        void displayMessage(String message);

        void handleLogin(User loggedInUser);
    }
    public void loginTask(String alias, String password) {
        LoginObserver observer = new LoginObserver();
        service.loginTask(alias, password, observer);
    }
    public void validateLogin(String alias, String password) {
        service.validateLogin(alias, password);
    }

    private class LoginObserver implements UserService.LoginObserver {

        @Override
        public void handleLogin(User loggedInUser) {
            view.handleLogin(loggedInUser);
        }

        @Override
        public void displayErrorMessage(String message) {
            view.displayMessage("Failed to get followers: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to login because of exception: " + ex.getMessage());
        }
    }
}
