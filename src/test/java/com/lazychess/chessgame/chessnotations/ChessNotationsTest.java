package com.lazychess.chessgame.chessnotations;

import static testUtil.ChessMoveNotationsParser.parseChessNotions;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.lazychess.chessgame.chessgame.Board;
import com.lazychess.chessgame.chessgame.Piece;
import com.lazychess.chessgame.dto.ChessMoveDto;

import testUtil.ChessMoveNotion;
import testUtil.TypeOfMoveEnum;

@SpringBootTest
public class ChessNotationsTest {

    private Board board;

    @BeforeEach
    public void loadChess() {
        board = new Board();
    }

    // Tigran L Petrosian vs Danilo Milanovic
    //VI Ciudad de Dos Hermanas Internet (2005) (rapid), ICC INT, rd 2, Mar-26
    private String chessNotationsString = "1. e4 c5 2. Nf3 Nc6 3. Bb5 e6 4. Bxc6 bxc6 5. d3 Qc7 6. O-O e5 7. Nc3 Be7 8. Nd2 Nf6 9. Nc4 d5 10. Nd2 Bg4 11. f3 Be6 12. Kh1 O-O 13. b3 Rad8 14. Qe2 Bd6 15. Na4 Nd7 16. c4 d4 17. Ba3 a5 18. Rae1 g6 19. Qf2 Rde8 20. Re2 Qd8 21. Qg3 Qe7 22. Rfe1 Nf6 23. Qh4 Nd7 24. Qxe7 Rxe7 25. g3 f6 26. Kg2 Kg7 27. Rf2 g5 28. h3 h5 29. g4 h4 30. Rb1 Rb8 31. Rff1 Ree8 32. Kf2 Rb7 33. Ke2 Reb8 34. Kd1 Kf7 35. Kc2 Ke7 36. Rb2 Kd8 37. Rbb1 Kc7 38. Ra1 Ra8 39. Rfb1 Rab8 40. Rh1 Ra8 41. Kc1 Rab8 42. Nb1 Ra8 43. Bb2 Nb6 44. Nxb6 Rxb6 45. Kc2 Rb7 46. Nd2 Rb6 47. Rhb1 Rab8 48. Bc1 Ra8 49. Nf1 Bd7 50. Bd2 Rba6 51. a3 Kc8 52. Rb2 Kc7 53. Rab1 R8a7 54. Be1 Be8 55. Nd2 Bd7 56. Ra1 Be8 57. Rba2 Bd7 58. Nb1 Be8 59. Kb2 Bd7 60. Nd2 Be8 61. Kc2 Bd7 62. Rb2 Be8 63. Kd1 Bd7 64. Rc1 Rb7 65. Nb1 Be7 66. Kc2 Be8 67. Nd2 Bd7 68. Rcb1 Be8 69. Nf1 Rba7 70. Kc1 Bd7 71. Kd1 Be8 72. Ke2 Bd7 73. Nd2 Be8 74. Kd1 Bd7 75. Ra2 Be8 76. Kc2 Bd7 77. Kb2 Be8 78. Ka1 Bd7 79. Rbb2 Be8 80. Nb1 Bd7 81. Bd2 Be8 82. a4 Bd7 83. Na3 Rb6 84. Rb1 Rb8 85. Rc2 Kb6 86. Ka2 Rba8 87. Rcc1 Rb8 88. Nc2 Rba8 89. Ne1 Rh8 90. Rc2 Rha8 91. Ng2 Rh8 92. Bc1 Kc7 93. Ba3 Bd6 94. Rf2 Be6 95. Ne1 Bd7 96. Nc2 Be6 97. Rg1 Bd7 98. Rff1 Be6 99. Rg2 Bd7 100. Ne1 Be6 101. Kb2 Bd7 102. Ka2 Be6 103. Rff2 Bd7 104. Re2 Be6 105. f4 gxf4 106. Nf3 Raa8 107. Re1 Rag8 108. Reg1 Be7 109. Kb2 Kb6 110. Kc2 Bd6 111. Rh1 Rg6 112. Bb2 Kc7 113. Rhg1 Kb6 114. Bc1 Kc7 115. Bd2 Kb6 116. Be1 Rgh6 117. g5 fxg5 118. Rxg5 Bxh3 119. Rh1 Bc8 120. Rxh4 Rxh4 121. Bxh4 Re8 122. Rg6 Bc7 123. Bf6 Bh3 124. Kd2 Bf1 125. Rg5 Rf8 126. Bxe5 Bxe5 127. Nxe5 Rh8 128. Ke1 Rh1 129. Kf2 Bh3 130. Rh5 Kc7 131. Rh6 Rf1+ 132. Ke2 Rh1 133. Kf2 Kb7 134. Rh7+ Kb8 135. Nxc6+ Kc8 136. Ne5 Rf1+ 137. Ke2 Rh1 138. Kf2 Rf1+ 139. Ke2 Rh1 140. Rh8+ Kc7 141. Kd2 Rh2+ 142. Ke1 Rh1+ 143. Kd2 Rh2+ 144. Kc1 Rh1+ 145. Kb2 Bg2 146. Rxh1 Bxh1 147. Kc2 Kd6 148. Ng4 Bf3 149. Nf2 Ke5 150. Kd2 Kf6 151. Ke1 Kg5 152. Kf1 Kh4 153. e5 Kg5 154. Nh3+ Kf5 155. e6 Bc6 156. e7 f3 157. Kf2 Kg4 158. Ng1 Kf5 159. Nxf3 Ke6 160. Kg3 Kxe7 161. Ne5 Be8 162. Kf4 Ke6 163. Nf3 Bh5 164. Ng5+ Kd6 165. Ne4+ Kc6 166. Nd2 Bd1 167. Ke4 Bc2 168. Ke5 Bxd3 169. Kf4 Bc2 170. Kf3 Bd3 171. Kf4 Kd6 172. Kf3 Ke5 173. Kg3 Be2 174. Kf2 Bh5 175. Nf1 Ke4 176. Ng3+ Kd3 177. Nxh5 Kc3 178. Ke2 Kxb3 179. Kd3 Kxa4 180. Ng3 Kb3 181. Ne4 Kb4 182. Nd2 a4 183. Kc2 a3 184. Kc1 Kc3 185. Nb1+ Kb3 186. Nd2+ Kb4 187. Kc2 Ka4 188. Kb1 Kb4 189. Kc2 d3+ 190. Kb1 Kc3 191. Ne4+ Kxc4 192. Ka2 Kd4 193. Nd2 Kc3 194. Ne4+ Kb4 195. Kb1 c4 196. Nf2 Kb3 197. Kc1 a2 198. Kd2 a1=Q 199. Ke3 Qe1+";

