package com.codenjoy.dojo.chess.engine.model;

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


import com.codenjoy.dojo.services.printer.CharElements;

import java.util.Arrays;

public enum Element implements CharElements {

    SQUARE('.'),
    BARRIER(' '),

    WHITE_KING('W'),
    WHITE_QUEEN('Q'),
    WHITE_ROOK('R'),
    WHITE_BISHOP('B'),
    WHITE_KNIGHT('K'),
    WHITE_PAWN('P'),

    BLACK_KING('w'),
    BLACK_QUEEN('q'),
    BLACK_ROOK('r'),
    BLACK_BISHOP('b'),
    BLACK_KNIGHT('k'),
    BLACK_PAWN('p'),

    RED_KING('Y'),
    RED_QUEEN('X'),
    RED_ROOK('I'),
    RED_BISHOP('G'),
    RED_KNIGHT('L'),
    RED_PAWN('Z'),

    BLUE_KING('y'),
    BLUE_QUEEN('x'),
    BLUE_ROOK('i'),
    BLUE_BISHOP('g'),
    BLUE_KNIGHT('l'),
    BLUE_PAWN('z');

    final char ch;

    Element(char ch) {
        this.ch = ch;
    }

    @Override
    public char ch() {
        return ch;
    }

    @Override
    public String toString() {
        return String.valueOf(ch);
    }

    public static Element of(char ch) {
        for (Element element : Element.values()) {
            if (element.ch == ch) {
                return element;
            }
        }
        return null;
    }

    public static Element[] pieces() {
        return Arrays.stream(Element.values())
                .filter(e -> e != SQUARE)
                .filter(e -> e != BARRIER)
                .toArray(Element[]::new);
    }
}
