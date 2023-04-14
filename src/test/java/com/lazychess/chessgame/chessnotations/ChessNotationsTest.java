package com.lazychess.chessgame.chessnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static testUtil.ChessMoveNotationsParser.parseChessNotions;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.lazychess.chessgame.chessgame.Board;
import com.lazychess.chessgame.chessgame.ChessGameState;
import com.lazychess.chessgame.chessgame.King;
import com.lazychess.chessgame.chessgame.LegalMoveSquare;
import com.lazychess.chessgame.chessgame.Pawn;
import com.lazychess.chessgame.chessgame.Piece;
import com.lazychess.chessgame.chessgame.Square;
import com.lazychess.chessgame.dto.ChessMoveDto;

import testUtil.ChessMoveNotion;
import testUtil.TypeOfMoveEnum;

@SpringBootTest
@Disabled
class ChessNotationsTest {

    private Board board;

    @BeforeEach
    public void loadChess() {
        board = new Board();
    }

    // Tigran L Petrosian vs Danilo Milanovic
    //VI Ciudad de Dos Hermanas Internet (2005) (rapid), ICC INT, rd 2, Mar-26
    private String worldsLongestGame = "1. e4 c5 2. Nf3 Nc6 3. Bb5 e6 4. Bxc6 bxc6 5. d3 Qc7 6. O-O e5 7. Nc3 Be7 8. Nd2 Nf6 9. Nc4 d5 10. Nd2 Bg4 11. f3 Be6 12. Kh1 O-O 13. b3 Rad8 14. Qe2 Bd6 15. Na4 Nd7 16. c4 d4 17. Ba3 a5 18. Rae1 g6 19. Qf2 Rde8 20. Re2 Qd8 21. Qg3 Qe7 22. Rfe1 Nf6 23. Qh4 Nd7 24. Qxe7 Rxe7 25. g3 f6 26. Kg2 Kg7 27. Rf2 g5 28. h3 h5 29. g4 h4 30. Rb1 Rb8 31. Rff1 Ree8 32. Kf2 Rb7 33. Ke2 Reb8 34. Kd1 Kf7 35. Kc2 Ke7 36. Rb2 Kd8 37. Rbb1 Kc7 38. Ra1 Ra8 39. Rfb1 Rab8 40. Rh1 Ra8 41. Kc1 Rab8 42. Nb1 Ra8 43. Bb2 Nb6 44. Nxb6 Rxb6 45. Kc2 Rb7 46. Nd2 Rb6 47. Rhb1 Rab8 48. Bc1 Ra8 49. Nf1 Bd7 50. Bd2 Rba6 51. a3 Kc8 52. Rb2 Kc7 53. Rab1 R8a7 54. Be1 Be8 55. Nd2 Bd7 56. Ra1 Be8 57. Rba2 Bd7 58. Nb1 Be8 59. Kb2 Bd7 60. Nd2 Be8 61. Kc2 Bd7 62. Rb2 Be8 63. Kd1 Bd7 64. Rc1 Rb7 65. Nb1 Be7 66. Kc2 Be8 67. Nd2 Bd7 68. Rcb1 Be8 69. Nf1 Rba7 70. Kc1 Bd7 71. Kd1 Be8 72. Ke2 Bd7 73. Nd2 Be8 74. Kd1 Bd7 75. Ra2 Be8 76. Kc2 Bd7 77. Kb2 Be8 78. Ka1 Bd7 79. Rbb2 Be8 80. Nb1 Bd7 81. Bd2 Be8 82. a4 Bd7 83. Na3 Rb6 84. Rb1 Rb8 85. Rc2 Kb6 86. Ka2 Rba8 87. Rcc1 Rb8 88. Nc2 Rba8 89. Ne1 Rh8 90. Rc2 Rha8 91. Ng2 Rh8 92. Bc1 Kc7 93. Ba3 Bd6 94. Rf2 Be6 95. Ne1 Bd7 96. Nc2 Be6 97. Rg1 Bd7 98. Rff1 Be6 99. Rg2 Bd7 100. Ne1 Be6 101. Kb2 Bd7 102. Ka2 Be6 103. Rff2 Bd7 104. Re2 Be6 105. f4 gxf4 106. Nf3 Raa8 107. Re1 Rag8 108. Reg1 Be7 109. Kb2 Kb6 110. Kc2 Bd6 111. Rh1 Rg6 112. Bb2 Kc7 113. Rhg1 Kb6 114. Bc1 Kc7 115. Bd2 Kb6 116. Be1 Rgh6 117. g5 fxg5 118. Rxg5 Bxh3 119. Rh1 Bc8 120. Rxh4 Rxh4 121. Bxh4 Re8 122. Rg6 Bc7 123. Bf6 Bh3 124. Kd2 Bf1 125. Rg5 Rf8 126. Bxe5 Bxe5 127. Nxe5 Rh8 128. Ke1 Rh1 129. Kf2 Bh3 130. Rh5 Kc7 131. Rh6 Rf1+ 132. Ke2 Rh1 133. Kf2 Kb7 134. Rh7+ Kb8 135. Nxc6+ Kc8 136. Ne5 Rf1+ 137. Ke2 Rh1 138. Kf2 Rf1+ 139. Ke2 Rh1 140. Rh8+ Kc7 141. Kd2 Rh2+ 142. Ke1 Rh1+ 143. Kd2 Rh2+ 144. Kc1 Rh1+ 145. Kb2 Bg2 146. Rxh1 Bxh1 147. Kc2 Kd6 148. Ng4 Bf3 149. Nf2 Ke5 150. Kd2 Kf6 151. Ke1 Kg5 152. Kf1 Kh4 153. e5 Kg5 154. Nh3+ Kf5 155. e6 Bc6 156. e7 f3 157. Kf2 Kg4 158. Ng1 Kf5 159. Nxf3 Ke6 160. Kg3 Kxe7 161. Ne5 Be8 162. Kf4 Ke6 163. Nf3 Bh5 164. Ng5+ Kd6 165. Ne4+ Kc6 166. Nd2 Bd1 167. Ke4 Bc2 168. Ke5 Bxd3 169. Kf4 Bc2 170. Kf3 Bd3 171. Kf4 Kd6 172. Kf3 Ke5 173. Kg3 Be2 174. Kf2 Bh5 175. Nf1 Ke4 176. Ng3+ Kd3 177. Nxh5 Kc3 178. Ke2 Kxb3 179. Kd3 Kxa4 180. Ng3 Kb3 181. Ne4 Kb4 182. Nd2 a4 183. Kc2 a3 184. Kc1 Kc3 185. Nb1+ Kb3 186. Nd2+ Kb4 187. Kc2 Ka4 188. Kb1 Kb4 189. Kc2 d3+ 190. Kb1 Kc3 191. Ne4+ Kxc4 192. Ka2 Kd4 193. Nd2 Kc3 194. Ne4+ Kb4 195. Kb1 c4 196. Nf2 Kb3 197. Kc1 a2 198. Kd2 a1=Q 199. Ke3 Qe1+";

