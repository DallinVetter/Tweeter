package edu.byu.cs.tweeter.client.backgroundTask.observer;

public interface IsFollowerObserver extends ServiceObserver {
    void handleSuccess(boolean isFollower);
}
