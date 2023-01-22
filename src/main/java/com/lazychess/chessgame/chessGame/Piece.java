package com.lazychess.chessgame.chessGame;

import static com.lazychess.chessgame.chessGame.ChessConstants.EMPTY_PIECE;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class Piece {

    String name;
    private String colour;
    private int row;
    private int column;
    public List<Square> legalMoves;

    public Piece(String name, int row, int column, String colour) {
        this.name = name;
        this.row = row;
        this.column = column;
        this.colour = colour;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public int getPieceRow() {
        return row;
    }

    public void setPieceRow(int row) {
        this.row = row;
    }

    public int getPieceColumn() {
        return column;
    }

    public void setPieceColumn(int column) {
        this.column = column;
    }

    public List<Square> getLegalMoves() {
        return legalMoves;
    }

    public void removeLegalMove(int row, int column) {
        ArrayList<Square> squareArrayList = new ArrayList<>(legalMoves);

        this.legalMoves = squareArrayList.stream()
            .filter(square -> !(square.getRow() == row && square.getColumn() == column))
            .toList();
    }

    public void generateLegalMoves(Square[][] squares) {
    }

    public void setLegalMoves(List<Square> legalMoves) {
        this.legalMoves = legalMoves;
    }

    public void clearLegalMoves() {

        this.legalMoves = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public boolean filterSquaresWithSameColourPiece(Square square) {
        return Objects.equals(square.getPiece().getColour(), EMPTY_PIECE) || !Objects.equals(square.getPiece().getColour(), getColour());
    }

    public boolean piecesInTheWayDiagonally(Square[][] squares, Square square) {
        int currentRow = getPieceRow();
        int currentColumn = getPieceColumn();

        int newRow = square.getRow();
        int newColumn = square.getColumn();

        int directionX = newRow > currentRow ? 1 : -1;
        int directionY = newColumn > currentColumn ? 1 : -1;

        for(int i = 1; i < Math.abs(newRow-currentRow); ++i) {
            if(!Objects.equals(squares[currentRow + i * directionX][currentColumn + i * directionY].getPiece().getColour(), EMPTY_PIECE)) {
                return false;
            }
        }
        return true;
    }

    public boolean piecesInTheWayStraight(Square[][] squares, Square square) {
        int currentColumn = getPieceColumn();
        int currentRow = getPieceRow();

        int newColumn = square.getColumn();
        int newRow = square.getRow();

        int direction;

        if(currentRow != newRow){
            if(currentRow < newRow){
                direction = 1;
            }else{
                direction = -1;
            }

            for(int x = currentRow + direction; x != newRow; x += direction){
                if(!Objects.equals(squares[x][currentColumn].getPiece().getColour(), EMPTY_PIECE)) {

                    return false;
                }
            }
        }

        if(currentColumn != newColumn){
            if(currentColumn < newColumn){
                direction = 1;
            }else{
                direction = -1;
            }

            for(int x = currentColumn + direction; x != newColumn; x += direction){
                if(!Objects.equals(squares[currentRow][x].getPiece().getColour(), EMPTY_PIECE)){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean bishopLegalMoves(Square square) {
        return Math.abs(square.getRow() - getPieceRow()) == Math.abs(square.getColumn() - getPieceColumn());
    }

    public boolean rookCanMoveToSameColumnOrRow(Square square) {
        return square.getColumn() == getPieceColumn() || square.getRow() == getPieceRow();
    }

//    public boolean piecesInTheWayStraight(Square[][] squares, Square square) {
//        int currentColumn = getPieceColumn();
//        int currentRow = getPieceRow();
//
//        int newColumn = square.getColumn();
//        int newRow = square.getRow();
//
//        if(currentRow == newRow){
//            for(int x = Math.min(currentColumn, newColumn) + 1; x < Math.max(currentColumn, newColumn); x++){
//                if(!Objects.equals(squares[currentRow][x].getPiece().getColour(), EMPTY_PIECE)){
//                    return false;
//                }
//            }
//        }else if(currentColumn == newColumn){
//            for(int x = Math.min(currentRow, newRow) + 1; x < Math.max(currentRow, newRow); x++){
//                if(!Objects.equals(squares[x][currentColumn].getPiece().getColour(), EMPTY_PIECE)) {
//                    return false;
//                }
//            }
//        }
//        return true;
//    }

}
