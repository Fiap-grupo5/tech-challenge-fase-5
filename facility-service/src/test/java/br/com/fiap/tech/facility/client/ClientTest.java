package br.com.fiap.tech.facility.client;

import br.com.fiap.tech.facility.dto.AdministratorResponse;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ClientTest {

    @MockBean
    private IdentityClient identityClient;

    @Test
    void testGetAdministrator_Success() {
        Long id = 1L;
        AdministratorResponse response = new AdministratorResponse();
        response.setId(id);
        response.setFullName("Filipe Luis");

        when(identityClient.getAdministrator(id)).thenReturn(response);

        AdministratorResponse result = identityClient.getAdministrator(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Filipe Luis", result.getFullName());
        verify(identityClient, times(1)).getAdministrator(id);
    }

    @Test
    void testGetAdministrator_NotFound() {
        Long id = 2L;

        when(identityClient.getAdministrator(id)).thenThrow(new EntityNotFoundException("Administrador com ID " + id + " não encontrado"));

        Exception exception = assertThrows(EntityNotFoundException.class, () -> identityClient.getAdministrator(id));

        assertEquals("Administrador com ID " + id + " não encontrado", exception.getMessage());
        verify(identityClient, times(1)).getAdministrator(id);
    }

    @Test
    void testAdministratorExists_True() {
        Long id = 1L;
        AdministratorResponse response = new AdministratorResponse();
        response.setId(id);

        when(identityClient.getAdministrator(id)).thenReturn(response);
        when(identityClient.administratorExists(id)).thenCallRealMethod();

        boolean exists = identityClient.administratorExists(id);

        assertTrue(exists);
        verify(identityClient, times(1)).getAdministrator(id);
    }

    @Test
    void testAdministratorExists_False() {
        Long id = 2L;

        when(identityClient.getAdministrator(id)).thenThrow(new EntityNotFoundException("Administrador com ID " + id + " não encontrado"));
        when(identityClient.administratorExists(id)).thenCallRealMethod();

        Exception exception = assertThrows(EntityNotFoundException.class, () -> identityClient.administratorExists(id));

        assertEquals("Administrador com ID " + id + " não encontrado", exception.getMessage());
        verify(identityClient, times(1)).getAdministrator(id);
    }
}