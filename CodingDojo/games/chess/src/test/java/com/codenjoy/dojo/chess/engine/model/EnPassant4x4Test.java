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

import com.codenjoy.dojo.chess.engine.model.item.piece.Piece;
import org.junit.Ignore;
import org.junit.Test;

import static com.codenjoy.dojo.chess.engine.model.Color.*;
import static com.codenjoy.dojo.chess.engine.service.Move.from;
import static com.codenjoy.dojo.chess.engine.model.Event.WRONG_MOVE;
import static org.junit.Assert.assertFalse;

@Ignore
public class EnPassant4x4Test extends AbstractGameTest {

    @Test
    public void shouldBeAllowed_whenBlueAttacksRed() {

        givenFl("rkbqwbkr" +
                "pppppppp" +
                "........" +
                "Y..z...y" +
                ".Z......" +
                "........" +
                "PPPPPPPP" +
                "RKBQWBKR");
        Piece redPawn = getPieceAt(1, 3);
        move(WHITE, from(4, 1).to(4, 2));
        move(BLACK, from(0, 6).to(0, 5));
        move(RED, from(1, 3).to(2, 3));

        // when
        move(BLUE, from(3, 4).to(2, 3));

        // then
        assertE("rkbqwbkr" +
                ".ppppppp" +
                "p......." +
                "Y......y" +
                "..z....." +
                "....P..." +
                "PPPP.PPP" +
                "RKBQWBKR");
        neverFired(BLUE, WRONG_MOVE);
        assertFalse(redPawn.isAlive());
    }

    @Test
    public void shouldBeAllowed_whenRedAttacksBlue() {

        givenFl("rkbqwbkr" +
                "pppppppp" +
                "........" +
                "Y.....zy" +
                "...Z...." +
                "........" +
                "PPPPPPPP" +
                "RKBQWBKR");
        Piece bluePawn = getPieceAt(6, 4);
        move(WHITE, from(4, 1).to(4, 2));
        move(BLACK, from(0, 6).to(0, 5));
        move(RED, from(3, 3).to(4, 3));
        move(BLUE, from(6, 4).to(4, 4));
        move(WHITE, from(0, 1).to(0, 2));
        move(BLACK, from(7, 6).to(7, 5));

        // when
        move(RED, from(4, 3).to(5, 4));

        // then
        assertE("rkbqwbkr" +
                ".pppppp." +
                "p......p" +
                "Y....Z.y" +
                "........" +
                "P...P..." +
                ".PPP.PPP" +
                "RKBQWBKR");
        neverFired(BLUE, WRONG_MOVE);
        assertFalse(bluePawn.isAlive());
    }

    @Test
    public void shouldNotBeAllowedFromSide() {
        givenFl("rkbqwbkr" +
                "pppppppp" +
                "........" +
                "Y......y" +
                "...Z...." +
                "........" +
                "PPPPPPPP" +
                "RKBQWBKR");
        move(WHITE, from(4, 1).to(4, 3));
        move(BLACK, from(0, 6).to(0, 5));

        // when trying to do en passant from side
        move(RED, from(3, 3).to(4, 2));

        // then
        assertE("rkbqwbkr" +
                ".ppppppp" +
                "p......." +
                "Y......y" +
                "...ZP..." +
                "........" +
                "PPPP.PPP" +
                "RKBQWBKR");
        fired(RED, WRONG_MOVE);
    }
}
