package Domain.Validator;

import Domain.User;

public class UserValidator implements IValidator<User>{
    @Override
    public void validate(User entity) {
        if (entity.getFirstName() == "" || entity.getLastName() == "" || entity.getUsername() == "" || entity.getPassword() == "")
            throw new ValidationException("Invalid User!");
    }
}
