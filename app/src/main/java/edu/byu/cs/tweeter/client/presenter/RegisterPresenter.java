package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter {
    UserService service;
    private View view;

    public RegisterPresenter(View view) {
        this.view = view;
        service = new UserService();
    }

    public void registerTask(String firstName, String lastName, String alias, String password, String imageBytesBase64) {
        RegisterObserver observer = new RegisterObserver();
        service.registerTask(firstName, lastName, alias, password, imageBytesBase64, observer);
    }

    public void validateRegistration(String firstName, String lastName, String alias, String password, String imageBytesBase64) {
        service.validateRegistration(firstName, lastName, alias, password, imageBytesBase64);
    }

    public interface View {
        void displayMessage(String message);

        void handleRegister(User registeredUser);
    }

    private class RegisterObserver implements UserService.RegisterObserver {

        @Override
        public void handleRegister(User registeredUser) {
            view.handleRegister(registeredUser);
        }

        @Override
        public void displayErrorMessage(String message) {
            view.displayMessage("Failed to register: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to register because of exception: " + ex.getMessage());
        }
    }
}