    private String checkMateGame = "1. Nf3 Nf6 2. c4 e6 3. g3 d5 4. Bg2 Be7 5. d4 O-O 6. Qc2 dxc4 7. Qxc4 a6 8. Qc2 c5 9. dxc5 Qa5+ 10. Bd2 Qxc5 11. Qxc5 Bxc5 12. Ne5 Nbd7 13. Nd3 Bd4 14. Bb4 Re8 15. Nd2 Ne5 16. Nxe5 Bxb2 17. Nd3 Bxa1 18. Nb3 a5 19. Bd2 e5 20. Nxa1 e4 21. Nc5 a4 22. Bg5 Ra5 23. Bxf6 Rxc5 24. Bd4 Rc4 25. e3 Bh3 26. O-O Bf5 27. h3 Rec8 28. g4 Bg6 29. a3 b5 30. Kh2 b4 31. axb4 Rxb4 32. Rd1 h5 33. Kg3 hxg4 34. hxg4 a3 35. Rd2 f6 36. Kf4 Rc1 37. Ra2 Ra4 38. Nc2 Rg1 39. Bxe4 Bxe4 40. Kxe4 Rxg4+ 41. Kf3 f5 42. Rxa3 Rxa3 43. Nxa3 Kf7 44. Nb5 g6 45. Nc3 Rg1 46. Ne2 Rb1 47. Ng3 Rg1 48. Be5 Ke6 49. Bg7 Kf7 50. Bh6 Kf6 51. Kf4 Rg2 52. f3 Rb2 53. e4 fxe4 54. Bg5+ Ke6 55. fxe4 Rf2+ 56. Ke3 Rg2 57. Kf3 Rg1 58. Be3 Rb1 59. Bf4 Rb3+ 60. Kg4 Kf6 61. Nh1 Rb2 62. Bg3 Rb4 63. Nf2 Ra4 64. Bf4 Ra3 65. Nh3 Ra1 66. Bg3 Ra3 67. Kf4 Ra1 68. Bh4+ Ke6 69. Ng5+ Kd6 70. Bg3 Rf1+ 71. Ke3+ Ke7 72. e5 Ra1 73. Bf2 Ra4 74. Kd3 Rf4 75. Be3 Rg4 76. Bc5+ Ke8 77. Ne6 Rh4 78. Nd4 g5 79. e6 Rh6 80. Ke4 g4 81. Ke5 g3 82. Nf5 Rg6 83. Nd6+ Kd8 84. Kf5 Rg8 85. Ne4 g2 86. e7+ Kc7 87. Nf6 Kc6 88. Bg1 Ra8 89. e8=Q+ Rxe8 90. Nxe8 Kd5 91. Nf6+ Kc4 92. Ke4 Kc3 93. Kf3 Kd3 94. Kxg2 Ke2 95. Ne4 Kd3 96. Nd6 Kc3 97. Kf3 Kd3 98. Be3 Kc3 99. Ke2 Kb4 100. Bb6 Kc3 101. Ke3 Kc2 102. Ba5 Kb3 103. Kd3 Ka4 104. Bc3 Kb3 105. Ne4 Ka4 106. Kc4 Ka3 107. Nc5 Ka2 108. Kd3 Kb1 109. Kd2 Ka2 110. Kc2 Ka3 111. Bd2 Ka2 112. Bc1 Ka1 113. Nd3 Ka2 114. Nb4+ Ka1 115. Bb2#";

