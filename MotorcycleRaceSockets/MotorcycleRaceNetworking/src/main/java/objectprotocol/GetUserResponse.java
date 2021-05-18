package objectprotocol;

import Domain.User;

public class GetUserResponse implements Response{
    private User user;

    public GetUserResponse(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

}
