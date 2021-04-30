package com.digitalstork.tictactoespring.mapper;

import com.digitalstork.tictactoespring.dto.BoardDTO;
import com.digitalstork.tictactoespring.model.Board;
import com.digitalstork.tictactoespring.model.enumeration.Box;

import java.util.function.Function;

public class BoardMapper implements Function<Board, BoardDTO> {

    public BoardDTO apply(Board board) {
        String nextPlayer = board.getNextPlayer() == Box.BLANK ? "X can start the game" : String.format("%s Player", board.getNextPlayer());
        return BoardDTO.builder()
                .id(board.getId())
                .endBoard(board.isEndBoard())
                .nextPlayer(nextPlayer)
                .build();
    }
}
