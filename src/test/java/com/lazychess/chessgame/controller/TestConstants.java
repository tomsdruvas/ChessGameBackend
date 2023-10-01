package com.lazychess.chessgame.controller;

public class TestConstants {

    private TestConstants() {
    }

    public static final String BOARD_PATH = "/board";
    public static final String ADD_PLAYER_TWO_TO_BOARD_PATH = "/add-player-two-board/";
    public static final String MAKE_A_MOVE_PATH = "/make-a-move/";
    public static final String PROMOTE_PAWN_PATH = "/promote-pawn/";
    public static final String REGISTRATION_PATH = "/registration";
    public static final String LOGIN_PATH = "/login";
    public static final String REFRESH_TOKEN_PATH = "/refresh-token";
    public static final String LOGOUT_PATH = "/logout";
    public static final String WEB_SOCKET_BASE_WITH_ID_PATH = "/topic/game-progress/%s";
}
