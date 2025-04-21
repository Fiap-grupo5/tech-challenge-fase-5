package br.com.fiap.tech.identity.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AdministratorDTOTest {

    @Test
    void shouldCreateAdministratorDTOWithBuilder() {
        AdministratorDTO admin = AdministratorDTO.builder()
                .id(1L)
                .username("Moisés")
                .email("moises@exemplo.com")
                .build();

        assertThat(admin.getId()).isEqualTo(1L);
        assertThat(admin.getUsername()).isEqualTo("Moisés");
        assertThat(admin.getEmail()).isEqualTo("moises@exemplo.com");
    }

    @Test
    void shouldCreateAdministratorDTOWithAllArgsConstructor() {
        AdministratorDTO admin = new AdministratorDTO(2L, "Maria", "maria@exemplo.com");

        assertThat(admin.getId()).isEqualTo(2L);
        assertThat(admin.getUsername()).isEqualTo("Maria");
        assertThat(admin.getEmail()).isEqualTo("maria@exemplo.com");
    }

    @Test
    void shouldSetAndGetFields() {
        AdministratorDTO admin = new AdministratorDTO();
        admin.setId(3L);
        admin.setUsername("José");
        admin.setEmail("jose@exemplo.com");

        assertThat(admin.getId()).isEqualTo(3L);
        assertThat(admin.getUsername()).isEqualTo("José");
        assertThat(admin.getEmail()).isEqualTo("jose@exemplo.com");
    }

    @Test
    void shouldTestEqualsAndHashCode() {
        AdministratorDTO admin1 = new AdministratorDTO(4L, "Paulo", "paulo@exemplo.com");
        AdministratorDTO admin2 = new AdministratorDTO(4L, "Paulo", "paulo@exemplo.com");

        assertThat(admin1)
                .isEqualTo(admin2)
                .hasSameHashCodeAs(admin2);
    }

    @Test
    void shouldTestToString() {
        AdministratorDTO admin = new AdministratorDTO(5L, "Pedro", "pedro@exemplo.com");

        String expected = "AdministratorDTO(id=5, username=Pedro, email=pedro@exemplo.com)";
        assertThat(admin).hasToString(expected);
    }
}