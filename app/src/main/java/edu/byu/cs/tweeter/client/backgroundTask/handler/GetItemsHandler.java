package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.backgroundTask.observer.GetItemsObserver;

public class GetItemsHandler<T> extends BackgroundTaskHandler<GetItemsObserver> {
    public GetItemsHandler(GetItemsObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, GetItemsObserver observer) {
        List<T> items = (List<T>) data.getSerializable(GetFollowingTask.ITEMS_KEY);
        boolean hasMorePages = data.getBoolean(GetFollowingTask.MORE_PAGES_KEY);
        observer.handleSuccess(items, hasMorePages);
    }
}
