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

import com.codenjoy.dojo.chess.engine.model.item.piece.Piece;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

import java.util.Objects;

public class Move {

    private final Point from;
    private final Point to;

    @Override
    public String toString() {
        return "Move " +
                "from " + from +
                " to " + to +
                (withPromotion() ? " with promotion " + promotion : "");
    }

    public Piece.Type getPromotion() {
        return promotion;
    }

    private Piece.Type promotion;

    private Move(Point from, Point to) {
        this.from = from;
        this.to = to;
    }

    public static Move decode(int... p) {
        if (p.length < 4 || p.length > 5) {
            return null;
        }
        Move move = Move.from(p[0], p[1]).to(p[2], p[3]);
        if (p.length == 5) {
            move.promotion(p[4]);
        }
        return move;
    }

    public static Builder from(int x, int y) {
        return from(new PointImpl(x, y));
    }

    public static Builder from(Point from) {
        return new Builder(from);
    }

    public int[] command() {
        return promotion == null
                ? new int[]{from.getX(), from.getY(), to.getX(), to.getY()}
                : new int[]{from.getX(), from.getY(), to.getX(), to.getY(), promotion.getId()};
    }

    public Point getFrom() {
        return from;
    }

    public Point getTo() {
        return to;
    }

    public Move promotion(Piece.Type piece) {
        promotion = piece;
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public Move promotion(int pieceId) {
        return promotion(Piece.Type.byId(pieceId));
    }

    public boolean withPromotion() {
        return promotion != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return Objects.equals(from, move.from) && Objects.equals(to, move.to) && promotion == move.promotion;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, promotion);
    }


    public static class Builder {

        private final Point from;

        private Builder(Point from) {
            this.from = from;
        }

        public Move to(int x, int y) {
            return to(new PointImpl(x, y));
        }

        public Move to(Point to) {
            return new Move(from, to);
        }
    }
}
