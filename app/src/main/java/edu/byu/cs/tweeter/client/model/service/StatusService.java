package edu.byu.cs.tweeter.client.model.service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.backgroundTask.handler.GetItemsHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.backgroundTask.observer.GetItemsObserver;
import edu.byu.cs.tweeter.client.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService {
    private BackgroundTaskUtils backgroundTaskUtils;

    public StatusService() {
        this.backgroundTaskUtils = new BackgroundTaskUtils();
    }

    public void postStatus(AuthToken currUserAuthToken, User currUser, String post, String formattedDateTime, List<String> parseURLs,
                           List<String> parseMentions, SimpleNotificationObserver postStatusObserver) {
        Status newStatus = new Status(post, currUser, formattedDateTime, parseURLs, parseMentions);
        PostStatusTask statusTask = new PostStatusTask(currUserAuthToken,
                newStatus, new SimpleNotificationHandler(postStatusObserver));
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.execute(statusTask);
        backgroundTaskUtils.runTask(statusTask);
    }

    public void loadMoreItems(AuthToken currUserAuthToken, User user, int pageSize, Status lastStatus, GetItemsObserver<Status> getItemsObserver) {
        GetFeedTask getFeedTask = new GetFeedTask(currUserAuthToken,
                user, pageSize, lastStatus, new GetItemsHandler<Status>(getItemsObserver));
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.execute(getFeedTask);
        backgroundTaskUtils.runTask(getFeedTask);
    }

}
