package com.digitalstork.tictactoespring.web.rest;
import com.digitalstork.tictactoespring.exception.Error;

import com.digitalstork.tictactoespring.dto.BoardDTO;
import com.digitalstork.tictactoespring.dto.RoundDTO;
import com.digitalstork.tictactoespring.service.BoardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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


    @Test
    @DisplayName("The col and row should be between or equals 0 and 2.")
    void should_throw_IllegalArgumentException_when_row_or_col_are_not_correct() {

        // Given
        String url = "http://localhost:" + port + "/api/tictactoe/play";
        RoundDTO round = RoundDTO.builder()
                .col(3)
                .row(-1)
                .build();
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");

        // When
        ResponseEntity<Error> response = this.restTemplate.exchange(
                url, HttpMethod.POST, new HttpEntity(round, headers), Error.class);

        //Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Wrong row or column information, they should be between 0 and 2!", response.getBody().getMessage());

    }

    @Test
    @DisplayName("The player should be X or O.")
    void should_throw_IllegalArgumentException_when_player_is_not_X_or_O() {

        // Given
        String url = "http://localhost:" + port + "/api/tictactoe/play";
        RoundDTO round = RoundDTO.builder()
                .col(2)
                .row(0)
                .player("#")
                .build();

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");


        // When
        ResponseEntity<Error> response = this.restTemplate.exchange(

                url,
                HttpMethod.POST,
                new HttpEntity(round, headers),
                Error.class
        );

        //Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Wrong player name, it should be X or O.", response.getBody().getMessage());
    }

    @Test
    @DisplayName("The return OK when all information are correct.")
    void should_return_OK_when_all_information_are_correct() {

        // Given
        String url = "http://localhost:" + port + "/api/tictactoe/play";
        UUID uuid = UUID.randomUUID();
        RoundDTO round = RoundDTO.builder()
                .id(uuid.toString())
                .col(2)
                .row(0)
                .player("O")
                .build();

        BoardDTO game = BoardDTO.builder()
                .id(uuid)
                .endBoard(false)
                .nextPlayer("X")
                .build();

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");

        // When
        when(boardService.play(any())).thenReturn(game);
        ResponseEntity<BoardDTO> response = this.restTemplate.exchange(
                url, HttpMethod.POST, new HttpEntity(round, headers), BoardDTO.class);

        //Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        BoardDTO body = response.getBody();
        assertNotNull(body);
        assertEquals(uuid, body.getId());
        assertFalse(body.isEndBoard());
        assertEquals("X", body.getNextPlayer());
    }

}