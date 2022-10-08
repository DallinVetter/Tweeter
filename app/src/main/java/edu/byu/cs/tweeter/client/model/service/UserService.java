package edu.byu.cs.tweeter.client.model.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.backgroundTask.handler.AuthenticationHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.GetUserHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.backgroundTask.observer.AuthenticationObserver;
import edu.byu.cs.tweeter.client.backgroundTask.observer.GetUserObserver;
import edu.byu.cs.tweeter.client.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;

public class UserService {
    private BackgroundTaskUtils backgroundTaskUtils;

    public UserService() {
        backgroundTaskUtils = new BackgroundTaskUtils();
    }

    public void logout(AuthToken authToken, SimpleNotificationObserver logoutObserver) {
        LogoutTask logoutTask = new LogoutTask(authToken, new SimpleNotificationHandler(logoutObserver));
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.execute(logoutTask);
        backgroundTaskUtils.runTask(logoutTask);
    }

    public void loginTask(String alias, String password, AuthenticationObserver observer) {
        LoginTask loginTask = new LoginTask(alias,
                password,
                new AuthenticationHandler(observer));
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.execute(loginTask);
        backgroundTaskUtils.runTask(loginTask);
    }

    public void registerTask(String firstName, String lastName, String alias, String password, String imageBytesBase64, AuthenticationObserver observer) {
        RegisterTask registerTask = new RegisterTask(firstName, lastName,
                alias, password, imageBytesBase64, new AuthenticationHandler(observer));

//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.execute(registerTask);
        backgroundTaskUtils.runTask(registerTask);
    }

    public void getUserTask(AuthToken authToken, String alias, GetUserObserver observer) {
        GetUserTask getUserTask = new GetUserTask(authToken,
                alias, new GetUserHandler(observer));
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.execute(getUserTask);
        backgroundTaskUtils.runTask(getUserTask);
    }
}
