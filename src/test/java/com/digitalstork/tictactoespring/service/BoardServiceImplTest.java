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
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;
import java.util.UUID;

import static junit.framework.TestCase.*;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

    @Test
    public void should_throw_IllegalArgumentException_when_the_game_is_end_and_winner_is_O_with_any_case() {

        // Given
        UUID uuid = UUID.randomUUID();
        RoundDTO roundDTO = RoundDTO.builder()
                .id(uuid.toString())
                .player("O")
                .build();

        Board gameById = new Board().toBuilder()
                .id(uuid)
                .nextPlayer(Box.O)
                .topLeft(Box.O)
                .topCenter(Box.O)
                .topRight(Box.O)
                .endBoard(true)
                .build();

        // When
        when(boardRepository.findById(any())).thenReturn(Optional.of(gameById));
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> boardServiceImpl.play(roundDTO)
        );

        // Then
        assertEquals("The game is end and the winner was: O", exception.getMessage());
    }

    @Test
    public void should_throw_IllegalArgumentException_when_the_game_is_end_and_winner_is_X_with_any_case() {

        // Given
        UUID uuid = UUID.randomUUID();
        RoundDTO roundDTO = RoundDTO.builder()
                .id(uuid.toString())
                .player("X")
                .build();

        Board gameById = new Board().toBuilder()
                .id(uuid)
                .nextPlayer(Box.X)
                .topLeft(Box.O)
                .topCenter(Box.O)
                .topRight(Box.O)
                .endBoard(true)
                .build();

        // When
        when(boardRepository.findById(any())).thenReturn(Optional.of(gameById));
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> boardServiceImpl.play(roundDTO)
        );

        // Then
        assertEquals("The game is end and the winner was: O", exception.getMessage());
    }

    @Test
    public void should_throw_IllegalArgumentException_when_the_game_is_end_and_match_case_10_11_12() {

        // Given
        UUID uuid = UUID.randomUUID();
        RoundDTO roundDTO = RoundDTO.builder()
                .id(uuid.toString())
                .player("X")
                .build();

        Board gameById = new Board().toBuilder()
                .id(uuid)
                .nextPlayer(Box.X)
                .centerLeft(Box.O)
                .center(Box.O)
                .centerRight(Box.O)
                .endBoard(true)
                .build();

        // When
        when(boardRepository.findById(any())).thenReturn(Optional.of(gameById));
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> boardServiceImpl.play(roundDTO)
        );

        // Then
        assertEquals("The game is end and the winner was: O", exception.getMessage());
    }

    @Test
    public void should_throw_IllegalArgumentException_when_the_game_is_end_and_match_case_20_21_22() {

        // Given
        UUID uuid = UUID.randomUUID();
        RoundDTO roundDTO = RoundDTO.builder()
                .id(uuid.toString())
                .player("X")
                .build();

        Board gameById = new Board().toBuilder()
                .id(uuid)
                .nextPlayer(Box.X)
                .bottomLeft(Box.O)
                .bottomCenter(Box.O)
                .bottomRight(Box.O)
                .endBoard(true)
                .build();

        // When
        when(boardRepository.findById(any())).thenReturn(Optional.of(gameById));
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> boardServiceImpl.play(roundDTO)
        );

        // Then
        assertEquals("The game is end and the winner was: O", exception.getMessage());
    }

    @Test
    public void should_throw_IllegalArgumentException_when_the_game_is_end_and_match_case_00_10_20() {

        // Given
        UUID uuid = UUID.randomUUID();
        RoundDTO roundDTO = RoundDTO.builder()
                .id(uuid.toString())
                .player("X")
                .build();

        Board gameById = new Board().toBuilder()
                .id(uuid)
                .nextPlayer(Box.X)
                .topLeft(Box.O)
                .centerLeft(Box.O)
                .bottomLeft(Box.O)
                .endBoard(true)
                .build();

        // When
        when(boardRepository.findById(any())).thenReturn(Optional.of(gameById));
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> boardServiceImpl.play(roundDTO)
        );

        // Then
        assertEquals("The game is end and the winner was: O", exception.getMessage());
    }

    @Test
    public void should_throw_IllegalArgumentException_when_the_game_is_end_and_match_case_01_11_21() {

        // Given
        UUID uuid = UUID.randomUUID();
        RoundDTO roundDTO = RoundDTO.builder()
                .id(uuid.toString())
                .player("X")
                .build();

        Board gameById = new Board().toBuilder()
                .id(uuid)
                .nextPlayer(Box.X)
                .topCenter(Box.O)
                .center(Box.O)
                .bottomCenter(Box.O)
                .endBoard(true)
                .build();

        // When
        when(boardRepository.findById(any())).thenReturn(Optional.of(gameById));
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> boardServiceImpl.play(roundDTO)
        );

        // Then
        assertEquals("The game is end and the winner was: O", exception.getMessage());
    }

    @Test
    public void should_throw_IllegalArgumentException_when_the_game_is_end_and_match_case_02_12_22() {

        // Given
        UUID uuid = UUID.randomUUID();
        RoundDTO roundDTO = RoundDTO.builder()
                .id(uuid.toString())
                .player("X")
                .build();

        Board gameById = new Board().toBuilder()
                .id(uuid)
                .nextPlayer(Box.X)
                .topRight(Box.O)
                .centerRight(Box.O)
                .bottomRight(Box.O)
                .endBoard(true)
                .build();

        // When
        when(boardRepository.findById(any())).thenReturn(Optional.of(gameById));
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> boardServiceImpl.play(roundDTO)
        );

        // Then
        assertEquals("The game is end and the winner was: O", exception.getMessage());
    }

    @Test
    public void should_throw_IllegalArgumentException_when_the_game_is_end_and_match_case_00_11_22() {

        // Given
        UUID uuid = UUID.randomUUID();
        RoundDTO roundDTO = RoundDTO.builder()
                .id(uuid.toString())
                .player("X")
                .build();

        Board gameById = new Board().toBuilder()
                .id(uuid)
                .nextPlayer(Box.X)
                .topLeft(Box.O)
                .center(Box.O)
                .bottomRight(Box.O)
                .endBoard(true)
                .build();

        // When
        when(boardRepository.findById(any())).thenReturn(Optional.of(gameById));
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> boardServiceImpl.play(roundDTO)
        );

        // Then
        assertEquals("The game is end and the winner was: O", exception.getMessage());
    }

    @Test
    public void should_throw_IllegalArgumentException_when_the_game_is_end_and_match_case_02_11_20() {

        // Given
        UUID uuid = UUID.randomUUID();
        RoundDTO roundDTO = RoundDTO.builder()
                .id(uuid.toString())
                .player("X")
                .build();

        Board gameById = new Board().toBuilder()
                .id(uuid)
                .nextPlayer(Box.X)
                .topRight(Box.O)
                .center(Box.O)
                .bottomLeft(Box.O)
                .endBoard(true)
                .build();

        // When
        when(boardRepository.findById(any())).thenReturn(Optional.of(gameById));
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> boardServiceImpl.play(roundDTO)
        );

        // Then
        assertEquals("The game is end and the winner was: O", exception.getMessage());
    }

    @Test
    public void should_return_BoardDTO_with_isWinner_true() {

        // Given
        UUID uuid = UUID.randomUUID();
        RoundDTO roundDTO = RoundDTO.builder()
                .id(uuid.toString())
                .player("O")
                .row(0)
                .col(2)
                .build();

        Optional<Board> gameById = Optional.of(new Board().toBuilder()
                .id(uuid)
                .nextPlayer(Box.O)
                .topLeft(Box.O)
                .topCenter(Box.O)
                .build());

        // When
        when(boardRepository.findById(any())).thenReturn(gameById);
        when(boardRepository.save(any())).thenReturn(Mockito.mock(Board.class));
        BoardDTO response = boardServiceImpl.play(roundDTO);

        // Then
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals("X Player", response.getNextPlayer());
        assertTrue(response.isEndBoard());
    }

    @Test
    public void should_return_BoardDTO_with_isBoardDTOTie_true() {

        // Given
        UUID uuid = UUID.randomUUID();
        RoundDTO roundDTO = RoundDTO.builder()
                .id(uuid.toString())
                .player("O")
                .row(1)
                .col(1)
                .build();

        Optional<Board> gameById = Optional.of(new Board().toBuilder()
                .id(uuid)
                .nextPlayer(Box.O)
                .topLeft(Box.O)
                .topCenter(Box.X)
                .topRight(Box.O)
                .centerLeft(Box.O)
                .centerRight(Box.X)
                .bottomLeft(Box.X)
                .bottomCenter(Box.O)
                .bottomRight(Box.X)
                .build());

        // When
        when(boardRepository.findById(any())).thenReturn(gameById);
        when(boardRepository.save(any())).thenReturn(Mockito.mock(Board.class));
        BoardDTO response = boardServiceImpl.play(roundDTO);

        // Then
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals("X Player", response.getNextPlayer());
        assertTrue(response.isEndBoard());
    }

}