    //Casper vs Heckert
    private String fourQueensByMoves7 = "1. e4 Nf6 2. Nc3 d5 3. e5 d4 4. exf6 dxc3 5. d4 cxb2 6. fxg7 bxa1=Q 7. gxh8=Q Qxa2 8. Nf3 Bf5 9. Ne5 Qa5+ 10. Bd2 Qad5 11. Bc4 Qe4+ 12. Be3 e6 13. O-O Qdh4 14. Bd3 Qd5 15. Bxf5 exf5 16. Re1 Nd7 17. Nf3 Qh5 18. Bg5+ Qe4 19. Qd2 f6 20. Bxf6 Qxe1+ 21. Qxe1+ Kf7 22. Ng5+ Kg6 23. Qg8+";

    private String checkMateAndPromotionInOne = "1. e4 e5 2. f4 exf4 3. Nf3 f5 4. e5 d6 5. d4 dxe5 6. Nxe5 Qh4+ 7. g3 fxg3 8. Bg2 gxh2+ 9. Kf1 Nf6 10. Nf3 Qh5 11. Rxh2 Qf7 12. Ne5 Qg8 13. Nc3 c6 14. Bg5 Nbd7 15. Qd3 Nxe5 16. dxe5 Be6 17. Re1 Ne4 18. Nxe4 Bc4 19. Nd6+ Bxd6 20. exd6+ Kd7 21. Re7+ Kc8 22. d7+ Kd8 23. Rf7+ Kc7 24. d8=Q#";

    //Alfonso Romero Holmes vs Boris Kantsler
    private String alfonsoVsBoris = "1. e4 g6 2. Nc3 Bg7 3. h4 h5 4. d4 c6 5. Bc4 d5 6. exd5 b5 7. Bd3 b4 8. Ne4 cxd5 9. Ng5 Nc6 10. N1f3 Bg4 11. c3 Qa5 12. Bd2 Nh6 13. O-O O-O 14. Re1 e6 15. Qc1 bxc3 16. bxc3 Bf5 17. Nh3 Bxd3 18. Bxh6 Rac8 19. Bxg7 Kxg7 20. Qg5 Be4 21. Nf4 Rh8 22. Re3 Qc7 23. Nd2 Bf5 24. Rae1 Ne7 25. Qg3 Bg4 26. Qh2 Nf5 27. Rd3 Nd6 28. Rde3 Bf5 29. Nxd5 exd5 30. Re7 Qc6 31. Qe5+ Kg8 32. Nf3 Ne4 33. Rxa7 Rh7 34. Qe7 Re8 35. Qa3 f6 36. Ra6 Qc8 37. Nd2 Rc7 38. Nxe4 Bxe4 39. Rxf6 Rxc3 40. Qd6 Qg4 41. Rxe4 Qxe4 42. Rxg6+ Kh7 43. Rh6+ Kg8 44. Qf6 Qe1+ 45. Kh2 Qh1+ 46. Kxh1 Re1+ 47. Kh2 Rh1+ 48. Kxh1 Rh3+ 49. Kg1 Rh1+";

    private String vikVsKat = "1. d4 Nf6 2. c4 g6 3. Nc3 d5 4. Bf4 Bg7 5. e3 c5 6. dxc5 Qa5 7. Rc1 dxc4 8. Bxc4 O-O 9. Nf3 Qxc5 10. Bb3 Nc6 11. O-O Qa5 12. h3 Bf5 13. Qe2 Ne4 14. Nd5 e5 15. Bg5 Nxg5 16. Nxg5 Qd8 17. h4 h6 18. g4 Bd7 19. Ne4 Qxh4 20. f3 Kh8 21. Rf2 f5 22. Rh2 Qd8 23. Rxh6+ Bxh6 24. Qh2 Kg7 25. Rxc6 Bf4 26. Nxf4 bxc6 27. g5 Rh8 28. Ne6+ Bxe6 29. Qxe5+ Kh7 30. Nf6+ Qxf6 31. Qxf6 Bxb3 32. Qe7+ Kg8 33. Qb7 Rf8 34. Qxb3+ Rf7 35. Qe6 Kg7 36. Qxc6 Rh5 37. f4 Rh8 38. Qc3+ Kh7 39. Qd2 Re8 40. Kf2 Re6 41. b4 a6 42. Qd5 Rfe7 43. Qc5 Rd7 44. Qf8 Rdd6 45. Qf7+ Kh8 46. Kf3 Rc6 47. a4 Rcd6 48. Ke2 Rc6 49. b5 axb5 50. axb5 Rcd6 51. Kf3 Rxe3+ 52. Kxe3 Rd3+ 53. Ke2 Rd2+ 54. Ke1 Rd1+ 55. Kf2 Rd2+ 56. Kg3 Rd3+ 57. Kh4 Rh3+ 58. Kxh3";

