package Domain.Validator;

import Domain.Race;

public class RaceValidator implements IValidator<Race> {
    @Override
    public void validate(Race entity) throws ValidationException{
        if (entity.getName() == "")
            throw new ValidationException("Invalid Race!");
    }
}
