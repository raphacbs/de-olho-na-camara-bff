package br.com.deolhonacamara.api.model.input;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PropositionInputTest {

    @Test
    void shouldBuildInputWithProvidedParameters() {
        PropositionInput input = PropositionInput.fromPoliticianQuery(10, 2, 15, 2026);

        assertEquals(2, input.getPage());
        assertEquals(15, input.getSizePage());
        assertEquals(10, input.getPropositionId());
        assertTrue(input.hasFilter("year"));
        assertEquals(Integer.valueOf(2026), input.<Integer>getFilter("year"));
    }

    @Test
    void shouldApplyDefaultPaginationWhenParametersAreNull() {
        PropositionInput input = PropositionInput.fromPoliticianQuery(20, null, null, null);

        assertEquals(0, input.getPage());
        assertEquals(20, input.getSizePage());
        assertEquals(20, input.getPropositionId());
        assertTrue(input.hasFilter("year"));
        assertNull(input.getFilter("year"));
    }
}
