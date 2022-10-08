package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.observer.GetUserObserver;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<T> extends ParentPresenter {
    protected static final int PAGE_SIZE = 10;
    protected User targetUser;
    protected AuthToken authToken;
    protected T lastItem;
    protected boolean hasMorePages;
    protected boolean isLoading;
//    protected boolean isGettingUser;
    private final PagedView<T> view;
    protected UserService userService;
    protected StatusService statusService;
    protected FollowService followService;

    public interface PagedView<T> extends View {
        // duplicate page view code
        void setLoadingFooter(boolean footerStatus);

        void displayUser(User user);

        void addItems(List<T> items);
    }

    public PagedPresenter(PagedView<T> view) {
        this.view = view;
        this.userService = new UserService();
        this.statusService = new StatusService();
        this.followService = new FollowService();
    }

    public void loadMoreItems(User targetUser) {
        isLoading = true;
        view.setLoadingFooter(true);
        getItems(Cache.getInstance().getCurrUserAuthToken(), targetUser, PAGE_SIZE, lastItem, new GetItemsObserver());
    }

    public void getUser(AuthToken authToken, String userAlias) {
        userService.getUserTask(authToken, userAlias, new GetUserProfileObserver());
    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    protected abstract void getItems(AuthToken authToken, User user, int pageSize, T lastItem, GetItemsObserver getItemsObserver);

    protected abstract String getDescription(boolean errorOrException);

    protected class GetItemsObserver implements edu.byu.cs.tweeter.client.backgroundTask.observer.GetItemsObserver<T> {
        @Override
        public void handleSuccess(List<T> items, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(false);

            lastItem = (items.size() > 0) ? items.get(items.size() - 1) : null;
            view.addItems(items);

            setHasMorePages(hasMorePages);
        }

        @Override
        public void handleFailure(String message) {
            isLoading = false;
            view.setLoadingFooter(false);

            view.displayMessage(getDescription(true) + ": " + message);
        }

        @Override
        public void handleException(Exception ex) {
            isLoading = false;
            view.setLoadingFooter(false);

            view.displayMessage(getDescription(false) + ": " + ex.getMessage());
        }
    }

    protected class GetUserProfileObserver implements GetUserObserver {
        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to get user's profile: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayMessage("Failed to get user's profile because of exception: " + ex.getMessage());
        }

        @Override
        public void handleSuccess(User user) {
            view.displayUser(user);
        }
    }
}
