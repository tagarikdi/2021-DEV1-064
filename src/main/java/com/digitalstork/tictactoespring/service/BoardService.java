package com.digitalstork.tictactoespring.service;

import com.digitalstork.tictactoespring.dto.BoardDTO;
import com.digitalstork.tictactoespring.dto.RoundDTO;

public interface BoardService {
    BoardDTO createBoard();
    BoardDTO play(RoundDTO roundDTO);
}
