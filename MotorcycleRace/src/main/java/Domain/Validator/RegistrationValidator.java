package Domain.Validator;

import Domain.Registration;

public class RegistrationValidator implements IValidator<Registration> {
    @Override
    public void validate(Registration entity) throws ValidationException {
        if (entity.getRaceID() <0 || entity.getRiderID() <0)
            throw new ValidationException("Invalid Registration!");
    }
}
