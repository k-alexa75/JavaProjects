package Repository.UserRepository;

import Domain.User;

public interface IUserRepository  {

    Iterable<User> findAll();
    void save(User entity);
    User getUserByUsernameAndPassword(String username, String password);
}