    private String levMilmanVsJosephFang = "1. e4 c6 2. d4 d5 3. Nc3 dxe4 4. Nxe4 Bf5 5. Ng3 Bg6 6. h4 h6 7. Nf3 Nd7 8. h5 Bh7 9. Bd3 Bxd3 10. Qxd3 e6 11. Bf4 Bb4+ 12. c3 Be7 13. O-O-O Ngf6 14. Kb1 O-O 15. Ne5 c5 16. Qf3 Qb6 17. Nxd7 Nxd7 18. d5 exd5 19. Nf5 Bf6 20. Rxd5 Qe6 21. Bxh6 Ne5 22. Qe4 Nc6 23. Qf3 Ne5 24. Qe4 Nc6 25. Qg4 Qxd5 26. Bxg7 Qd3+ 27. Ka1 Ne5 28. Ne7+ Kh7 29. Qg6+ fxg6 30. hxg6+ Kxg7 31. Rh7#";

    private String irinaKorepanovaVsAlexanderTishkov = "1. b4 e5 2. Bb2 Bxb4 3. Bxe5 Nf6 4. a3 Be7 5. e3 O-O 6. Nf3 Nc6 7. Bb2 a6 8. d4 d5 9. c4 Be6 10. Nbd2 h6 11. Rc1 Na5 12. Ne5 c6 13. c5 Ne4 14. Nxe4 dxe4 15. Rc3 b5 16. Qc2 Bd5 17. Be2 Qe8 18. Bh5 g6 19. Bg4 Kg7 20. Nd7 f5 21. Nxf8 fxg4 22. Nxg6 Qxg6 23. g3 Nb7 24. h3 Rf8 25. h4 h5 26. Rh2 a5 27. a4 b4 28. Rb3 Qe6 29. Qd2 Kg6 30. Rxb4 axb4 31. Qxb4 Qc8 32. Ke2 Bd8 33. Rh1 Ba5 34. Qa3 Qf5 35. Rh2 Qf3+ 36. Kf1 Qd1+ 37. Kg2 Qe1 38. f4 exf3#";

    private String frederickRhinevsNN = "1.d4 d5 2.c4 dxc4 3.e4 c6 4.Bxc4 Nf6 5.Nc3 e6 6.Nf3 Be7 7.O-O O-O 8.Re1 b5 9.Bd3 Nbd7 10.e5 Nd5 11.Bc2 b4 12.Qd3 f5 13.exf6 N7xf6 14.Na4 a5 15.Ng5 Ba6 16.Qh3 h6 17.Qxe6+ Kh8 18.Nf7+ Rxf7 19.Qxf7 Qd6 20.Nc5 Rf8 21.Qe6 Bc8 22.Qxd6 Bxd6 23.Ne6 Bxe6 24.Rxe6 Bf4 25.Rxc6 Bxc1 26.Rxc1 Nf4 27.Re1 Rd8 28.g3 Nh3+ 29.Kg2 Ng5 30.Bg6 Rxd4 31.Rc8+ Ng8 32.h4 Nh7 33.Bf7 Nf6 34.Bxg8 Nxg8 35.Ree8 Rd2 36.Rxg8+ Kh7 37.h5 g5 38.hxg6#";

    private String robertSRobinsonVsFrederickRhine = "1.e4 c5 2.c4 Nc6 3.Nc3 g6 4.d4 cxd4 5.Nce2 Bg7 6.b3 e5 7.Bb2 Nf6 8.Nf3 Nxe4 9.Nxe5 Bxe5 10.Nxd4 Qa5+ 11.b4 Qxb4+ 12.Ke2 Qxb2+ 13.Kf3 Qxf2+ 14.Kxe4 d5+ 15.Kxd5 Be6+ 16.Kc5 Bxd4+ 17.Kd6 O-O-O#";

