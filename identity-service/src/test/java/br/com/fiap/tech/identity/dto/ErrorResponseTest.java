package br.com.fiap.tech.identity.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorResponseTest {

    @Test
    void shouldCreateErrorResponseWithAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        ErrorResponse errorResponse = new ErrorResponse("Erro de validação", now);

        assertThat(errorResponse.getMessage()).isEqualTo("Erro de validação");
        assertThat(errorResponse.getTimestamp()).isEqualTo(now);
    }

    @Test
    void shouldCreateErrorResponseWithMessageConstructor() {
        String message = "Erro inesperado";
        ErrorResponse errorResponse = new ErrorResponse(message);

        assertThat(errorResponse.getMessage()).isEqualTo(message);
        assertThat(errorResponse.getTimestamp()).isNotNull();
    }

    @Test
    void shouldSetAndGetFields() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("Erro de autenticação");
        LocalDateTime now = LocalDateTime.now();
        errorResponse.setTimestamp(now);

        assertThat(errorResponse.getMessage()).isEqualTo("Erro de autenticação");
        assertThat(errorResponse.getTimestamp()).isEqualTo(now);
    }

    @Test
    void shouldTestEqualsAndHashCode() {
        LocalDateTime now = LocalDateTime.now();
        ErrorResponse errorResponse1 = new ErrorResponse("Erro", now);
        ErrorResponse errorResponse2 = new ErrorResponse("Erro", now);

        assertThat(errorResponse1)
                .isEqualTo(errorResponse2)
                .hasSameHashCodeAs(errorResponse2);
    }

    @Test
    void shouldTestToString() {
        LocalDateTime now = LocalDateTime.of(2023, 10, 1, 12, 0);
        ErrorResponse errorResponse = new ErrorResponse("Erro crítico", now);

        String expected = "ErrorResponse(message=Erro crítico, timestamp=2023-10-01T12:00)";
        assertThat(errorResponse).hasToString(expected);
    }
}