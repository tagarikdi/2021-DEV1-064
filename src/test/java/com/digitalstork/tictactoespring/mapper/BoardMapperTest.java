package com.digitalstork.tictactoespring.mapper;

import com.digitalstork.tictactoespring.dto.BoardDTO;
import com.digitalstork.tictactoespring.model.Board;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BoardMapperTest {

    private static final BoardMapper BOARD_MAPPER = new BoardMapper();

    @Test
    void should_transform_from_Board_to_BoardDTO_when_using_default_values() {

        // Given
        Board board = new Board().toBuilder()
                .id(UUID.randomUUID())
                .build();

        // When
        BoardDTO game = BOARD_MAPPER.apply(board);

        // Then
        assertNotNull(game);
        assertNotNull(game.getId());
        assertFalse(game.isEndBoard());
        assertEquals("X can start the game", game.getNextPlayer());
    }

}