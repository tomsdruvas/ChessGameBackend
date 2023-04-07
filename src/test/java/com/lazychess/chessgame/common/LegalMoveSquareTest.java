package com.lazychess.chessgame.common;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import com.lazychess.chessgame.chessgame.LegalMoveSquare;

import nl.jqno.equalsverifier.EqualsVerifier;

class LegalMoveSquareTest {

    @Test
    void equalsAndHashCodeTest() {
        EqualsVerifier
            .forClass(LegalMoveSquare.class)
            .withIgnoredFields("piece")
            .withPrefabValues(LegalMoveSquare.class, new LegalMoveSquare(1,2), new LegalMoveSquare(2,3))
            .verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(LegalMoveSquare.class)
            .withClassName(NameStyle.SIMPLE_NAME)
            .verify();
    }
}
