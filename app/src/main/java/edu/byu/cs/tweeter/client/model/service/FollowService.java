package edu.byu.cs.tweeter.client.model.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.backgroundTask.handler.GetCountHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.GetItemsHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.IsFollowerHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.backgroundTask.observer.GetCountObserver;
import edu.byu.cs.tweeter.client.backgroundTask.observer.GetItemsObserver;
import edu.byu.cs.tweeter.client.backgroundTask.observer.IsFollowerObserver;
import edu.byu.cs.tweeter.client.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {
    private final BackgroundTaskUtils backgroundTaskUtils;

    public FollowService() {
        backgroundTaskUtils = new BackgroundTaskUtils();
    }

    public void loadMoreFollowees(AuthToken currUserAuthToken, User user, int pageSize, User lastFollowee, GetItemsObserver<User> getItemsObserver) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(currUserAuthToken,
                user, pageSize, lastFollowee, new GetItemsHandler<User>(getItemsObserver));
        BackgroundTaskUtils.runTask(getFollowingTask);
    }

    public void loadMoreFollowers(AuthToken currUserAuthToken, User user, int pageSize, User lastFollower, GetItemsObserver<User> getItemsObserver) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(currUserAuthToken, user, pageSize, lastFollower, new GetItemsHandler<User>(getItemsObserver));
        BackgroundTaskUtils.runTask(getFollowersTask);
    }

    public void unfollow(AuthToken currUserAuthToken, User user, SimpleNotificationObserver unfollowObserver) {
        UnfollowTask unfollowTask = new UnfollowTask(currUserAuthToken,
                user, new SimpleNotificationHandler(unfollowObserver));
        BackgroundTaskUtils.runTask(unfollowTask);
    }

    public void follow(AuthToken currUserAuthToken, User user, SimpleNotificationObserver followObserver) {
        FollowTask followTask = new FollowTask(currUserAuthToken,
                user, new SimpleNotificationHandler(followObserver));
        BackgroundTaskUtils.runTask(followTask);
    }

    public void updateSelectedUserFollowingAndFollowers(AuthToken currUserAuthToken, User user, GetCountObserver followerCountObserver, GetCountObserver followingCountObserver) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Get count of most recently selected user's followers.
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(currUserAuthToken,
                user, new GetCountHandler(followerCountObserver));
        executor.execute(followersCountTask);

        // Get count of most recently selected user's followees (who they are following)
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(Cache.getInstance().getCurrUserAuthToken(),
                user, new GetCountHandler(followingCountObserver));
        executor.execute(followingCountTask);
    }

    public void isFollower(AuthToken currUserAuthToken, User currUser, User user, IsFollowerObserver isFollowerObserver) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(currUserAuthToken,
                currUser, user, new IsFollowerHandler(isFollowerObserver));
        BackgroundTaskUtils.runTask(isFollowerTask);
    }
}
