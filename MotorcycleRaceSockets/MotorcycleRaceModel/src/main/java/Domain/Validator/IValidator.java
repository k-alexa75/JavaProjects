package Domain.Validator;

public interface IValidator<T> {
    void validate(T entity);
}
