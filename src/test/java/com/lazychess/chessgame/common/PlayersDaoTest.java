package com.lazychess.chessgame.common;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import com.lazychess.chessgame.repository.entity.PlayersDao;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

class PlayersDaoTest {

    @Test
    void equalsAndHashCodeTest() {
        EqualsVerifier
            .forClass(PlayersDao.class)
            .suppress(Warning.NONFINAL_FIELDS)
            .verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(PlayersDao.class)
            .withClassName(NameStyle.SIMPLE_NAME)
            .verify();
    }
}
