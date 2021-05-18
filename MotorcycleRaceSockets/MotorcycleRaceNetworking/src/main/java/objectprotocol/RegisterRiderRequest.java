package objectprotocol;

import Domain.Rider;
import Domain.User;

public class RegisterRiderRequest implements Request{
    Rider rider;
    User user;

    public RegisterRiderRequest(Rider rider, User user) {
        this.rider = rider;
        this.user = user;
    }

    public Rider getRider() {
        return rider;
    }

    public User getUser() {
        return user;
    }
}
