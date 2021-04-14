package com.codenjoy.dojo.chess.engine.service;

/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2020 Codenjoy
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


import com.codenjoy.dojo.chess.engine.level.Level;
import com.codenjoy.dojo.chess.engine.level.Levels;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;

import static com.codenjoy.dojo.chess.engine.service.GameSettings.Option.*;

public final class GameSettings extends SettingsImpl implements SettingsReader<GameSettings> {

    public enum Option implements Key {

        LEVEL_MAP("Level map"),
        GAME_OVER_IF_WRONG_MOVE(
                "If set TRUE and player making wrong move then he immediately loses, " +
                        "if set FALSE the player skips move"
        ),
        WAIT_UNTIL_MAKE_A_MOVE(
                "If set TRUE and player has not responded in right time then game waits him, " +
                        "if set FALSE the player skips move"
        );

        private final String key;

        Option(String key) {
            this.key = key;
        }

        @Override
        public String key() {
            return key;
        }
    }

    public GameSettings() {
        bool(GAME_OVER_IF_WRONG_MOVE, false);
        bool(WAIT_UNTIL_MAKE_A_MOVE, false);
        multiline(LEVEL_MAP, Levels.classicChessBoard());
    }

    public Level level() {
        return new Level(string(LEVEL_MAP));
    }
}