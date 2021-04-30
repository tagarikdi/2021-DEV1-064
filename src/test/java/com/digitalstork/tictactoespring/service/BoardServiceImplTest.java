package com.digitalstork.tictactoespring.service;

import com.digitalstork.tictactoespring.dto.BoardDTO;
import com.digitalstork.tictactoespring.dto.RoundDTO;
import com.digitalstork.tictactoespring.model.Board;
import com.digitalstork.tictactoespring.model.enumeration.Box;
import com.digitalstork.tictactoespring.repository.BoardRepository;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;
import java.util.UUID;

import static junit.framework.TestCase.*;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BoardServiceImplTest {
    @InjectMocks
    BoardServiceImpl boardServiceImpl;
    @Mock
    BoardRepository boardRepository;

    @Test
    public void should_create_new_board_and_return_a_board_with_full_information() {

        // Given
        Board board = new Board().toBuilder()
                .id(UUID.randomUUID())
                .nextPlayer(Box.BLANK)
                .endBoard(false)
                .build();

        // When,
        when(boardRepository.save(any())).thenReturn(board);
        BoardDTO boardDTO = boardServiceImpl.createBoard();

        // Then
        assertNotNull(boardDTO);
        assertNotNull(boardDTO.getId());
        assertFalse(boardDTO.isEndBoard());
        assertEquals("X can start the game", boardDTO.getNextPlayer());
    }

    @Test
    public void should_throw_IllegalArgumentException_when_first_player_is_not_X() {
        // Given
        UUID uuid = UUID.randomUUID();
        RoundDTO roundDTO = RoundDTO.builder()
                .id(uuid.toString())
                .player("O")
                .build();

        Optional<Board> gameById = Optional.of(new Board().toBuilder()
                .id(uuid)
                .nextPlayer(Box.O)
                .topLeft(Box.BLANK)
                .build());

        // When
        when(boardRepository.findById(any())).thenReturn(gameById);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> boardServiceImpl.play(roundDTO)
        );

        // Then
        assertEquals("The first player should be: X", exception.getMessage());

    }

    @Test
    public void should_throw_IllegalArgumentException_when_round_player_name_not_equal_nextPlayer() {

        // Given
        UUID uuid = UUID.randomUUID();
        RoundDTO roundDTO = RoundDTO.builder()
                .id(uuid.toString())
                .player("X")
                .build();

        Board gameById = Board.builder()
                .id(uuid)
                .nextPlayer(Box.O)
                .build();

        // When
        when(boardRepository.findById(any())).thenReturn(Optional.of(gameById));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> boardServiceImpl.play(roundDTO)
        );

        // Then
        assertEquals("The next player should be: O", exception.getMessage());
    }

    @Test
    public void should_throw_IllegalArgumentException_when_the_game_is_end() {

        // Given
        UUID uuid = UUID.randomUUID();
        RoundDTO roundDTO = RoundDTO.builder()
                .id(uuid.toString())
                .player("X")
                .build();

        Board gameById = new Board().toBuilder()
                .id(uuid)
                .nextPlayer(Box.X)
                .endBoard(true)
                .build();

        // When
        when(boardRepository.findById(any())).thenReturn(Optional.of(gameById));
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> boardServiceImpl.play(roundDTO)
        );

        // Then
        assertEquals("The game is end and the winner was: No one", exception.getMessage());
    }

    @Test
    public void should_throw_IllegalArgumentException_when_box_isNotBlank() {

        // Given
        UUID uuid = UUID.randomUUID();
        RoundDTO roundDTO = RoundDTO.builder()
                .id(uuid.toString())
                .player("O")
                .col(0)
                .row(0)
                .build();

        Board gameById = new Board().toBuilder()
                .id(uuid)
                .topLeft(Box.O)
                .nextPlayer(Box.O)
                .endBoard(false)
                .build();

        // When
        when(boardRepository.findById(any())).thenReturn(Optional.of(gameById));
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> boardServiceImpl.play(roundDTO)
        );

        // Then
        assertEquals("The asked box is not Blank row = 0, col = 0 ", exception.getMessage());
    }

}