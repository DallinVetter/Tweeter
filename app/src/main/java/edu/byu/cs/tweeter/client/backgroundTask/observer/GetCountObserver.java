package edu.byu.cs.tweeter.client.backgroundTask.observer;

public interface GetCountObserver extends ServiceObserver{
    void handleSuccess(int count);
}
