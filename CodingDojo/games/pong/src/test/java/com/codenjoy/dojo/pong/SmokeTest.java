package com.codenjoy.dojo.pong;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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


import com.codenjoy.dojo.client.local.LocalGameRunner;
import com.codenjoy.dojo.pong.client.Board;
import com.codenjoy.dojo.pong.client.YourSolver;
import com.codenjoy.dojo.pong.client.ai.AISolver;
import com.codenjoy.dojo.pong.services.GameRunner;
import com.codenjoy.dojo.pong.services.GameSettings;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.utils.Smoke;
import org.junit.Test;

import java.util.Arrays;

import static com.codenjoy.dojo.pong.services.GameSettings.Keys.LEVEL_MAP;
import static org.junit.Assert.assertEquals;

public class SmokeTest {

    @Test
    public void test() {
        Dice dice = LocalGameRunner.getDice("435874345435874365843564398", 100, 200);

        // about 1.2 sec
        int ticks = 1000;

        Smoke.play(ticks, "SmokeTest.data",
                new GameRunner() {
                    @Override
                    public Dice getDice() {
                        return dice;
                    }

                    @Override
                    public GameSettings getSettings() {
                        return new GameSettings()
                                .string(LEVEL_MAP,
                                        "           \n" +
                                        "-----------\n" +
                                        "           \n" +
                                        "           \n" +
                                        "           \n" +
                                        "     o     \n" +
                                        "           \n" +
                                        "           \n" +
                                        "           \n" +
                                        "-----------\n" +
                                        "           \n");
                    }
                },
                Arrays.asList(new AISolver(dice), new YourSolver(dice)),
                Arrays.asList(new Board(), new Board()));
    }
}