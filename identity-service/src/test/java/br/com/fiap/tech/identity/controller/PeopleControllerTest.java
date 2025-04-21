package br.com.fiap.tech.identity.controller;

import br.com.fiap.tech.identity.service.PeopleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PeopleControllerTest {

    @Mock
    private PeopleService peopleService;

    @InjectMocks
    private PeopleController peopleController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCheckCpfExistsTrue() {
        String cpf = "12345678900";
        when(peopleService.checkCpfExists(cpf)).thenReturn(true);

        ResponseEntity<Boolean> response = peopleController.checkCpfExists(cpf);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody());
        verify(peopleService, times(1)).checkCpfExists(cpf);
    }

    @Test
    void testCheckCpfExistsFalse() {
        String cpf = "12345678900";
        when(peopleService.checkCpfExists(cpf)).thenReturn(false);

        ResponseEntity<Boolean> response = peopleController.checkCpfExists(cpf);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertFalse(response.getBody());
        verify(peopleService, times(1)).checkCpfExists(cpf);
    }

    @Test
    void testCheckCrmExistsTrue() {
        String crm = "CRM12345";
        when(peopleService.checkCrmExists(crm)).thenReturn(true);

        ResponseEntity<Boolean> response = peopleController.checkCrmExists(crm);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody());
        verify(peopleService, times(1)).checkCrmExists(crm);
    }

    @Test
    void testCheckCrmExistsFalse() {
        String crm = "CRM12342";
        when(peopleService.checkCrmExists(crm)).thenReturn(false);

        ResponseEntity<Boolean> response = peopleController.checkCrmExists(crm);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertFalse(response.getBody());
        verify(peopleService, times(1)).checkCrmExists(crm);
    }
}