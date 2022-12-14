package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.backgroundTask.observer.AuthenticationObserver;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class AuthenticationPresenter extends ParentPresenter {
    private final AuthenticationView view;
    protected UserService userService;

    public AuthenticationPresenter(AuthenticationView view) {
        this.view = view;
        userService = new UserService();
    }

    protected abstract String getDescription(boolean errOrEx);

    protected abstract String validateUser(String alias, String password);

    protected abstract String getAction();

    protected abstract void run(String alias, String password, AuthenticationObserver observer);

    public void authenticate(String alias, String password) {
        String errorMessage = validateUser(alias, password);
        if (errorMessage == null) {
            view.displayMessage(getAction());
            run(alias, password, new ValidateUserObserver());
        } else {
            view.displayErrorMessage(errorMessage);
        }
    }

    public interface AuthenticationView extends View {
        void displayErrorMessage(String message);

        void clearErrorMessage();

        void clearInfoMessage();

        void navigateToUser(User user);
    }

    protected class ValidateUserObserver implements AuthenticationObserver {

        @Override
        public void handleSuccess(User user, AuthToken authToken) {

            view.displayMessage("Hello " + Cache.getInstance().getCurrUser().getName());
            view.navigateToUser(user);
        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage(getDescription(true) + message);

        }

        @Override
        public void handleException(Exception ex) {
            view.displayMessage(getDescription(false) + ex.getMessage());
        }
    }
}