package br.com.fiap.tech.people.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AdministratorRequestTest {

    @Test
    void shouldSetAndGetFullName() {
        AdministratorRequest request = new AdministratorRequest();
        request.setFullName("Giorgian De Arrascaeta");

        assertThat(request.getFullName()).isEqualTo("Giorgian De Arrascaeta");
    }

    @Test
    void shouldSetAndGetCpf() {
        AdministratorRequest request = new AdministratorRequest();
        request.setCpf("12345678900");

        assertThat(request.getCpf()).isEqualTo("12345678900");
    }

    @Test
    void shouldSetAndGetEmail() {
        AdministratorRequest request = new AdministratorRequest();
        request.setEmail("arrasca.fla@example.com");

        assertThat(request.getEmail()).isEqualTo("arrasca.fla@example.com");
    }

    @Test
    void shouldSetAndGetPhoneNumber() {
        AdministratorRequest request = new AdministratorRequest();
        request.setPhoneNumber("123456789");

        assertThat(request.getPhoneNumber()).isEqualTo("123456789");
    }

    @Test
    void shouldVerifyEqualsAndHashCode() {
        AdministratorRequest request1 = new AdministratorRequest();
        request1.setFullName("Giorgian De Arrascaeta");
        request1.setCpf("12345678900");
        request1.setEmail("arrasca.fla@example.com");
        request1.setPhoneNumber("123456789");

        AdministratorRequest request2 = new AdministratorRequest();
        request2.setFullName("Giorgian De Arrascaeta");
        request2.setCpf("12345678900");
        request2.setEmail("arrasca.fla@example.com");
        request2.setPhoneNumber("123456789");

        assertThat(request1).isEqualTo(request2);
        assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
    }
}