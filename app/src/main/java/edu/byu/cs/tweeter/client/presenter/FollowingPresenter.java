package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter extends PagedPresenter<User> {
    public FollowingPresenter(PagedView<User> view) {
        super(view);
    }

    @Override
    protected void getItems(AuthToken authToken, User user, int pageSize, User lastItem, GetItemsObserver getItemsObserver) {
        followService.loadMoreFollowees(authToken, user, pageSize, lastItem, getItemsObserver);
    }

    @Override
    protected String getDescription(boolean errorOrException) {
        if (errorOrException) {
            return "Failed to get followees: ";
        } else {
            return "Failed to get followees because of exception: ";

        }
    }
}
