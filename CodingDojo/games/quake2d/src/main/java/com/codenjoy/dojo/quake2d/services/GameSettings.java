package com.codenjoy.dojo.quake2d.services;

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


import com.codenjoy.dojo.quake2d.model.Level;
import com.codenjoy.dojo.quake2d.model.LevelImpl;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;

import static com.codenjoy.dojo.quake2d.services.GameSettings.Keys.*;

public final class GameSettings extends SettingsImpl implements SettingsReader<GameSettings> {

    public enum Keys implements Key {

        KILL_SCORE("Kill score"),
        INJURE_SCORE("Injure score"),
        LEVEL_MAP("Level map");

        private String key;

        Keys(String key) {
            this.key = key;
        }

        @Override
        public String key() {
            return key;
        }
    }

    public GameSettings() {
        addEditBox(KILL_SCORE.key()).type(Integer.class).def(30);
        addEditBox(INJURE_SCORE.key()).type(Integer.class).def(100);

        addEditBox(LEVEL_MAP.key()).multiline().type(String.class)
                .def("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                    "☼                            ☼" +
                    "☼                            ☼" +
                    "☼                            ☼" +
                    "☼                  ☼☼☼☼      ☼" +
                    "☼                  ☼☼☼☼      ☼" +
                    "☼    ☼☼☼☼          ☼☼☼☼      ☼" +
                    "☼    ☼☼☼☼          ☼☼☼☼      ☼" +
                    "☼    ☼☼☼☼                    ☼" +
                    "☼    ☼☼☼☼                    ☼" +
                    "☼                            ☼" +
                    "☼                            ☼" +
                    "☼           ☼☼☼☼             ☼" +
                    "☼           ☼☼☼☼             ☼" +
                    "☼           ☼☼☼☼             ☼" +
                    "☼           ☼☼☼☼             ☼" +
                    "☼                            ☼" +
                    "☼                            ☼" +
                    "☼                   ☼☼☼☼     ☼" +
                    "☼                   ☼☼☼☼     ☼" +
                    "☼    ☼☼☼☼                    ☼" +
                    "☼    ☼☼☼☼                    ☼" +
                    "☼    ☼☼☼☼                    ☼" +
                    "☼    ☼☼☼☼                    ☼" +
                    "☼                ☼☼☼☼        ☼" +
                    "☼                ☼☼☼☼        ☼" +
                    "☼                ☼☼☼☼        ☼" +
                    "☼       ☺        ☼☼☼☼        ☼" +
                    "☼                            ☼" +
                    "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼");
    }

    public Level level() {
        return new LevelImpl(string(LEVEL_MAP));
    }

}