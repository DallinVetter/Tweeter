package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter extends PagedPresenter<Status> {

    public StoryPresenter(PagedView<Status> view) {
        super(view);
    }

    @Override
    protected void getItems(AuthToken authToken, User user, int pageSize, Status lastItem, GetItemsObserver getItemsObserver) {
        statusService.loadStory(authToken, user, pageSize, lastItem, getItemsObserver);
    }

    @Override
    protected String getDescription(boolean errorOrException) {
        if (errorOrException) {
            return "Failed to get story: ";
        } else {
            return "Failed to get story because of exception: ";

        }
    }
}
