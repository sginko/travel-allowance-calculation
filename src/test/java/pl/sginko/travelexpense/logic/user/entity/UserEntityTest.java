package pl.sginko.travelexpense.logic.user.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserEntityTest {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    public void should_create_user_entity() {
        // GIVEN
        String email = "test@example.com";
        String name = "name";
        String surname = "surname";
        String password = "password123";

        // WHEN
        UserEntity user = new UserEntity(email, name, surname, password);

        // THEN
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getSurname()).isEqualTo(surname);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getRoles()).isEqualTo(Roles.ROLE_USER);
    }

    @Test
    public void should_change_role_to_accountant() {
        // GIVEN
        String email = "test@example.com";
        String name = "name";
        String surname = "surname";
        String password = "password123";

        UserEntity user = new UserEntity(email, name, surname, password);

        // WHEN
        user.changeRoleToAccountant();

        // THEN
        assertThat(user.getRoles()).isEqualTo(Roles.ROLE_ACCOUNTANT);
    }

    @Test
    public void should_change_role_to_manager() {
        // GIVEN
        String email = "manager@example.com";
        String name = "Jane";
        String surname = "Smith";
        String password = "password456";

        UserEntity user = new UserEntity(email, name, surname, password);

        // WHEN
        user.changeRoleToManager();

        // THEN
        assertThat(user.getRoles()).isEqualTo(Roles.ROLE_MANAGER);
    }

    @Test
    public void should_fail_when_email_is_invalid() {
        // GIVEN
        String invalidEmail = "invalid-email";
        String name = "name";
        String surname = "surname";
        String password = "password123";

        // WHEN
        UserEntity user = new UserEntity(invalidEmail, name, surname, password);
        Set<ConstraintViolation<UserEntity>> violations = validator.validate(user);

        // THEN
        assertThat(violations).anyMatch(v -> v.getMessage().equals("Invalid email format"));
    }

    @Test
    public void should_fail_when_password_is_too_short() {
        // GIVEN
        String email = "test@example.com";
        String name = "name";
        String surname = "surname";
        String shortPassword = "short";

        // WHEN
        UserEntity user = new UserEntity(email, name, surname, shortPassword);
        Set<ConstraintViolation<UserEntity>> violations = validator.validate(user);

        // THEN
        assertThat(violations).anyMatch(v -> v.getMessage().equals("Password must be at least 8 characters long"));
    }
}
