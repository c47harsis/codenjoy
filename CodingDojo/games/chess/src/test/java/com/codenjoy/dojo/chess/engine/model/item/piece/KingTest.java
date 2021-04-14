package com.codenjoy.dojo.chess.engine.model.item.piece;

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

import com.codenjoy.dojo.chess.engine.model.Element;
import com.codenjoy.dojo.services.PointImpl;
import org.fest.util.Arrays;
import org.junit.Test;

import static com.codenjoy.dojo.chess.engine.model.Color.WHITE;
import static com.codenjoy.dojo.chess.engine.service.Move.from;
import static com.codenjoy.dojo.chess.engine.model.Event.WRONG_MOVE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class KingTest extends AbstractPieceTest {

    public KingTest() {
        super(Element.WHITE_KING);
    }

    @Override
    public void shouldMoveInAccordanceWithClassicChessRules() {

        // when
        givenFl("w......." +
                "........" +
                "........" +
                "........" +
                "...W...." +
                "........" +
                "........" +
                "........");
        Piece whiteKing = getPieceAt(3, 3);

        // then
        assertCanMoveOnlyTo(whiteKing,
                new PointImpl(2, 2),
                new PointImpl(2, 3),
                new PointImpl(2, 4),
                new PointImpl(3, 2),
                new PointImpl(3, 4),
                new PointImpl(4, 2),
                new PointImpl(4, 3),
                new PointImpl(4, 4)
        );
    }

    @Override
    public void shouldBeAbleToTakeEnemyPiece() {

        // when
        givenFl("w......." +
                "........" +
                "........" +
                "....p..." +
                "...W...." +
                "........" +
                "........" +
                "........");
        Piece blackPawn = getPieceAt(4, 4);

        // when
        move(WHITE, from(3, 3).to(4, 4));

        // then
        assertE("w......." +
                "........" +
                "........" +
                "....W..." +
                "........" +
                "........" +
                "........" +
                "........");
        neverFired(WHITE, WRONG_MOVE);
        assertFalse(blackPawn.isAlive());
    }

    @Override
    public void shouldNotBeAbleToTakeFriendlyPiece() {

        // when
        givenFl("w......." +
                "........" +
                "........" +
                "....Q..." +
                "...W...." +
                "........" +
                "........" +
                "........");
        Piece whiteQueen = getPieceAt(4, 4);

        // when
        move(WHITE, from(3, 3).to(4, 4));

        // then
        assertE("w......." +
                "........" +
                "........" +
                "....Q..." +
                "...W...." +
                "........" +
                "........" +
                "........");
        fired(WHITE, WRONG_MOVE);
        assertTrue(whiteQueen.isAlive());
    }

    @Test
    public void shouldNotBeAbleToMoveOutOfSquaresOnBoard() {

        // when
        givenFl("W");
        Piece whiteKing = getPieceAt(0, 0);

        // then
        assertCanMoveOnlyTo(whiteKing, Arrays.array());
    }
}
