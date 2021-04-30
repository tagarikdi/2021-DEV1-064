package com.digitalstork.tictactoespring.web.rest;

import com.digitalstork.tictactoespring.dto.BoardDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tictactoe")
public class BoardRessource {


    @GetMapping("/new")
    public ResponseEntity<BoardDTO> createNewGame() {


        return ResponseEntity.ok(new BoardDTO());
    }
}
