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
import org.junit.Test;

import static com.codenjoy.dojo.chess.engine.model.Color.WHITE;
import static com.codenjoy.dojo.chess.engine.service.Move.from;
import static com.codenjoy.dojo.chess.engine.model.Event.WRONG_MOVE;

/**
 * Wiki: https://en.wikipedia.org/wiki/Promotion_(chess)
 */
@SuppressWarnings("SpellCheckingInspection")
public class PromotionTest extends AbstractGameTest {

    @Test
    public void shouldBeFiredWrongMove_whenTryingToPromoteKing() {

        givenFl("w......." +
                ".......P" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "RKBQWBKR");

        // when
        move(WHITE, from(7, 6).to(7, 7).promotion(Piece.Type.KING));

        // then
        assertE("w......." +
                ".......P" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "RKBQWBKR");
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAllowed_whenTryingToPromoteIfPawnNotAtLastLine() {

        givenFl("w......." +
                "........" +
                ".......P" +
                "........" +
                "........" +
                "........" +
                "........" +
                "RKBQWBKR");

        // when
        move(WHITE, from(7, 5).to(7, 6).promotion(Piece.Type.QUEEN));

        // then
        assertE("w......." +
                "........" +
                ".......P" +
                "........" +
                "........" +
                "........" +
                "........" +
                "RKBQWBKR");
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAllowed_whenTryingToMakePromotionByNotAPawn() {

        givenFl("w......." +
                ".......Q" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "RKB.WBKR");

        // when
        move(WHITE, from(7, 6).to(7, 5).promotion(Piece.Type.PAWN));

        // then
        assertE("w......." +
                ".......Q" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "RKB.WBKR");
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldBeAllowed_whenTryingToPromoteAPieceExceptKing() {

        givenFl("w......." +
                ".......P" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "RKBQWBKR");

        // when
        move(WHITE, from(7, 6).to(7, 7).promotion(Piece.Type.QUEEN));

        // then
        assertE("w......Q" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "RKBQWBKR");
        neverFired(WHITE, WRONG_MOVE);
    }
}
