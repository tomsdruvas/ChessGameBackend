package com.lazychess.chessgame;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ChessMoveNotationsParserTest {


    @Test
    void parseChessNotions() {
        String chessNotationsString = "1.e4c52.c4Nc63.Nc3g64.d4cxd45.Nce2Bg76.b3e57.Bb2Nf68.Nf3Nxe49.Nxe5Bxe510.Nxd4Qa5+11.b4Qxb4+12.Ke2Qxb2+13.Kf3Qxf2+14.Kxe4d5+15.Kxd5Be6+16.Kc5Bxd4+17.Kd6O-O-O";
        String substring = chessNotationsString.substring(2);
        String[] splitStringList = substring.split("\\.");

        for (int i = 0; i < splitStringList.length; i++) {

            String suffix = String.valueOf(i + 2);
            boolean b = splitStringList[i].endsWith(suffix);

            if(b) {
                for(int j = 0; j < suffix.length(); j++) {
                    splitStringList[i] = StringUtils.chop(splitStringList[i]);
                }
            }
        }
    }

}
