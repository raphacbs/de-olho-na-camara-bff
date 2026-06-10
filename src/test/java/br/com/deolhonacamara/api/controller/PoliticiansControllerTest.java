package br.com.deolhonacamara.api.controller;

import br.com.deolhonacamara.api.service.PoliticianService;
import br.com.deolhonacamara.api.service.UserService;
import net.coelho.deolhonacamara.api.model.PoliticianDto;
import net.coelho.deolhonacamara.api.model.PoliticianResponseDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PoliticiansControllerTest {

    @Mock
    private PoliticianService politicianService;

    @Mock
    private UserService userService;

    @InjectMocks
    private PoliticiansController politiciansController;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldPassAuthenticatedUserFiltersToListPoliticians() {
        UUID userId = UUID.randomUUID();
        Authentication authentication = org.mockito.Mockito.mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("user@example.com");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(userService.getUserIdByEmail("user@example.com")).thenReturn(userId);

        PoliticianResponseDTO expectedResponse = new PoliticianResponseDTO().data(List.of(new PoliticianDto())).page(0).sizePage(10).total(1).totalPages(1);
        when(politicianService.getAll(eq(0), eq(10), anyMap(), eq(userId))).thenReturn(expectedResponse);

        PoliticianResponseDTO response = politiciansController.listPoliticians(0, 10, "Ana", List.of("PSB"), List.of("PE"), true, 2026).getBody();

        ArgumentCaptor<Map<String, Object>> filtersCaptor = ArgumentCaptor.forClass(Map.class);
        verify(politicianService).getAll(eq(0), eq(10), filtersCaptor.capture(), eq(userId));
        Map<String, Object> filters = filtersCaptor.getValue();
        assertEquals("Ana", filters.get("name"));
        assertEquals(List.of("PSB"), filters.get("party"));
        assertEquals(List.of("PE"), filters.get("state"));
        assertEquals(true, filters.get("isFollowed"));
        assertEquals(2026, filters.get("year"));
        assertEquals(expectedResponse, response);
    }

    @Test
    void shouldFallbackToNullUserIdWhenAuthenticatedUserCannotBeResolved() {
        Authentication authentication = org.mockito.Mockito.mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("user@example.com");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(userService.getUserIdByEmail("user@example.com")).thenThrow(new RuntimeException("lookup failed"));

        PoliticianResponseDTO expectedResponse = new PoliticianResponseDTO();
        when(politicianService.getAll(eq(1), eq(20), anyMap(), org.mockito.ArgumentMatchers.isNull())).thenReturn(expectedResponse);

        PoliticianResponseDTO response = politiciansController.listPoliticians(1, 20, null, null, null, null, null).getBody();

        ArgumentCaptor<Map<String, Object>> filtersCaptor = ArgumentCaptor.forClass(Map.class);
        verify(politicianService).getAll(eq(1), eq(20), filtersCaptor.capture(), org.mockito.ArgumentMatchers.isNull());
        assertTrue(filtersCaptor.getValue().isEmpty());
        assertEquals(expectedResponse, response);
    }

    @Test
    void shouldDelegatePoliticianLookupUsingAuthenticatedUserId() {
        UUID userId = UUID.randomUUID();
        Authentication authentication = org.mockito.Mockito.mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("user@example.com");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(userService.getUserIdByEmail("user@example.com")).thenReturn(userId);

        PoliticianDto expected = new PoliticianDto();
        when(politicianService.getById(42, userId, 2025)).thenReturn(expected);

        PoliticianDto response = politiciansController.politiciansIdGet(42, 2025).getBody();

        verify(politicianService).getById(42, userId, 2025);
        assertEquals(expected, response);
    }
}
