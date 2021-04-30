package com.digitalstork.tictactoespring.service;

import com.digitalstork.tictactoespring.dto.BoardDTO;
import com.digitalstork.tictactoespring.dto.RoundDTO;
import com.digitalstork.tictactoespring.mapper.BoardMapper;
import com.digitalstork.tictactoespring.model.Board;
import com.digitalstork.tictactoespring.model.enumeration.Box;
import com.digitalstork.tictactoespring.repository.BoardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

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

    private boolean isBoardEmpty(Board board) {
        List<Box> boxes = List.of(
                board.getTopLeft(), board.getTopCenter(), board.getTopRight(),
                board.getCenterLeft(), board.getCenter(), board.getCenterRight(),
                board.getBottomLeft(), board.getBottomCenter(), board.getBottomRight()
        );
        return boxes.stream().allMatch(b -> b == Box.BLANK);
    }

    private void updateBox(Board board, String player, int row, int col) {
        Map<String, Consumer<Box>> boxes = Map.of(
                "00", board::setTopLeft, "01", board::setTopCenter, "02", board::setTopRight,
                "10", board::setCenterLeft, "11", board::setCenter, "12", board::setCenterRight,
                "20", board::setBottomLeft, "21", board::setBottomCenter, "22", board::setBottomLeft
        );

        boxes.get(String.format("%d%d", row, col)).accept(Box.valueOf(player));
    }

    @Override
    public BoardDTO play(RoundDTO roundDTO) {
        Optional<Board> boardById = boardRepository.findById(UUID.fromString(roundDTO.getId()));
        if(boardById.isEmpty())
            throw new IllegalArgumentException("missing parameter: ");

        var board = boardById.get();

        if (!roundDTO.getPlayer().equals("X") && isBoardEmpty(board)) {
            throw new IllegalArgumentException("The first player should be: " + Box.X);
        }

        updateBox(board, roundDTO.getPlayer(), roundDTO.getRow(), roundDTO.getCol());

        Box nextPlayer = Box.O.getValue().equals(roundDTO.getPlayer()) ? Box.X : Box.O;
        board.setNextPlayer(nextPlayer);

        this.boardRepository.save(board);

        LOG.info("Initial game: {}\n", board.drawBoard()!= null ? board.drawBoard(): "");
        return BOARD_MAPPER.apply(board);
    }


}
