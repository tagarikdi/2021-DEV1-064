package com.digitalstork.tictactoespring.service;

import com.digitalstork.tictactoespring.dto.BoardDTO;
import com.digitalstork.tictactoespring.model.Board;
import com.digitalstork.tictactoespring.model.enumeration.Box;
import com.digitalstork.tictactoespring.repository.BoardRepository;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

import static junit.framework.TestCase.*;
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

}