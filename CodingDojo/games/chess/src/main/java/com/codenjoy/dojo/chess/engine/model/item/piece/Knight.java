package com.codenjoy.dojo.chess.engine.model.item.piece;

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


import com.codenjoy.dojo.chess.engine.model.Color;
import com.codenjoy.dojo.chess.engine.service.GameBoard;
import com.codenjoy.dojo.chess.engine.service.Move;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;

public class Knight extends Piece {

    public Knight(Color color, GameBoard board, Point position) {
        super(Type.KNIGHT, color, board, position);
    }

    @Override
    public List<Move> getAvailableMoves() {
        return availableMoves(board, position, color);
    }

    private static boolean isAvailable(GameBoard board, Point position, Color color) {
        return board.getPieceAt(position)
                .map(p -> p.getColor() != color)
                .orElse(board.isInBounds(position));
    }

    private static List<Point> moves(Point position) {
        int x = position.getX();
        int y = position.getY();
        return Lists.newArrayList(
                new PointImpl(x - 2, y - 1),
                new PointImpl(x - 2, y + 1),
                new PointImpl(x + 2, y - 1),
                new PointImpl(x + 2, y + 1),
                new PointImpl(x - 1, y + 2),
                new PointImpl(x + 1, y + 2),
                new PointImpl(x - 1, y - 2),
                new PointImpl(x + 1, y - 2)
        );
    }

    public static List<Move> availableMoves(GameBoard board, Point position, Color color) {
        return moves(position).stream()
                .filter(p -> isAvailable(board, p, color))
                .map(m -> Move.from(position).to(m))
                .collect(Collectors.toList());
    }
}