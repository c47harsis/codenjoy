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

import org.junit.Test;

import static com.codenjoy.dojo.chess.engine.model.Color.BLACK;
import static com.codenjoy.dojo.chess.engine.model.Color.WHITE;
import static com.codenjoy.dojo.chess.engine.service.Move.from;
import static com.codenjoy.dojo.chess.engine.model.Event.WRONG_MOVE;

/**
 * Wiki: https://en.wikipedia.org/wiki/En_passant
 */
@SuppressWarnings("SpellCheckingInspection")
public class EnPassantTest extends AbstractGameTest {

    @Test
    public void shouldBeAllowed_whenEnemyPawnSteps2SquaresForwardAndOurPawnCanImmediatelyAttackMiddleCellOfTheMove() {

        givenFl("rkbqwbkr" +
                "pppp.ppp" +
                "........" +
                "........" +
                "....p..." +
                "........" +
                "PPPPPPPP" +
                "RKBQWBKR");
        move(WHITE, from(5, 1).to(5, 3));

        // when
        move(BLACK, from(4, 3).to(5, 2));

        // then
        assertE("rkbqwbkr" +
                "pppp.ppp" +
                "........" +
                "........" +
                "........" +
                ".....p.." +
                "PPPPP.PP" +
                "RKBQWBKR");
        neverFired(BLACK, WRONG_MOVE);
    }

    @Test
    public void shouldWorkOnlyWithPawns() {

        givenFl("rkb.wbkr" +
                "pppppppp" +
                "........" +
                "........" +
                "....q..." +
                "........" +
                "PPPPPPPP" +
                "RKBQWBKR");
        move(WHITE, from(5, 1).to(5, 3));

        // when
        move(BLACK, from(4, 3).to(5, 2));

        // then
        assertE("rkb.wbkr" +
                "pppppppp" +
                "........" +
                "........" +
                ".....P.." +
                ".....q.." +
                "PPPPP.PP" +
                "RKBQWBKR");
    }

    @Test
    public void shouldNotBeAllowed_ifHasNotDoneImmediately() {

        givenFl("rkbqwbkr" +
                "pppp.ppp" +
                "........" +
                "........" +
                "....p..." +
                "........" +
                "PPPPPPPP" +
                "RKBQWBKR");
        move(WHITE, from(5, 1).to(5, 3));
        move(BLACK, from(0, 6).to(0, 5));
        move(WHITE, from(7, 1).to(7, 2));

        assertE("rkbqwbkr" +
                ".ppp.ppp" +
                "p......." +
                "........" +
                "....pP.." +
                ".......P" +
                "PPPPP.P." +
                "RKBQWBKR");

        // when trying to make "en passant"
        move(BLACK, from(4, 3).to(5, 2));

        // then
        fired(BLACK, WRONG_MOVE);
    }
}