    private String ivanNikolicVsGoranArsovic = "1.d4 Nf6 2.c4 g6 3.Nc3 Bg7 4.e4 d6 5.Nf3 O-O 6.Be2 Nbd7 7.O-O e5 8.Re1 Re8 9.Bf1 h6 10.d5 Nh7 11.Rb1 f5 12.Nd2 f4 13.b4 g5 14.Nb3 Bf8 15.Be2 Ndf6 16.c5 g4 17.cxd6 cxd6 18.a3 Ng5 19.Bf1 Re7 20.Qd3 Rg7 21.Kh1 Qe8 22.Nd2 g3 23.fxg3 fxg3 24.Qxg3 Nh3 25.Qf3 Qg6 26.Nc4 Bd7 27.Bd3 Ng5 28.Bxg5 Qxg5 29.Ne3 Re8 30.Ne2 Be7 31.Rbd1 Rf8 32.Nf5 Ng4 33.Neg3 h5 34.Kg1 h4 35.Qxg4 Qxg4 36.Nh6+ Kh7 37.Nxg4 hxg3 38.Ne3 gxh2+ 39.Kxh2 Rh8 40.Rh1 Kg6+ 41.Kg1 Rc8 42.Be2 Rc3 43.Rd3 Rc1+ 44.Nf1 Bd8 45.Rh8 Bb6+ 46.Kh2 Rh7+ 47.Rxh7 Kxh7 48.Nd2 Bg1+ 49.Kh1 Bd4+ 50.Nf1 Bg4 51.Bxg4 Rxf1+ 52.Kh2 Bg1+ 53.Kh3 Re1 54.Bf5+ Kh6 55.Kg4 Re3 56.Rd1 Bh2 57.Rh1 Rg3+ 58.Kh4 Rxg2 59.Kh3 Rg3+ 60.Kxh2 Rxa3 61.Rg1 Ra6 62.Rg6+ Kh5 63.Kg3 Rb6 64.Rg7 Rxb4 65.Bc8 a5 66.Bxb7 a4 67.Bc6 a3 68.Ra7 Rb3+ 69.Kf2 Kg5 70.Ke2 Kf4 71.Ra4 Rh3 72.Kd2 a2 73.Bb5 Rh1 74.Rxa2 Rh2+ 75.Be2 Kxe4 76.Ra5 Kd4 77.Ke1 Rh1+ 78.Kf2 Rc1 79.Bg4 Rc2+ 80.Ke1 e4 81.Be6 Ke5 82.Bg8 Rc8 83.Bf7 Rc7 84.Be6 Rc2 85.Ra8 Rb2 86.Ra6 Rg2 87.Kd1 Rb2 88.Ra5 Rg2 89.Bd7 Rh2 90.Bc6 Kf4 91.Ra8 e3 92.Re8 Kf3 93.Rf8+ Ke4 94.Rf6 Kd3 95.Bb5+ Kd4 96.Rf5 Rh1+ 97.Ke2 Rh2+ 98.Kd1 Rh1+ 99.Kc2 Rh2+ 100.Kc1 Rh1+ 101.Kc2 Rh2+ 102.Kd1 Rh1+ 103.Ke2 Rh2+ 104.Kf1 Rb2 105.Be2 Ke4 106.Rh5 Rb1+ 107.Kg2 Rb2 108.Rh4+ Kxd5 109.Kf3 Kc5 110.Kxe3 Rb3+ 111.Bd3 d5 112.Rh8 Ra3 113.Re8 Kd6 114.Kd4 Ra4+ 115.Kc3 Ra3+ 116.Kd4 Ra4+ 117.Ke3 Ra3 118.Rh8 Ke5 119.Rh5+ Kd6 120.Rg5 Rb3 121.Kd2 Rb8 122.Bf1 Re8 123.Kd3 Re5 124.Rg8 Rh5 125.Bg2 Kc5 126.Rf8 Rh6 127.Bf3 Rd6 128.Re8 Rc6 129.Ra8 Rb6 130.Rd8 Rd6 131.Rf8 Ra6 132.Rf5 Rd6 133.Kc3 Rd8 134.Rg5 Rd6 135.Rh5 Rd8 136.Rf5 Rd6 137.Rf8 Ra6 138.Re8 Rc6 139.Ra8 Rb6 140.Ra5+ Rb5 141.Ra1 Rb8 142.Rd1 Rd8 143.Rd2 Rd7 144.Bg2 Rd8 145.Kd3 Ra8 146.Ke3 Re8+ 147.Kd3 Ra8 148.Kc3 Rd8 149.Bf3 Rd7 150.Kd3 Ra7 151.Bg2 Ra8 152.Rc2+ Kd6 153.Rc3 Ra2 154.Bf3 Ra8 155.Rb3 Ra5 156.Ke3 Ke5 157.Rd3 Rb5 158.Kd2 Rc5 159.Bg2 Ra5 160.Bf3 Rc5 161.Bd1 Rc8 162.Bb3 Rc5 163.Rh3 Kf4 164.Kd3 Ke5 165.Rh5+ Kf4 166.Kd4 Rb5 167.Bxd5 Rb4+ 168.Bc4 Ra4 169.Rh7 Kg5 170.Rf7 Kg6 171.Rf1 Kg5 172.Kc5 Ra5+ 173.Kc6 Ra4 174.Bd5 Rf4 175.Re1 Rf6+ 176.Kc5 Rf5 177.Kd4 Kf6 178.Re6+ Kg5 179.Be4 Rf6 180.Re8 Kf4 181.Rh8 Rd6+ 182.Bd5 Rf6 183.Rh1 Kf5 184.Be4+ Ke6 185.Ra1 Kd6 186.Ra5 Re6 187.Bf5 Re1 188.Ra6+ Ke7 189.Be4 Rc1 190.Ke5 Rc5+ 191.Bd5 Rc7 192.Rg6 Rd7 193.Rh6 Kd8 194.Be6 Rd2 195.Rh7 Ke8 196.Kf6 Kd8 197.Ke5 Rd1 198.Bd5 Ke8 199.Kd6 Kf8 200.Rf7+ Ke8 201.Rg7 Rf1 202.Rg8+ Rf8 203.Rg7 Rf6+ 204.Be6 Rf2 205.Bd5 Rf6+ 206.Ke5 Rf1 207.Kd6 Rf6+ 208.Be6 Rf2 209.Ra7 Kf8 210.Rc7 Rd2+ 211.Ke5 Ke8 212.Kf6 Rf2+ 213.Bf5 Rd2 214.Rc1 Rd6+ 215.Be6 Rd2 216.Rh1 Kd8 217.Rh7 Rd1 218.Rg7 Rd2 219.Rg8+ Kc7 220.Rc8+ Kb6 221.Ke5 Kb7 222.Rc3 Kb6 223.Bd5 Rh2 224.Kd6 Rh6+ 225.Be6 Rh5 226.Ra3 Ra5 227.Rg3 Rh5 228.Rg2 Ka5 229.Rg3 Kb6 230.Rg4 Rb5 231.Bd5 Rc5 232.Rg8 Rc2 233.Rb8+ Ka5 234.Bb3 Rc3 235.Kd5 Rc7 236.Kd4 Rd7+ 237.Bd5 Re7 238.Rb2 Re8 239.Rb7 Ka6 240.Rb1 Ka5 241.Bc4 Rd8+ 242.Kc3 Rh8 243.Rb5+ Ka4 244.Rb6 Rh3+ 245.Bd3 Rh5 246.Re6 Rg5 247.Rh6 Rc5+ 248.Bc4 Rg5 249.Ra6+ Ra5 250.Rh6 Rg5 251.Rh4 Ka5 252.Rh2 Rg3+ 253.Kd4 Rg5 254.Bd5 Ka4 255.Kc5 Rg3 256.Ra2+ Ra3 257.Rb2 Rg3 258.Rh2 Rc3+ 259.Bc4 Rg3 260.Rb2 Rg5+ 261.Bd5 Rg3 262.Rh2 Rc3+ 263.Bc4 Rg3 264.Rh8 Ka3 265.Ra8+ Kb2 266.Ra2+ Kb1 267.Rf2 Kc1 268.Kd4 Kd1 269.Bd3 Rg7";

