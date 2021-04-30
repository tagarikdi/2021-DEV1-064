package com.digitalstork.tictactoespring.service;

import com.digitalstork.tictactoespring.dto.BoardDTO;
import com.digitalstork.tictactoespring.dto.RoundDTO;
import com.digitalstork.tictactoespring.mapper.BoardMapper;
import com.digitalstork.tictactoespring.model.Board;
import com.digitalstork.tictactoespring.repository.BoardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoardServiceImpl implements BoardService{


    private BoardRepository boardRepository;
    private static final Logger LOG = LoggerFactory.getLogger(BoardServiceImpl.class);
    private static final BoardMapper BOARD_MAPPER = new BoardMapper();


    @Override
    public BoardDTO createBoard() {
        var board = boardRepository.save(new Board());
        LOG.info("Initial game: {}\n", board.drawBoard()!= null ? board.drawBoard(): "");
        return BOARD_MAPPER.apply(board);
    }

    @Override
    public BoardDTO play(RoundDTO roundDTO) {
        return null;
    }
}
