package br.com.fiap.tech.identity.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthenticationRequestTest {

    @Test
    void shouldCreateAuthenticationRequestWithBuilder() {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .username("João")
                .password("senha123")
                .build();

        assertThat(request.getUsername()).isEqualTo("João");
        assertThat(request.getPassword()).isEqualTo("senha123");
    }

    @Test
    void shouldCreateAuthenticationRequestWithAllArgsConstructor() {
        AuthenticationRequest request = new AuthenticationRequest("Maria", "senha456");

        assertThat(request.getUsername()).isEqualTo("Maria");
        assertThat(request.getPassword()).isEqualTo("senha456");
    }

    @Test
    void shouldSetAndGetFields() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("Pedro");
        request.setPassword("senha789");

        assertThat(request.getUsername()).isEqualTo("Pedro");
        assertThat(request.getPassword()).isEqualTo("senha789");
    }

    @Test
    void shouldTestEqualsAndHashCode() {
        AuthenticationRequest request1 = new AuthenticationRequest("Lucas", "senha123");
        AuthenticationRequest request2 = new AuthenticationRequest("Lucas", "senha123");

        assertThat(request1)
                .isEqualTo(request2)
                .hasSameHashCodeAs(request2);
    }

    @Test
    void shouldTestToString() {
        AuthenticationRequest request = new AuthenticationRequest("Paulo", "senha321");

        String expected = "AuthenticationRequest(username=Paulo, password=senha321)";
        assertThat(request).hasToString(expected);
    }
}