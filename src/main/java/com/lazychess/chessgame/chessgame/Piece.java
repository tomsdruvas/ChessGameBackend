package com.lazychess.chessgame.chessgame;

import static com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import static com.lazychess.chessgame.chessgame.ChessConstants.EMPTY_PIECE;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.NoArgsConstructor;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type")
@JsonSubTypes({
    @Type(value = Pawn.class, name = "pawn"),
    @Type(value = Rook.class, name = "rook"),
    @Type(value = Bishop.class, name = "bishop"),
    @Type(value = King.class, name = "king"),
    @Type(value = Queen.class, name = "queen"),
    @Type(value = Knight.class, name = "knight"),
    @Type(value = EmptyPiece.class, name = "emptypiece")
})
@NoArgsConstructor
public abstract class Piece implements Serializable {

    @Serial
    private static final long serialVersionUID = 3963038001873827698L;

    private String name;
    private String colour;
    private int row;
    private int column;

    protected List<LegalMoveSquare> legalMoves;

    protected Piece(String name, int row, int column, String colour) {
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

    public List<LegalMoveSquare> getLegalMoves() {
        return legalMoves;
    }

    public void removeLegalMove(int row, int column) {
        ArrayList<LegalMoveSquare> squareArrayList = new ArrayList<>(legalMoves);

        this.legalMoves = squareArrayList.stream()
            .filter(square -> !(square.getRow() == row && square.getColumn() == column))
            .collect(Collectors.toCollection(ArrayList::new));
    }

    public void generateLegalMoves(Square[][] squares) {
    }

    public void setLegalMoves(List<LegalMoveSquare> legalMoves) {
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

    public boolean bishopLegalMoves(Square square) {
        return Math.abs(square.getRow() - getPieceRow()) == Math.abs(square.getColumn() - getPieceColumn());
    }

    public boolean rookCanMoveToSameColumnOrRow(Square square) {
        return square.getColumn() == getPieceColumn() || square.getRow() == getPieceRow();
    }

    public boolean piecesInTheWayStraight(Square[][] squares, Square square) {
        int currentColumn = getPieceColumn();
        int currentRow = getPieceRow();

        int newColumn = square.getColumn();
        int newRow = square.getRow();

        if(currentRow == newRow){
            for(int x = Math.min(currentColumn, newColumn) + 1; x < Math.max(currentColumn, newColumn); x++){
                if(!Objects.equals(squares[currentRow][x].getPiece().getColour(), EMPTY_PIECE)){
                    return false;
                }
            }
        } else if(currentColumn == newColumn){
            for(int x = Math.min(currentRow, newRow) + 1; x < Math.max(currentRow, newRow); x++){
                if(!Objects.equals(squares[x][currentColumn].getPiece().getColour(), EMPTY_PIECE)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Serial
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    @Serial
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
    }
}
