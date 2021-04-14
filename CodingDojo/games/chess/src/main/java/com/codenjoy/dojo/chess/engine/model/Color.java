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

import com.codenjoy.dojo.services.Direction;

public enum Color {
    WHITE   (0, Direction.UP),
    BLACK   (2, Direction.DOWN),
    RED     (1, Direction.RIGHT),
    BLUE    (3, Direction.LEFT);

    private final int priority;
    private final Direction attackDirection;

    Color(int priority, Direction attackDirection) {
        this.priority = priority;
        this.attackDirection = attackDirection;
    }

    public int getPriority() {
        return priority;
    }

    public Direction getAttackDirection() {
        return attackDirection;
    }
}
