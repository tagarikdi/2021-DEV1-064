package com.digitalstork.tictactoespring.repository;

import com.digitalstork.tictactoespring.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BoardRepository extends JpaRepository<Board, UUID> {
}