    @Test
    void playGameViaChessNotions() {
        List<ChessMoveNotion> chessMoveNotions = parseChessNotions(chessNotationsString);

        chessMoveNotions.forEach(chessMoveNotion -> {
            ChessMoveDto chessMove;
            if (chessMoveNotion.getTypeOfMove() == TypeOfMoveEnum.BASIC_PAWN_MOVE) {
                chessMove = buildPawnBasicMove(chessMoveNotion);
                board.movePiece(chessMove.currentRow(), chessMove.currentColumn(), chessMove.newRow(), chessMove.newColumn());
            }
        });
    }

    private ChessMoveDto buildPawnBasicMove(ChessMoveNotion chessMoveNotion) {
        Piece piece = Arrays.stream(board.getSquares()).flatMap(Arrays::stream)
            .filter(square -> Objects.equals(square.getPiece().getColour(), chessMoveNotion.getPieceColour()) && square.getPiece().getClass() == chessMoveNotion.getPieceClass())
            .filter(square -> square.getPiece().getLegalMoves().stream().anyMatch(legalMoveSquare -> legalMoveSquare.getRow() == chessMoveNotion.getRow() && legalMoveSquare.getColumn() == chessMoveNotion.getColumn())).findFirst().orElseThrow().getPiece();

        return new ChessMoveDto(piece.getPieceRow(),
            piece.getPieceColumn(), chessMoveNotion.getRow(), chessMoveNotion.getColumn());
    }

}
