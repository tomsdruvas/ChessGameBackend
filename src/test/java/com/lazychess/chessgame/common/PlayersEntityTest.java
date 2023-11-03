package com.lazychess.chessgame.common;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import com.lazychess.chessgame.repository.entity.PlayersEntity;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

class PlayersEntityTest {

    @Test
    void equalsAndHashCodeTest() {
        EqualsVerifier
            .forClass(PlayersEntity.class)
            .suppress(Warning.NONFINAL_FIELDS)
            .verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(PlayersEntity.class)
            .withClassName(NameStyle.SIMPLE_NAME)
            .verify();
    }
}
