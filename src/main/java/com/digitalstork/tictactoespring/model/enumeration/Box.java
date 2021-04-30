package com.digitalstork.tictactoespring.model.enumeration;

public enum Box {
    BLANK(" "),
    X("X"),
    O("O");

    private final String value;

    Box(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
