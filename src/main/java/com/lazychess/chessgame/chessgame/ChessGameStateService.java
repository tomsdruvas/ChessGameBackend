package com.lazychess.chessgame.chessgame;

import org.springframework.stereotype.Service;

@Service
public class ChessGameStateService {

    public ChessGameStateService() {
    }

    public static boolean chessGameStateEvaluator(Board board) {
        return true;
    }
}
