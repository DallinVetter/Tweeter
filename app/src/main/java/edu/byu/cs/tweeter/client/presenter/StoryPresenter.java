package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter {
    private static final int PAGE_SIZE = 10;
    StatusService statusService;
    UserService userService;
    private StoryPresenter.View view;
    private Status lastStatus;

    private boolean hasMorePages;
    private boolean isLoading = false;

    public StoryPresenter(StoryPresenter.View view) {
        this.view = view;
        statusService = new StatusService();
        userService = new UserService();
    }

    public void loadMoreItems(User user) {
        isLoading = true;
        view.setLoadingFooter(true);
        statusService.loadMoreItems(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastStatus, new StoryPresenter.GetStoryObserver());
    }

    public void getUser(AuthToken authToken, String alias) {
        userService.getUserTask(authToken, alias, new GetUserObserver());
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public interface View {
        void displayMessage(String message);

        void setLoadingFooter(boolean value);

        void handleStory(List<Status> feed);

        void displayUser(User user);
    }

    private class GetStoryObserver implements StatusService.GetStoryObserver {

        @Override
        public void handleStory(List<Status> statuses, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(false);
            lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
            view.handleStory(statuses);
            StoryPresenter.this.hasMorePages = hasMorePages;
        }

        @Override
        public void displayErrorMessage(String message) {
            isLoading = false;
            view.displayMessage("Failed to get story: " + message);
            view.setLoadingFooter(false);
        }

        @Override
        public void displayException(Exception ex) {
            isLoading = false;
            view.displayMessage("Failed to get story because of exception: " + ex.getMessage());
            view.setLoadingFooter(false);
        }
    }

    private class GetUserObserver implements UserService.GetUserObserver {

        @Override
        public void handleGetUser(User user) {
            view.displayUser(user);
        }

        @Override
        public void displayErrorMessage(String message) {
            isLoading = false;
            view.displayMessage("Failed to get user's profile: " +  message);
            view.setLoadingFooter(false);
        }

        @Override
        public void displayException(Exception ex) {
            isLoading = false;
            view.displayMessage("Failed to get user's profile because of exception: " + ex.getMessage());
            view.setLoadingFooter(false);
        }
    }
}
