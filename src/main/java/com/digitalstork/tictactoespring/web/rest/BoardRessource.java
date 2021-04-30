package com.digitalstork.tictactoespring.web.rest;

import com.digitalstork.tictactoespring.dto.BoardDTO;
import com.digitalstork.tictactoespring.dto.RoundDTO;
import com.digitalstork.tictactoespring.service.BoardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tictactoe")
public class BoardRessource {

    private static final Logger LOG = LoggerFactory.getLogger(BoardRessource.class);

    private final BoardService boardService;

    public BoardRessource(BoardService boardService) {
        this.boardService = boardService;
    }
    @GetMapping("/new")
    public ResponseEntity<BoardDTO> createNewGame() {

        LOG.info("Create new Tic Tac Toe Game.");
        BoardDTO board = boardService.createBoard();
        LOG.info("Game Id: {}\n", board.getId());

        return ResponseEntity.ok(board);
    }

    @PostMapping("/play")
    public ResponseEntity<BoardDTO> playGame(@RequestBody RoundDTO round) {


        return ResponseEntity.ok(new BoardDTO());
    }
}
