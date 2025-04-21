package br.com.fiap.tech.identity.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthenticationResponseTest {

    @Test
    void shouldCreateAuthenticationResponseWithBuilder() {
        AuthenticationResponse response = AuthenticationResponse.builder()
                .token("abc123")
                .userId(1L)
                .username("João")
                .build();

        assertThat(response.getToken()).isEqualTo("abc123");
        assertThat(response.getUserId()).isEqualTo(1L);
        assertThat(response.getUsername()).isEqualTo("João");
    }

    @Test
    void shouldCreateAuthenticationResponseWithAllArgsConstructor() {
        AuthenticationResponse response = new AuthenticationResponse("xyz789", 2L, "Maria");

        assertThat(response.getToken()).isEqualTo("xyz789");
        assertThat(response.getUserId()).isEqualTo(2L);
        assertThat(response.getUsername()).isEqualTo("Maria");
    }

    @Test
    void shouldSetAndGetFields() {
        AuthenticationResponse response = new AuthenticationResponse();
        response.setToken("token123");
        response.setUserId(3L);
        response.setUsername("Pedro");

        assertThat(response.getToken()).isEqualTo("token123");
        assertThat(response.getUserId()).isEqualTo(3L);
        assertThat(response.getUsername()).isEqualTo("Pedro");
    }

    @Test
    void shouldTestEqualsAndHashCode() {
        AuthenticationResponse response1 = new AuthenticationResponse("token456", 4L, "Lucas");
        AuthenticationResponse response2 = new AuthenticationResponse("token456", 4L, "Lucas");

        assertThat(response1)
                .isEqualTo(response2)
                .hasSameHashCodeAs(response2);
    }

    @Test
    void shouldTestToString() {
        AuthenticationResponse response = new AuthenticationResponse("token789", 5L, "Paulo");

        String expected = "AuthenticationResponse(token=token789, userId=5, username=Paulo)";
        assertThat(response).hasToString(expected);
    }
}