    private String staleMateHenryBirdvsBertholdEnglisch = "1. e4 e5 2. Nf3 Nc6 3. Bc4 Bc5 4. c3 Nf6 5. b4 Bb6 6. d3 d6 7. O-O O-O 8. Bg5 Be6 9. Nbd2 Qe7 10. a4 a6 11. a5 Ba7 12. Kh1 h6 13. Bh4 Rad8 14. b5 Bxc4 15. Nxc4 axb5 16. Ne3 Bxe3 17. fxe3 Qe6 18. Qb1 g5 19. Bg3 Na7 20. c4 c6 21. c5 Nh5 22. a6 bxa6 23. Rxa6 Qd7 24. d4 Nxg3+ 25. hxg3 Nc8 26. cxd6 f6 27. Rc1 Nxd6 28. Rcxc6 Ne8 29. Qxb5 g4 30. Nh4 exd4 31. exd4 Qxd4 32. Nf5 Qxe4 33. Re6 Rd1+ 34. Kh2 Qb1 35. Qxb1 Rxb1 36. Ra7 Rb5 37. Nxh6+ Kh8 38. Nxg4 Rg5 39. Rxe8 Rh5+ 40. Kg1 Rxe8 41. Nxf6 Rh1+ 42. Kxh1 Re1+ 43. Kh2 Rh1+ 44. Kxh1";

    private String stalemateJensHohmeistervsTenaFrank = "1.d4 e5 2.Qd2 e4 3.Qf4 f5 4.h3 Bb4+ 5.Nd2 d6 6.Qh2 Be6 7.a4 Qh4 8.Ra3 c5 9.Rg3 f4 10.f3 Bb3 11.d5 Ba5 12.c4 e3";

    private String staleMateJohanUpmarkVsRobinJohansson = "1.c4 h5 2.h4 a5 3.Qa4 Ra6 4.Qxa5 Rah6 5.Qxc7 f6 6.Qxd7+ Kf7 7.Qxb7 Qd3 8.Qxb8 Qh7 9.Qxc8 Kg6 10.Qe6";


    @Test
    void playWorldsLongestGame() {
        playGameViaChessNotions(worldsLongestGame);
    }

    @Test
    void shouldEndInCheckMate() {
        playGameViaChessNotions(checkMateGame);
    }

    @Test
    void fourQueensByMove7() {
        playGameViaChessNotions(fourQueensByMoves7);
    }

    @Test
    void checkMateAndPromotionInOne() {
        playGameViaChessNotions(checkMateAndPromotionInOne);
    }

    @Test
    void alfonsoVsBoris() {
        playGameViaChessNotions(alfonsoVsBoris);
    }

    @Test
    void vikVsKat() {
        playGameViaChessNotions(vikVsKat);
    }

