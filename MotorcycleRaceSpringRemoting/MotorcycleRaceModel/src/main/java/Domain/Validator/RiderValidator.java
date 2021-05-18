package Domain.Validator;

import Domain.Rider;

public class RiderValidator implements IValidator<Rider>{
    @Override
    public void validate(Rider entity) throws ValidationException {
        if (entity.getFirstName() == "" || entity.getLastName() == "" || entity.getTeam() =="")
            throw new ValidationException("Invalid Rider!");
    }
}
