package com.digitalstork.tictactoespring.web.rest;

import com.digitalstork.tictactoespring.dto.BoardDTO;
import com.digitalstork.tictactoespring.service.BoardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
class BoardRessourceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private BoardService boardService;

    @Test
    void should_return_OK_when_call_new_game_endpoint() {

        // Given
        String url = "http://localhost:" + port + "/api/tictactoe/new";
        UUID uuid = UUID.randomUUID();
        BoardDTO game = BoardDTO.builder()
                .id(uuid)
                .nextPlayer("O or X can start the game")
                .endBoard(false)
                .build();

        // When
        when(boardService.createBoard()).thenReturn(game);
        ResponseEntity<BoardDTO> response = this.restTemplate.exchange(url, HttpMethod.GET, null, BoardDTO.class);

        //Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        var responseBody = response.getBody();
        if(responseBody != null) {
            assertEquals(uuid, response.getBody().getId());
        }
        assertEquals("O or X can start the game", response.getBody().getNextPlayer());
        assertFalse(response.getBody().isEndBoard());
    }

}