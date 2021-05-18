package Repository.RegistrationRepository;

import Domain.Registration;

public interface IRegistrationRepository {

    Iterable<Registration> findAll();
    void save(Registration entity);

}