    @Test
    void levMilmanvsJosephFang() {
        playGameViaChessNotions(levMilmanVsJosephFang);
    }

    @Test
    void irinaKorepanovaVsAlexanderTishkov() {
        playGameViaChessNotions(irinaKorepanovaVsAlexanderTishkov);
    }

    @Test
    void frederickRhinevsNN() {
        playGameViaChessNotions(frederickRhinevsNN);
    }

    @Test
    void robertSRobinsonVsFrederickRhine() {
        playGameViaChessNotions(robertSRobinsonVsFrederickRhine);
    }

    @Test
    void ivanNikolicVsGoranArsovic() {
        playGameViaChessNotions(ivanNikolicVsGoranArsovic);
    }

    @Test
    void staleMateHenryBirdVsBertholdEnglisch() {
        playGameViaChessNotions(staleMateHenryBirdvsBertholdEnglisch);
        assertThat(board.getStateOfTheGame()).isSameAs(ChessGameState.STALEMATE);
    }

    @Test
    void stalemateJensHohmeisterVsTenaFrank() {
        playGameViaChessNotions(stalemateJensHohmeistervsTenaFrank);
        assertThat(board.getStateOfTheGame()).isSameAs(ChessGameState.STALEMATE);
    }

    @Test
    void staleMateJohanUpmarkVsRobinJohansson() {
        playGameViaChessNotions(staleMateJohanUpmarkVsRobinJohansson);
        assertThat(board.getStateOfTheGame()).isSameAs(ChessGameState.STALEMATE);
    }

    void playGameViaChessNotions(String chessNotationsString) {
        List<ChessMoveNotion> chessMoveNotions = parseChessNotions(chessNotationsString);

        chessMoveNotions.forEach(chessMoveNotion -> {
            ChessMoveDto chessMove;
            int numberOfPiecesOnTheBoard = board.getAllPieces().size();
            int numberOfPawnsCurrentPlayer = getAllPawns(chessMoveNotion).size();
            if (chessMoveNotion.getTypeOfMove() == TypeOfMoveEnum.BASIC_PAWN_MOVE || chessMoveNotion.getTypeOfMove() == TypeOfMoveEnum.CASTLING || chessMoveNotion.getTypeOfMove() == TypeOfMoveEnum.PAWN_PROMOTION) {
                chessMove = buildPawnBasicMove(chessMoveNotion);
            } else if (chessMoveNotion.getTypeOfMove() == TypeOfMoveEnum.BASIC_WITH_PIECE_NAME) {
                chessMove = buildBasicMoveWithPieceName(chessMoveNotion);
            } else if (chessMoveNotion.getTypeOfMove() == TypeOfMoveEnum.PIECE_TO_MOVE_WITH_COLUMN ||chessMoveNotion.getTypeOfMove() == TypeOfMoveEnum.PAWN_PROMOTION_WITH_COLUMN) {
                chessMove = buildMoveWithColumnAvailable(chessMoveNotion);
            } else if (chessMoveNotion.getTypeOfMove() == TypeOfMoveEnum.PIECE_TO_MOVE_WITH_ROW) {
                chessMove = buildMoveWithRowAvailable(chessMoveNotion);
            } else {
                throw new RuntimeException(chessMoveNotion.getTypeOfMove().toString() + " not part of any logic");
            }

            moveChessPiece(chessMove);
            pawnPromotionActionIfRequired(chessMoveNotion);
            assertMovesWasSuccessful(chessMoveNotion, numberOfPiecesOnTheBoard, numberOfPawnsCurrentPlayer);
        });
    }

    private void pawnPromotionActionIfRequired(ChessMoveNotion chessMoveNotion) {
        if (chessMoveNotion.getTypeOfMove() == TypeOfMoveEnum.PAWN_PROMOTION || chessMoveNotion.getTypeOfMove() == TypeOfMoveEnum.PAWN_PROMOTION_WITH_COLUMN) {
            board.promoteAPawn(chessMoveNotion.getPawnPromotionPieceType().getSimpleName());
        }
    }

    private ChessMoveDto buildMoveWithRowAvailable(ChessMoveNotion chessMoveNotion) {
        System.out.println(chessMoveNotion);
        List<Square> squares = Arrays.stream(board.getSquares()).flatMap(Arrays::stream)
            .filter(square -> Objects.equals(square.getPiece().getColour(), chessMoveNotion.getPieceColour()) && square.getPiece().getClass() == chessMoveNotion.getPieceClass() && square.getPiece().getPieceRow() == chessMoveNotion.getPieceLocationRow())
            .filter(square -> square.getPiece().getLegalMoves().stream().anyMatch(legalMoveSquare -> legalMoveSquare.getRow() == chessMoveNotion.getRow() && legalMoveSquare.getColumn() == chessMoveNotion.getColumn())).toList();

        Piece piece = getAndAssertOnePiece(squares);

        return new ChessMoveDto(piece.getPieceRow(),
            piece.getPieceColumn(), chessMoveNotion.getRow(), chessMoveNotion.getColumn());
    }

