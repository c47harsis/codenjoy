package com.codenjoy.dojo.chess.engine.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import org.junit.Ignore;
import org.junit.Test;

import static com.codenjoy.dojo.chess.engine.model.Color.*;
import static com.codenjoy.dojo.chess.engine.model.Color.BLACK;
import static com.codenjoy.dojo.chess.engine.model.Color.WHITE;
import static com.codenjoy.dojo.chess.engine.service.Move.from;
import static com.codenjoy.dojo.chess.engine.model.Event.WRONG_MOVE;

@Ignore
public class Castling4x4Test extends AbstractGameTest {

    /**************************
     *  CASTLING NOT ALLOWED  *
     **************************/

    @Test
    public void shouldNotBeAbleToMakeCastling_ifKingAlreadyMoved() {

        givenFl(".w.....i" +
                "........" +
                "........" +
                ".......y" +
                "Y......." +
                "........" +
                "........" +
                "I...W..R");
        move(WHITE, from(4, 0).to(4, 1));
        move(BLACK, from(1, 7).to(1, 6));
        move(WHITE, from(4, 1).to(4, 0)); // King returns to it's default place
        move(BLACK, from(1, 6).to(1, 7));

        // when try to make castling
        move(WHITE, from(4, 0).to(0, 0));

        // then
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToMakeCastling_ifCastlingAlreadyDoneOnce() {

        givenFl(".w......" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "R...W..R");
        move(WHITE, from(4, 0).to(0, 0)); // make a castling
        assertE(".w......" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "..WR...R");
        move(BLACK, from(1, 7).to(1, 6));
        move(WHITE, from(2, 0).to(2, 1));
        move(BLACK, from(1, 6).to(1, 7));
        move(WHITE, from(2, 1).to(3, 1));
        move(BLACK, from(1, 7).to(1, 6));
        move(WHITE, from(3, 1).to(4, 1));
        move(BLACK, from(1, 6).to(1, 7));
        move(WHITE, from(4, 1).to(4, 0)); // King returns on it's default place
        move(BLACK, from(1, 7).to(1, 6));
        move(WHITE, from(3, 0).to(0, 0)); // Rook returns on it's default place
        move(BLACK, from(1, 6).to(1, 7));
        assertE(".w......" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "R...W..R");

        // when try to make one more castling
        move(WHITE, from(4, 0).to(0, 0));

        // then
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToMakeCastlingWithRook_ifTheRookAlreadyMoved() {

        givenFl(".w......" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "R...W..R");
        move(WHITE, from(0, 0).to(0, 1));
        move(BLACK, from(1, 7).to(1, 6));
        move(WHITE, from(0, 1).to(0, 0)); // Rook returns on it's default position
        move(BLACK, from(1, 6).to(1, 7));

        // when trying to make castling with rook that already moved
        move(WHITE, from(4, 0).to(0, 0));

        // then
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToMakeCastling_ifThereIsFriendlyPieceBetweenKingAndRook() {

        givenFl(".w......" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "R.B.W..R");

        // when
        move(WHITE, from(4, 0).to(0, 0));

        // then
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToMakeCastling_ifThereIsEnemyPieceBetweenKingAndRook() {

        givenFl(".w......" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "R.b.W..R");

        // when
        move(WHITE, from(4, 0).to(0, 0));

        // then
        assertE(".w......" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "R.b.W..R");
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToMakeCastling_ifKingIsUnderACheck() {

        givenFl(".w......" +
                "........" +
                "........" +
                "........" +
                "........" +
                "..b....." +
                "........" +
                "R...W..R");

        // when
        move(WHITE, from(4, 0).to(0, 0));

        // then
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToMakeLongCastling_ifKingShouldPassThroughSquareWhichAttackedByEnemy() {

        givenFl(".w......" +
                "........" +
                "........" +
                "........" +
                "........" +
                "...r...." +
                "........" +
                "R...W..R");

        // when
        move(WHITE, from(4, 0).to(0, 0));

        // then
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToMakeShortCastling_ifKingShouldPassThroughSquareWhichAttackedByEnemy() {

        givenFl(".w......" +
                "........" +
                "........" +
                "........" +
                "........" +
                ".....r.." +
                "........" +
                "R...W..R");

        // when
        move(WHITE, from(4, 0).to(7, 0));

        // then
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToMakeLongCastling_ifAfterThatKingWillBeUnderCheck() {

        givenFl(".w......" +
                "........" +
                "........" +
                "........" +
                "........" +
                "..r....." +
                "........" +
                "R...W..R");

        // when
        move(WHITE, from(4, 0).to(0, 0));

        // then
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToMakeShortCastling_ifAfterThatKingWillBeUnderCheck() {

        givenFl(".w......" +
                "........" +
                "........" +
                "........" +
                "........" +
                "......r." +
                "........" +
                "R...W..R");

        // when
        move(WHITE, from(4, 0).to(7, 0));

        // then
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToMakeCastling_ifRookAndKingIsUnderAttackOfOnePiece() {

        givenFl(".w......" +
                "........" +
                "........" +
                "q......." +
                "........" +
                "........" +
                "........" +
                "R...W..R");

        // when
        move(WHITE, from(4, 0).to(0, 0));

        // then
        fired(WHITE, WRONG_MOVE);
    }


    /**************************
     *    CASTLING ALLOWED    *
     **************************/

    @Test
    public void shouldNotBeAbleToMakeCastlingWithEnemyRook() {

        givenFl(".w.....i" +
                "........" +
                "........" +
                ".......y" +
                "Y......." +
                "........" +
                "........" +
                "I...W..R");

        // when
        move(WHITE, from(4, 0).to(0, 0));

        // then
        assertE(".w.....i" +
                "........" +
                "........" +
                ".......y" +
                "Y......." +
                "........" +
                "........" +
                "I...W..R");
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldBeAbleToMakeCastling_red() {

        givenFl(".w.....i" +
                "........" +
                "........" +
                ".......y" +
                "Y......." +
                "........" +
                "........" +
                "I...W..R");
        move(WHITE, from(4, 0).to(4, 1));
        move(BLACK, from(1, 7).to(1, 6));

        // when
        move(RED, from(0, 3).to(0, 0));

        // then
        assertE(".......i" +
                ".w......" +
                "........" +
                ".......y" +
                "........" +
                "I......." +
                "Y...W..." +
                ".......R");
        neverFired(RED, WRONG_MOVE);
    }

    @Test
    public void shouldBeAbleToMakeCastling_blue() {

        givenFl(".w.....i" +
                "........" +
                "........" +
                ".......y" +
                "Y......." +
                "........" +
                "........" +
                "I...W...");
        move(WHITE, from(4, 0).to(4, 1));
        move(BLACK, from(1, 7).to(1, 6));
        move(RED, from(0, 3).to(0, 0));

        // when
        move(BLUE, from(7, 4).to(7, 7));

        // then
        assertE("........" +
                ".w.....y" +
                ".......i" +
                "........" +
                "........" +
                "I......." +
                "Y...W..." +
                "........");
        neverFired(BLUE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToMakeCastling_ifKingIsUnderAttack() {

        givenFl(".w.....i" +
                "........" +
                "........" +
                "R......y" +
                "Y......." +
                "........" +
                "........" +
                "I...W...");
        move(WHITE, from(4, 0).to(4, 1));
        move(BLACK, from(1, 7).to(1, 6));
        move(RED, from(0, 3).to(1, 3));

        // when
        move(BLUE, from(7, 4).to(7, 7));

        // then
        assertE(".......i" +
                ".w......" +
                "........" +
                "R......y" +
                ".Y......" +
                "........" +
                "....W..." +
                "I.......");
        fired(BLUE, WRONG_MOVE);
    }

    @Test
    public void shouldBeAbleToMakeCastling_ifKingIsNotUnderAttack() {

        givenFl(".w.....i" +
                "........" +
                "........" +
                "I..b...y" +
                "Y......." +
                "........" +
                "........" +
                "I...W...");
        move(WHITE, from(4, 0).to(4, 1));
        move(BLACK, from(1, 7).to(1, 6));
        move(RED, from(0, 3).to(0, 0));

        // when
        move(BLUE, from(7, 4).to(7, 7));

        // then
        assertE("........" +
                ".w.....y" +
                ".......i" +
                "I..b...." +
                "........" +
                "I......." +
                "Y...W..." +
                "........");
        neverFired(BLUE, WRONG_MOVE);
    }

    @Test
    public void blueShouldBeAbleToMakeCastling_ifUnderRedAttack() {

        givenFl(".w.....i" +
                "........" +
                "........" +
                "I......y" +
                "Y......." +
                "........" +
                "........" +
                "I...W...");
        move(WHITE, from(4, 0).to(4, 1));
        move(BLACK, from(1, 7).to(1, 6));
        move(RED, from(0, 3).to(0, 0));

        // when
        move(BLUE, from(7, 4).to(7, 7));

        // then
        assertE("........" +
                ".w.....y" +
                ".......i" +
                "I..b...." +
                "........" +
                "I......." +
                "Y...W..." +
                "........");
        neverFired(BLUE, WRONG_MOVE);
    }
    // опасность оценивать только следующего хода, а не в принципе
}
