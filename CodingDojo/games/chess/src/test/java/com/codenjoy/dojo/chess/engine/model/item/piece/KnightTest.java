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

import static com.codenjoy.dojo.chess.engine.model.Color.WHITE;
import static com.codenjoy.dojo.chess.engine.service.Move.from;
import static com.codenjoy.dojo.chess.engine.model.Event.WRONG_MOVE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class KnightTest extends AbstractPieceTest {

    public KnightTest() {
        super(Element.WHITE_KNIGHT);
    }

    @Override
    public void shouldMoveInAccordanceWithClassicChessRules() {

        // when
        givenFl("w......." +
                "........" +
                "...b...." +
                "..pp...." +
                ".qpKp..." +
                "..ppp..." +
                "........" +
                ".......W");
        Piece whiteKnight = getPieceAt(3, 3);

        // then
        assertCanMoveOnlyTo(whiteKnight,
                new PointImpl(1, 2),
                new PointImpl(1, 4),
                new PointImpl(5, 2),
                new PointImpl(5, 4),
                new PointImpl(2, 5),
                new PointImpl(4, 5),
                new PointImpl(2, 1),
                new PointImpl(4, 1)
        );
    }

    @Override
    public void shouldBeAbleToTakeEnemyPiece() {

        givenFl("w......." +
                "........" +
                "....q..." +
                "........" +
                "...K...." +
                "........" +
                "........" +
                ".......W");
        Piece enemyQueen = getPieceAt(4, 5);

        // when
        move(WHITE, from(3, 3).to(4, 5));

        // then
        assertE("w......." +
                "........" +
                "....K..." +
                "........" +
                "........" +
                "........" +
                "........" +
                ".......W");
        neverFired(WHITE, WRONG_MOVE);
        assertFalse(enemyQueen.isAlive());
    }

    @Override
    public void shouldNotBeAbleToTakeFriendlyPiece() {

        givenFl("w......." +
                "........" +
                "....Q..." +
                "........" +
                "...K...." +
                "........" +
                "........" +
                ".......W");
        Piece friendlyQueen = getPieceAt(4, 5);

        // when
        move(WHITE, from(3, 3).to(4, 5));

        // then
        assertE("w......." +
                "........" +
                "....Q..." +
                "........" +
                "...K...." +
                "........" +
                "........" +
                ".......W");
        fired(WHITE, WRONG_MOVE);
        assertTrue(friendlyQueen.isAlive());
    }
}
