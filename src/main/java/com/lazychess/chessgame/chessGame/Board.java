package com.lazychess.chessgame.chessGame;

public class Board {

    private Square[][] squares;
    Piece 
        whiteRook1, whiteRook2,
        whiteKnight1, whiteKnight2,
        whiteBishop1, whiteBishop2,
        whiteQueen, whiteKing, whitePawn[];


    Piece
        blackRook1, blackRook2,
        blackKnight1, blackKnight2,
        blackBishop1, blackBishop2,
        blackQueen, blackKing, blackPawn[];

    public Board() {
        squares = new Square[8][8];
        this.loadSquares();
    }

    public void loadSquares() {
        boolean squareColour = true;

        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                squares[i][j] = new Square(i,j,squareColour);
                squareColour = !squareColour;
            }
        }

    }
}
