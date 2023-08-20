package com.lazychess.chessgame.controller;

public class ControllerConstants {

    private ControllerConstants() {
    }

    public static final String BASE_PATH = "/";
    public static final String BOARD_PATH = "board";
    public static final String ADD_PLAYER_TWO_TO_BOARD_PATH = "add-player-two-board/{boardGameId}";
    public static final String MAKE_A_MOVE_PATH = "make-a-move/{boardGameId}";
    public static final String PROMOTE_PAWN_PATH = "promote-pawn/{boardGameId}";
    public static final String REGISTRATION_PATH = "registration";
    public static final String LOGIN_PATH = "login";
    public static final String REFRESH_TOKEN_PATH = "refresh-token";
    public static final String LOGOUT_PATH = "logout";
}
