package com.digitalstork.tictactoespring.mapper;

import com.digitalstork.tictactoespring.dto.BoardDTO;
import com.digitalstork.tictactoespring.model.Board;

import java.util.function.Function;

public class BoardMapper implements Function<Board, BoardDTO> {

    public BoardDTO apply(Board board) {
        return new BoardDTO();
    }
}
