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
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.codenjoy.dojo.services.Direction.*;

public class Bishop extends Piece {

    public Bishop(Color color, GameBoard board, Point position) {
        super(Type.BISHOP, color, board, position);
    }

    @Override
    public List<Move> getAvailableMoves() {
        return availableMoves(board, position, color);
    }

    public static List<Move> availableMoves(GameBoard board, Point position, Color color) {
        return Stream.of(LEFT, UP, RIGHT, DOWN)
                .map(direction -> {
                    Point dest = diagonal(position, direction, direction.clockwise());
                    List<Move> result = Lists.newArrayList();
                    while (board.isInBounds(dest) && board.getPieceAt(dest).isEmpty()) {
                        result.add(Move.from(position).to(dest));
                        dest = diagonal(dest, direction, direction.clockwise());
                    }
                    if (board.isInBounds(dest) && board.getPieceAt(dest).isPresent() && board.getPieceAt(dest).get().getColor() != color) {
                        result.add(Move.from(position).to(dest));
                    }
                    return result;
                }).flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
