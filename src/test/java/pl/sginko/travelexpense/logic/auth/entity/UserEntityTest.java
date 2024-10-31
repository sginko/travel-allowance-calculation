package pl.sginko.travelexpense.logic.auth.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserEntityTest {

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
        assertThat(user.getRoles()).isEqualTo("ROLE_USER");
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
        assertThat(user.getRoles()).isEqualTo("ROLE_ACCOUNTANT");
    }
}