package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.backgroundTask.observer.AuthenticationObserver;

public class RegisterPresenter extends AuthenticationPresenter {
    AuthenticationView view;
    String firstName;
    String lastName;
    String imageStringToUpload;

    public RegisterPresenter(AuthenticationView view) {
        super(view);
        this.view = view;
    }

    @Override
    protected String getDescription(boolean errOrEx) {
        if (errOrEx) {
            return "Failed to register: ";
        } else {
            return "Failed to get register because of exception: ";
        }
    }

    @Override
    public String validateUser(String alias, String password) {
        if (firstName.length() == 0) {
            return "First Name cannot be empty.";
        }
        if (lastName.length() == 0) {
            return "Last Name cannot be empty.";
        }
        if (alias.length() == 0) {
            return "Alias cannot be empty.";
        }
        if (alias.charAt(0) != '@') {
            return "Alias must begin with @.";
        }
        if (alias.length() < 2) {
            return "Alias must contain 1 or more characters after the @.";
        }
        if (password.length() == 0) {
            return "Password cannot be empty.";
        }

        if (imageStringToUpload == "") {
            throw new IllegalArgumentException("Profile image must be uploaded.");
        }
        return null;
    }

    @Override
    protected String getAction() {
        return "Registering...";
    }

    @Override
    protected void run(String alias, String password, AuthenticationObserver observer) {
        userService.registerTask(firstName, lastName, alias, password, imageStringToUpload, observer);

    }

    public void setRegistrationInfo(String firstName, String lastName, String imageStringToUpload) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageStringToUpload = imageStringToUpload;
    }


}
