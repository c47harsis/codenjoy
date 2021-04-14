package com.codenjoy.dojo.chess.engine.service;

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
import com.codenjoy.dojo.chess.engine.model.Color;
import com.codenjoy.dojo.chess.engine.model.item.piece.Piece;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.Objects;

import static com.codenjoy.dojo.chess.engine.model.Element.*;
import static com.codenjoy.dojo.chess.engine.model.Color.*;
import static com.codenjoy.dojo.chess.engine.model.item.piece.Piece.Type.*;

public class ElementMapper {
    private static final BiMap<ColorAndType, Element> elementsByColorAndType = HashBiMap.create();

    static {
        define(WHITE, KING, WHITE_KING);
        define(WHITE, QUEEN, WHITE_QUEEN);
        define(WHITE, KNIGHT, WHITE_KNIGHT);
        define(WHITE, BISHOP, WHITE_BISHOP);
        define(WHITE, ROOK, WHITE_ROOK);
        define(WHITE, PAWN, WHITE_PAWN);

        define(BLACK, KING, BLACK_KING);
        define(BLACK, QUEEN, BLACK_QUEEN);
        define(BLACK, KNIGHT, BLACK_KNIGHT);
        define(BLACK, BISHOP, BLACK_BISHOP);
        define(BLACK, ROOK, BLACK_ROOK);
        define(BLACK, PAWN, BLACK_PAWN);

        define(RED, KING, RED_KING);
        define(RED, QUEEN, RED_QUEEN);
        define(RED, KNIGHT, RED_KNIGHT);
        define(RED, BISHOP, RED_BISHOP);
        define(RED, ROOK, RED_ROOK);
        define(RED, PAWN, RED_PAWN);

        define(BLUE, KING, BLUE_KING);
        define(BLUE, QUEEN, BLUE_QUEEN);
        define(BLUE, KNIGHT, BLUE_KNIGHT);
        define(BLUE, BISHOP, BLUE_BISHOP);
        define(BLUE, ROOK, BLUE_ROOK);
        define(BLUE, PAWN, BLUE_PAWN);
    }

    private static void define(Color color, Piece.Type type, Element element) {
        elementsByColorAndType.put(new ColorAndType(color, type), element);
    }

    @SuppressWarnings("unused")
    public static Element mapToElement(Piece piece) {
        return mapToElement(piece.getColor(), piece.getType());
    }

    @SuppressWarnings("unused")
    public static Element mapToElement(Color color, Piece.Type type) {
        return elementsByColorAndType.get(new ColorAndType(color, type));
    }

    @SuppressWarnings("unused")
    public static Color mapToColor(Element element) {
        return elementsByColorAndType.inverse().get(element).color;
    }

    @SuppressWarnings("unused")
    public static Piece.Type mapToPieceType(Element element) {
        return elementsByColorAndType.inverse().get(element).type;
    }

    private static class ColorAndType {
        private final Color color;
        private final Piece.Type type;

        public ColorAndType(Color color, Piece.Type type) {
            this.color = color;
            this.type = type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ColorAndType that = (ColorAndType) o;
            return color == that.color && type == that.type;
        }

        @Override
        public int hashCode() {
            return Objects.hash(color, type);
        }
    }
}
