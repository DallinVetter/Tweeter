package edu.byu.cs.tweeter.client.presenter;

public abstract class ParentPresenter {
    public interface View {
        void displayMessage(String message);
    }
}
