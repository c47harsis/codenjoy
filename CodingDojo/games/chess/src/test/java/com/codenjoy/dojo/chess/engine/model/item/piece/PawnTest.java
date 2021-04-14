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

import static com.codenjoy.dojo.chess.engine.model.Color.*;
import static com.codenjoy.dojo.chess.engine.service.Move.from;
import static com.codenjoy.dojo.chess.engine.model.Event.WRONG_MOVE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PawnTest extends AbstractPieceTest {

    public PawnTest() {
        super(Element.WHITE_PAWN);
    }

    @Override
    public void shouldMoveInAccordanceWithClassicChessRules() {

        givenFl(classicBoard());
        Piece whitePawn = getPieceAt(4, 1);

        assertCanMoveOnlyTo(whitePawn,
                new PointImpl(4, 2),
                new PointImpl(4, 3)
        );
    }

    @Override
    public void shouldBeAbleToTakeEnemyPiece() {

        // given
        givenFl("rkbqwbkr" +
                "pppp.ppp" +
                "........" +
                "........" +
                "........" +
                "....p..." +
                ".....P.." +
                "RKBQWBKR");
        Piece blackPawn = getPieceAt(4, 2);

        // when
        move(WHITE, from(5, 1).to(4, 2));

        // then
        assertE("rkbqwbkr" +
                "pppp.ppp" +
                "........" +
                "........" +
                "........" +
                "....P..." +
                "........" +
                "RKBQWBKR");
        assertFalse(blackPawn.isAlive());
        neverFired(WHITE, WRONG_MOVE);
    }

    @Override
    public void shouldNotBeAbleToTakeFriendlyPiece() {

        // given
        givenFl("rkbqwbkr" +
                "pppp.ppp" +
                "........" +
                "........" +
                "........" +
                "....Q..." +
                ".....P.." +
                "RKB.WBKR");
        Piece whiteQueen = getPieceAt(4, 2);

        // when
        move(WHITE, from(5, 1).to(4, 2));

        // then
        assertE("rkbqwbkr" +
                "pppp.ppp" +
                "........" +
                "........" +
                "........" +
                "....Q..." +
                ".....P.." +
                "RKB.WBKR");
        assertTrue(whiteQueen.isAlive());
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAbleToMoveSomewhere_ifThereIsEnemyPieceOnTheWay() {

        // when given
        givenFl("rkbqwbkr" +
                "pppp.ppp" +
                "........" +
                "........" +
                "........" +
                "....p..." +
                "PPPPPPPP" +
                "RKBQWBKR");
        Piece whitePawn = getPieceAt(4, 1);

        // then
        assertCanMoveOnlyTo(whitePawn, Arrays.array());
    }

    @Test
    public void shouldNotBeAbleToMoveSomewhere_ifThereIsFriendlyPieceOnTheWay() {

        // when given
        givenFl("rkbqwbkr" +
                "pppp.ppp" +
                "........" +
                "........" +
                "........" +
                "....Q..." +
                "PPPPPPPP" +
                "RKB.WBKR");
        Piece whitePawn = getPieceAt(4, 1);

        // then
        assertCanMoveOnlyTo(whitePawn, Arrays.array());
    }

    @Test
    public void shouldNotBeAbleToMoveTwoStepsForward_ifThereIsAnotherPieceAtTheTargetPosition() {

        // when given
        givenFl("rkbqwbkr" +
                "pppp.ppp" +
                "........" +
                "........" +
                "....p..." +
                "........" +
                "PPPPPPPP" +
                "RKBQWBKR");
        Piece whitePawn = getPieceAt(4, 1);

        // then
        assertCanMoveOnlyTo(whitePawn, new PointImpl(4, 2));
    }

    @Test
    public void shouldNotBeAbleToMoveTwoStepsForward_ifNotAtStartPosition() {

        givenFl(classicBoard());
        Piece whitePawn = getPieceAt(4, 1);

        // when
        Preconditions preconditions = () -> {
            move(WHITE, from(4, 1).to(4, 2));
            move(BLACK, from(3, 6).to(3, 5));
        };

        // then
        assertCanMoveOnlyTo(preconditions, whitePawn, new PointImpl(4, 3));
    }

    @Test
    public void blackPawnsShouldBeAbleToMoveDown() {

        givenFl(classicBoard());
        Piece blackPawn = getPieceAt(3, 6);

        // when
        Preconditions preconditions = () -> move(WHITE, from(4, 1).to(4, 2));

        // then
        assertCanMoveOnlyTo(preconditions, blackPawn,
                new PointImpl(3, 5),
                new PointImpl(3, 4)
        );
    }

//    private String fourPlayersBoard() {
//        return "  rkbqwbkr  " +
//                "  pppppppp  " +
//                "IZ........zi" +
//                "LZ........zl" +
//                "GZ........zg" +
//                "XZ........zx" +
//                "YZ........zy" +
//                "GZ........zg" +
//                "LZ........zl" +
//                "IZ........zi" +
//                "  PPPPPPPP  " +
//                "  RKBQWBKR  ";
//    }

//    TODO:  4x4
//    @Test
//    public void pawnsOfEachColorShouldMoveInSpecifiedDirection() {
//
//        givenFl(fourPlayersBoard());
//        Piece redPawn = getPieceAt(1, 5);
//        Piece bluePawn = getPieceAt(10, 5);
//
//        // when
//        Preconditions preconditionsForRed = () -> {
//            move(WHITE, from(4, 1).to(4, 3));
//            move(BLACK, from(4, 10).to(4, 9));
//        };
//
//        Preconditions preconditionsForBlue = () -> {
//            preconditionsForRed.run();
//            move(RED, from(1, 5).to(2, 5));
//        };
//
//        // then
//        assertCanMoveOnlyTo(preconditionsForRed, redPawn,
//                new PointImpl(2, 5),
//                new PointImpl(3, 5)
//        );
//
//        assertCanMoveOnlyTo(preconditionsForBlue, bluePawn,
//                new PointImpl(9, 5),
//                new PointImpl(8, 5)
//        );
//    }
}