    private ChessMoveDto buildMoveWithColumnAvailable(ChessMoveNotion chessMoveNotion) {
        System.out.println(chessMoveNotion);
        List<Square> squares = Arrays.stream(board.getSquares()).flatMap(Arrays::stream)
            .filter(square -> Objects.equals(square.getPiece().getColour(), chessMoveNotion.getPieceColour()) && square.getPiece().getClass() == chessMoveNotion.getPieceClass() && square.getPiece().getPieceColumn() == chessMoveNotion.getPieceLocationColumn())
            .filter(square -> square.getPiece().getLegalMoves().stream().anyMatch(legalMoveSquare -> legalMoveSquare.getRow() == chessMoveNotion.getRow() && legalMoveSquare.getColumn() == chessMoveNotion.getColumn())).toList();

        Piece piece = getAndAssertOnePiece(squares);

        return new ChessMoveDto(piece.getPieceRow(),
            piece.getPieceColumn(), chessMoveNotion.getRow(), chessMoveNotion.getColumn());
    }

    private ChessMoveDto buildBasicMoveWithPieceName(ChessMoveNotion chessMoveNotion) {
        System.out.println(chessMoveNotion);
        List<Square> squares = Arrays.stream(board.getSquares()).flatMap(Arrays::stream)
            .filter(square -> Objects.equals(square.getPiece().getColour(), chessMoveNotion.getPieceColour()) && square.getPiece().getClass() == chessMoveNotion.getPieceClass())
            .filter(square -> square.getPiece().getLegalMoves().stream().anyMatch(legalMoveSquare -> legalMoveSquare.getRow() == chessMoveNotion.getRow() && legalMoveSquare.getColumn() == chessMoveNotion.getColumn())).toList();

        Piece piece = getAndAssertOnePiece(squares);

        return new ChessMoveDto(piece.getPieceRow(),
            piece.getPieceColumn(), chessMoveNotion.getRow(), chessMoveNotion.getColumn());
    }

    private ChessMoveDto buildPawnBasicMove(ChessMoveNotion chessMoveNotion) {
        System.out.println(chessMoveNotion);
        List<Square> squares = Arrays.stream(board.getSquares()).flatMap(Arrays::stream)
            .filter(square -> Objects.equals(square.getPiece().getColour(), chessMoveNotion.getPieceColour()) && square.getPiece().getClass() == chessMoveNotion.getPieceClass())
            .filter(square -> square.getPiece().getLegalMoves().stream().anyMatch(legalMoveSquare -> legalMoveSquare.getRow() == chessMoveNotion.getRow() && legalMoveSquare.getColumn() == chessMoveNotion.getColumn())).toList();

        Piece piece = getAndAssertOnePiece(squares);

        return new ChessMoveDto(piece.getPieceRow(),
            piece.getPieceColumn(), chessMoveNotion.getRow(), chessMoveNotion.getColumn());
    }

    private void assertMovesWasSuccessful(ChessMoveNotion chessMoveNotion, int numberOfPiecesOnTheBoard, int numberOfPawnsCurrentPlayer) {
        if (chessMoveNotion.isShouldTakePiece()) {
            assertThat(board.getAllPieces()).hasSize(numberOfPiecesOnTheBoard - 1);
        } else {
            assertThat(board.getAllPieces()).hasSize(numberOfPiecesOnTheBoard);
        }

        if (chessMoveNotion.isShouldEndInCheck()) {
            List<LegalMoveSquare> squaresWhereKingIsInCheck = Arrays.stream(board.getSquares())
                .flatMap(Arrays::stream)
                .filter(square -> square.getPiece().getColour().equals(chessMoveNotion.getPieceColour()))
                .filter(square -> square.getPiece().getLegalMoves() != null)
                .flatMap(square -> square.getPiece().getLegalMoves().stream())
                .filter(square -> square.getPiece() instanceof King)
                .toList();

            assertThat(squaresWhereKingIsInCheck).isNotEmpty();
        }

        if (chessMoveNotion.isPawnPromotion()) {
            assertThat(getAllPawns(chessMoveNotion))
                .hasSizeLessThan(numberOfPawnsCurrentPlayer);
        }

        if (chessMoveNotion.isShouldEndInCheckMate()) {
            assertThat(board.getStateOfTheGame()).isEqualTo(ChessGameState.CHECKMATE);
        }

    }

    private List<Piece> getAllPawns(ChessMoveNotion chessMoveNotion) {
        return Arrays.stream(board.getSquares()).flatMap(Arrays::stream)
            .map(Square::getPiece)
            .filter(piece -> Objects.equals(piece.getColour(), chessMoveNotion.getPieceColour()))
            .filter(piece -> piece instanceof Pawn)
            .toList();
    }

    private void moveChessPiece(ChessMoveDto chessMove) {
        board.movePiece(chessMove.currentRow(), chessMove.currentColumn(), chessMove.newRow(), chessMove.newColumn());
    }

    private static Piece getAndAssertOnePiece(List<Square> squares) {
        assertThat(squares).hasSize(1);
        return squares.get(0).getPiece();
    }
}
