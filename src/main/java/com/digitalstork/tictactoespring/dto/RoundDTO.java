package com.digitalstork.tictactoespring.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString
public class RoundDTO {
    private String id;
    private String player;
    private int row;
    private int col;
}
