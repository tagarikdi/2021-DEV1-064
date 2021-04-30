package com.digitalstork.tictactoespring.model;

import com.digitalstork.tictactoespring.model.enumeration.Box;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private UUID id;
    private Box topLeft = Box.BLANK;
    private Box topCenter = Box.BLANK;
    private Box topRight = Box.BLANK;
    private Box centerLeft = Box.BLANK;
    private Box center = Box.BLANK;
    private Box centerRight = Box.BLANK;
    private Box bottomLeft = Box.BLANK;
    private Box bottomCenter = Box.BLANK;
    private Box bottomRight = Box.BLANK;
    private Box nextPlayer = Box.BLANK;
    private boolean endBoard;

    public String drawBoard() {
        var lineSeparatorLabel = "line.separator";
        var lineSeparatorLine = "+---+---+---+";
        var placeholder = "| %s | %s | %s |";

        String drawBoard;
        drawBoard = System.getProperty(lineSeparatorLabel) +
                lineSeparatorLine +
                System.getProperty(lineSeparatorLabel) +
                String.format(placeholder, topLeft.getValue(), topCenter.getValue(), topRight.getValue()) +
                System.getProperty(lineSeparatorLabel) +
                lineSeparatorLine +
                System.getProperty(lineSeparatorLabel) +
                String.format(placeholder, centerLeft.getValue(), center.getValue(), centerRight.getValue()) +
                System.getProperty(lineSeparatorLabel) +
                lineSeparatorLine +
                System.getProperty(lineSeparatorLabel) +
                String.format(placeholder, bottomLeft.getValue(), bottomCenter.getValue(), bottomRight.getValue()) +
                System.getProperty(lineSeparatorLabel) +
                lineSeparatorLine;
        return drawBoard;

    }
}
