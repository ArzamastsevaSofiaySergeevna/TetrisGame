package com.example.tetrisgame.presenters;

public enum GameStatus {
    START("СТАРТ"),
    PLAYING("ИГРА"),
    OVER("ТЫ УМЕР!"),
    PAUSED("ИГРА НА ПАУЗЕ");

    private final String value;

    GameStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
