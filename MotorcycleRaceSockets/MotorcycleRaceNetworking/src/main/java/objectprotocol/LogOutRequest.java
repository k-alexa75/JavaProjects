package objectprotocol;

import Domain.User;

public class LogOutRequest implements Request{
    private User user;

    public LogOutRequest(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
