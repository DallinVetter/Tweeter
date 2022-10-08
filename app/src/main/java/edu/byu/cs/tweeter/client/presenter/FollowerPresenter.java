package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowerPresenter extends PagedPresenter<User> {

    public FollowerPresenter(PagedView<User> view) {
        super(view);
    }

    @Override
    protected void getItems(AuthToken authToken, User user, int pageSize, User lastItem, GetItemsObserver getItemsObserver) {
        followService.loadMoreFollowers(authToken, user, pageSize, lastItem, getItemsObserver);
    }

    @Override
    protected String getDescription(boolean errorOrException) {
        if (errorOrException) {
            return "Failed to get followers: ";
        } else {
            return "Failed to get followers because of exception: ";

        }
    }
}
