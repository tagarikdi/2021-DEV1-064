package com.digitalstork.tictactoespring.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BoardDTO {
    private UUID id;
    private String nextPlayer;
    private boolean endBoard;
}
