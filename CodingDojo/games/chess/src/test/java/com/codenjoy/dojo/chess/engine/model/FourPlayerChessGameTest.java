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
import static com.codenjoy.dojo.chess.engine.service.Move.*;

/**
 * https://en.wikipedia.org/wiki/Four-player_chess
 * Order in this implementation: WHITE -> RED -> BLACK -> BLUE
 */
@Ignore
public class FourPlayerChessGameTest extends AbstractGameTest {

    private String fourPlayersBoard() {
        return  "   rkbwqbkr   " +
                "   pppppppp   " +
                "   ........   " +
                "IZ..........zi" +
                "LZ..........zl" +
                "GZ..........zg" +
                "YZ..........zx" +
                "XZ..........zy" +
                "GZ..........zg" +
                "LZ..........zl" +
                "IZ..........zi" +
                "   ........   " +
                "   PPPPPPPP   " +
                "   RKBQWBKR   ";
    }

    @Test
    public void shouldDrawBoardProperlyForAllPlayers() {

        // when
        givenFl(fourPlayersBoard());

        // then
        for (Color color: game.getColors()) {
            assertE(fourPlayersBoard(), color);
        }
    }

    @Test
    public void whitesShouldMoveFirst() {

        givenFl(fourPlayersBoard());

        // when
        move(BLACK, from(3, 12).to(3, 11));
        move(RED, from(1, 3).to(2, 3));
        move(BLUE, from(12, 10).to(11, 10));
        move(BLACK, from(3, 12).to(3, 11));
        move(RED, from(1, 3).to(2, 3));
        move(BLUE, from(12, 10).to(11, 10));
        move(WHITE, from(3, 1).to(3, 2));

        // then should ignore all except whites
        assertE("   rkbwqbkr   " +
                "   pppppppp   " +
                "   ........   " +
                "IZ..........zi" +
                "LZ..........zl" +
                "GZ..........zg" +
                "YZ..........zx" +
                "XZ..........zy" +
                "GZ..........zg" +
                "LZ..........zl" +
                "IZ..........zi" +
                "   P.......   " +
                "   .PPPPPPP   " +
                "   RKBQWBKR   ");
    }

    @Test
    public void redsShouldMoveSecond() {

        // given
        whitesShouldMoveFirst();

        // when
        move(BLACK, from(3, 12).to(3, 11));
        move(BLUE, from(12, 10).to(11, 10));
        move(WHITE, from(10, 1).to(10, 2));
        move(BLACK, from(3, 12).to(3, 11));
        move(BLUE, from(12, 10).to(11, 10));
        move(WHITE, from(10, 1).to(10, 2));
        move(RED, from(1, 10).to(2, 10));

        // then should ignore all except reds
        assertE("   rkbwqbkr   " +
                "   pppppppp   " +
                "   ........   " +
                "I.Z.........zi" +
                "LZ..........zl" +
                "GZ..........zg" +
                "YZ..........zx" +
                "XZ..........zy" +
                "GZ..........zg" +
                "LZ..........zl" +
                "IZ..........zi" +
                "   P.......   " +
                "   .PPPPPPP   " +
                "   RKBQWBKR   ");
    }

    @Test
    public void blacksShouldMoveThird() {

        // given
        redsShouldMoveSecond();

        // when
        move(BLUE, from(12, 10).to(11, 10));
        move(WHITE, from(10, 1).to(10, 2));
        move(RED, from(1, 3).to(2, 3));
        move(BLUE, from(12, 10).to(11, 10));
        move(WHITE, from(10, 1).to(10, 2));
        move(RED, from(1, 3).to(2, 3));
        move(BLACK, from(10, 12).to(10, 11));

        // then should ignore all except blacks
        assertE("   rkbwqbkr   " +
                "   ppppppp.   " +
                "   .......p   " +
                "I.Z.........zi" +
                "LZ..........zl" +
                "GZ..........zg" +
                "YZ..........zx" +
                "XZ..........zy" +
                "GZ..........zg" +
                "LZ..........zl" +
                "IZ..........zi" +
                "   P.......   " +
                "   .PPPPPPP   " +
                "   RKBQWBKR   ");
    }

    @Test
    public void bluesShouldMoveForth() {

        // given
        blacksShouldMoveThird();

        // when
        move(WHITE, from(10, 1).to(10, 2));
        move(RED, from(1, 3).to(2, 3));
        move(BLACK, from(3, 12).to(3, 11));
        move(WHITE, from(10, 1).to(10, 2));
        move(RED, from(1, 3).to(2, 3));
        move(BLACK, from(3, 12).to(3, 11));
        move(BLUE, from(12, 3).to(11, 3));

        // then should ignore all except blues (oh yeah)
        assertE("   rkbwqbkr   " +
                "   ppppppp.   " +
                "   .......p   " +
                "I.Z.........zi" +
                "LZ..........zl" +
                "GZ..........zg" +
                "YZ..........zx" +
                "XZ..........zy" +
                "GZ..........zg" +
                "LZ..........zl" +
                "IZ.........z.i" +
                "   P.......   " +
                "   .PPPPPPP   " +
                "   RKBQWBKR   ");
    }

    @Test
    public void andWhitesAgainShouldMoveAfterBlues() {

        // given
        bluesShouldMoveForth();

        // when
        move(RED, from(1, 3).to(2, 3));
        move(BLACK, from(3, 12).to(3, 11));
        move(BLUE, from(12, 10).to(11, 10));
        move(RED, from(1, 3).to(2, 3));
        move(BLACK, from(3, 12).to(3, 11));
        move(BLUE, from(12, 10).to(11, 10));
        move(WHITE, from(10, 1).to(10, 2));

        // then should ignore all except whites
        assertE("   rkbwqbkr   " +
                "   ppppppp.   " +
                "   .......p   " +
                "I.Z.........zi" +
                "LZ..........zl" +
                "GZ..........zg" +
                "YZ..........zx" +
                "XZ..........zy" +
                "GZ..........zg" +
                "LZ..........zl" +
                "IZ.........z.i" +
                "   P......P   " +
                "   .PPPPPP.   " +
                "   RKBQWBKR   ");
    }


}
