package com.lazychess.chessgame.common;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import com.lazychess.chessgame.chessgame.Bishop;
import com.lazychess.chessgame.chessgame.EmptyPiece;
import com.lazychess.chessgame.chessgame.King;
import com.lazychess.chessgame.chessgame.Knight;
import com.lazychess.chessgame.chessgame.LegalMoveSquare;
import com.lazychess.chessgame.chessgame.Pawn;
import com.lazychess.chessgame.chessgame.Queen;
import com.lazychess.chessgame.chessgame.Rook;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

class PieceTest {

    @Test
    void equalsAndHashCodeKingTest() {
        EqualsVerifier
            .forClass(King.class)
            .withRedefinedSuperclass()
            .suppress(Warning.NONFINAL_FIELDS)
            .withIgnoredFields("legalMoves")
            .withPrefabValues(LegalMoveSquare.class, new LegalMoveSquare(1,2), new LegalMoveSquare(2,3))
            .verify();
    }

    @Test
    void testToStringKing() {
        ToStringVerifier.forClass(King.class)
            .withClassName(NameStyle.SIMPLE_NAME)
            .withIgnoredFields("legalMoves")
            .verify();
    }

    @Test
    void equalsAndHashCodeQueenTest() {
        EqualsVerifier
            .forClass(Queen.class)
            .withRedefinedSuperclass()
            .suppress(Warning.NONFINAL_FIELDS)
            .withIgnoredFields("legalMoves")
            .withPrefabValues(LegalMoveSquare.class, new LegalMoveSquare(1,2), new LegalMoveSquare(2,3))
            .verify();
    }

    @Test
    void testToStringQueen() {
        ToStringVerifier.forClass(Queen.class)
            .withClassName(NameStyle.SIMPLE_NAME)
            .withIgnoredFields("legalMoves")
            .verify();
    }

    @Test
    void equalsAndHashCodeBishopTest() {
        EqualsVerifier
            .forClass(Bishop.class)
            .withRedefinedSuperclass()
            .suppress(Warning.NONFINAL_FIELDS)
            .withIgnoredFields("legalMoves")
            .withPrefabValues(LegalMoveSquare.class, new LegalMoveSquare(1,2), new LegalMoveSquare(2,3))
            .verify();
    }

    @Test
    void testToStringBishop() {
        ToStringVerifier.forClass(Bishop.class)
            .withClassName(NameStyle.SIMPLE_NAME)
            .withIgnoredFields("legalMoves")
            .verify();
    }

    @Test
    void equalsAndHashCodeRookTest() {
        EqualsVerifier
            .forClass(Rook.class)
            .withRedefinedSuperclass()
            .suppress(Warning.NONFINAL_FIELDS)
            .withIgnoredFields("legalMoves")
            .withPrefabValues(LegalMoveSquare.class, new LegalMoveSquare(1,2), new LegalMoveSquare(2,3))
            .verify();
    }

    @Test
    void testToStringRook() {
        ToStringVerifier.forClass(Rook.class)
            .withClassName(NameStyle.SIMPLE_NAME)
            .withIgnoredFields("legalMoves")
            .verify();
    }

    @Test
    void equalsAndHashCodePawnTest() {
        EqualsVerifier
            .forClass(Pawn.class)
            .withRedefinedSuperclass()
            .suppress(Warning.NONFINAL_FIELDS)
            .withIgnoredFields("legalMoves", "availableEnPassantMove", "enPassantPieceToRemove")
            .withPrefabValues(LegalMoveSquare.class, new LegalMoveSquare(1,2), new LegalMoveSquare(2,3))
            .verify();
    }

    @Test
    void testToStringPawn() {
        ToStringVerifier.forClass(Pawn.class)
            .withClassName(NameStyle.SIMPLE_NAME)
            .withIgnoredFields("legalMoves", "availableEnPassantMove", "enPassantPieceToRemove")
            .verify();
    }

    @Test
    void equalsAndHashCodeKnightTest() {
        EqualsVerifier
            .forClass(Knight.class)
            .withRedefinedSuperclass()
            .suppress(Warning.NONFINAL_FIELDS)
            .withIgnoredFields("legalMoves")
            .withPrefabValues(LegalMoveSquare.class, new LegalMoveSquare(1,2), new LegalMoveSquare(2,3))
            .verify();
    }

    @Test
    void testToStringKnight() {
        ToStringVerifier.forClass(Knight.class)
            .withClassName(NameStyle.SIMPLE_NAME)
            .withIgnoredFields("legalMoves")
            .verify();
    }

    @Test
    void equalsAndHashCodeEmptyPieceTest() {
        EqualsVerifier
            .forClass(EmptyPiece.class)
            .withRedefinedSuperclass()
            .suppress(Warning.NONFINAL_FIELDS)
            .withIgnoredFields("legalMoves", "colour")
            .withPrefabValues(LegalMoveSquare.class, new LegalMoveSquare(1,2), new LegalMoveSquare(2,3))
            .verify();
    }

    @Test
    void testToStringEmptyPiece() {
        ToStringVerifier.forClass(EmptyPiece.class)
            .withClassName(NameStyle.SIMPLE_NAME)
            .withIgnoredFields("legalMoves", "colour")
            .verify();
    }
}
