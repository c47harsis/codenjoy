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

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.codenjoy.dojo.services.Direction.*;

public abstract class Piece {

    protected final Color color;
    protected final Type type;
    protected final GameBoard board;
    protected Point position;
    protected boolean alive;
    protected boolean moved;
    protected List<Move> committedMoves;

    public Piece(Type type, Color color, GameBoard board, Point position) {
        this.type = type;
        this.color = color;
        this.board = board;
        this.position = position;
        this.alive = true;
        this.committedMoves = Lists.newArrayList();
    }

    public static Piece create(Type type, Color color, GameBoard board, Point position) {
        switch (type) {
            case KING:
                return new King(color, board, position);
            case QUEEN:
                return new Queen(color, board, position);
            case KNIGHT:
                return new Knight(color, board, position);
            case ROOK:
                return new Rook(color, board, position);
            case BISHOP:
                return new Bishop(color, board, position);
            case PAWN:
                return new Pawn(color, board, position);
            default:
                throw new IllegalArgumentException("Unknown piece type: " + type);
        }
    }

    protected static Point diagonal(Point position, Direction one, Direction two) {
        if (one == two || one.inverted() == two) {
            throw new IllegalArgumentException("Directions should be perpendicular for diagonal position calculation");
        }
        return one.change(two.change(position));
    }

    public boolean isMoved() {
        return moved;
    }

    public void move(Move move) {
        board.getPieceAt(move.getTo()).ifPresent(p -> p.setAlive(false));
        position = move.getTo();
        moved = true;
        committedMoves.add(move);
    }

    public Color getColor() {
        return color;
    }

    public Point getPosition() {
        return position;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public Type getType() {
        return type;
    }

    public abstract List<Move> getAvailableMoves();

    public Direction getAttackDirection() {
        return color.getAttackDirection();
    }

    @Override
    public String toString() {
        return "Piece{" +
                "color=" + color +
                ", type=" + type +
                ", position=" + position +
                ", alive=" + alive +
                ", moved=" + moved +
                '}';
    }

    public enum Type {
        KING(0, 20),
        QUEEN(1, 9),
        KNIGHT(2, 3),
        BISHOP(3, 5),
        ROOK(4, 5),
        PAWN(5, 1);

        private final int id;
        private final int cost;

        Type(int id, int cost) {
            this.id = id;
            this.cost = cost;
        }

        public static Type byId(int id) {
            for (Type type : Type.values()) {
                if (type.id == id) {
                    return type;
                }
            }
            return null;
        }

        public int getId() {
            return id;
        }

        public int getCost() {
            return cost;
        }
    }
}
