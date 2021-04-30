package com.digitalstork.tictactoespring.service;

import com.digitalstork.tictactoespring.dto.BoardDTO;
import com.digitalstork.tictactoespring.dto.RoundDTO;
import com.digitalstork.tictactoespring.mapper.BoardMapper;
import com.digitalstork.tictactoespring.model.Board;
import com.digitalstork.tictactoespring.model.enumeration.Box;
import com.digitalstork.tictactoespring.repository.BoardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Consumer;

public class BoardServiceImpl implements BoardService{


    private BoardRepository boardRepository;
    private static final Logger LOG = LoggerFactory.getLogger(BoardServiceImpl.class);
    private static final BoardMapper BOARD_MAPPER = new BoardMapper();

    private static final List<List<String>> WINNER_COMBINATIONS = Arrays.asList(
            Arrays.asList("00", "01", "02"), Arrays.asList("10", "11", "12"), Arrays.asList("20", "21", "22"),
            Arrays.asList("00", "10", "20"), Arrays.asList("01", "11", "21"), Arrays.asList("02", "12", "22"),
            Arrays.asList("00", "11", "22"), Arrays.asList("02", "11", "20")
    );


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

    /**
     * @param board Board
     * @return the name of the winner, if there are no winner then return 'No one'.
     */
    private String getWinner(Board board) {
        if (isWinnerByName(board, Box.O)) {
            return Box.O.getValue();
        }
        if (isWinnerByName(board, Box.X)) {
            return Box.X.getValue();
        }
        return "No one";
    }

    private boolean isWinnerByName(Board board, Box player) {
        Map<String, Box> boxes = Map.of(
                "00", board.getTopLeft(), "01", board.getTopCenter(), "02", board.getTopRight(),
                "10", board.getCenterLeft(), "11", board.getCenter(), "12", board.getCenterRight(),
                "20", board.getBottomLeft(), "21", board.getBottomCenter(), "22", board.getBottomRight()
        );
        return WINNER_COMBINATIONS.stream()
                .anyMatch(combination -> combination.stream().allMatch(b -> boxes.get(b) == player));
    }

    private void updateBox(Board board, String player, int row, int col) {
        Map<String, Consumer<Box>> boxes = Map.of(
                "00", board::setTopLeft, "01", board::setTopCenter, "02", board::setTopRight,
                "10", board::setCenterLeft, "11", board::setCenter, "12", board::setCenterRight,
                "20", board::setBottomLeft, "21", board::setBottomCenter, "22", board::setBottomLeft
        );

        boxes.get(String.format("%d%d", row, col)).accept(Box.valueOf(player));
    }

    /**
     * Check if a given box is Blank.
     *
     * @param board Board
     * @param col int
     * @param row int
     * @return true if the given box is Blank.
     */
    private boolean isBoxBlank(Board board, int col, int row) {
        Map<String, Box> boxs = Map.of(
                "00", board.getTopLeft(), "01", board.getTopCenter(), "02", board.getTopRight(),
                "10", board.getCenterLeft(), "11", board.getCenter(), "12", board.getCenterRight(),
                "20", board.getBottomLeft(), "21", board.getBottomCenter(), "22", board.getBottomRight()
        );

        return boxs.get(String.format("%d%d", row, col)) == Box.BLANK;
    }

    private boolean isWinner(Board board) {
        return isWinnerByName(board, Box.O) || isWinnerByName(board, Box.X);
    }

    /**
     * Check if there are an empty box.
     *
     * @param board Board
     * @return true if all boxes are full.
     */
    private boolean isGameTie(Board board) {
        List<Box> boxes = List.of(
                board.getTopLeft(), board.getTopCenter(), board.getTopRight(),
                board.getCenterLeft(), board.getCenter(), board.getCenterRight(),
                board.getBottomLeft(), board.getBottomCenter(), board.getBottomRight()
        );
        return boxes.stream().allMatch(b -> b != Box.BLANK);
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

        if (!board.getNextPlayer().getValue().equals(roundDTO.getPlayer())) {
            throw new IllegalArgumentException("The next player should be: " + board.getNextPlayer());
        }

        // Check if the game is end.
        if (board.isEndBoard()) {
            throw new IllegalArgumentException(String.format("The game is end and the winner was: %s", getWinner(board)));
        }

        // Check if the asked box is blank.
        if (!isBoxBlank(boardById.get(), roundDTO.getCol(), roundDTO.getRow())) {
            throw new IllegalArgumentException(String.format("The asked box is not Blank row = %s, col = %s ", roundDTO.getRow(), roundDTO.getCol()));
        }

        updateBox(board, roundDTO.getPlayer(), roundDTO.getRow(), roundDTO.getCol());

        Box nextPlayer = Box.O.getValue().equals(roundDTO.getPlayer()) ? Box.X : Box.O;
        board.setNextPlayer(nextPlayer);

        if (isWinner(board) || isGameTie(board)) {
            board.setEndBoard(true);
            LOG.info("The winner is: {}", getWinner(board));
        }

        this.boardRepository.save(board);

        LOG.info("Initial game: {}\n", board.drawBoard()!= null ? board.drawBoard(): "");
        return BOARD_MAPPER.apply(board);
    }


}
