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

import com.codenjoy.dojo.chess.engine.model.Color;

import java.util.ArrayList;
import java.util.List;

public class GameHistory {

    private final List<HistoryRecord> records;

    public GameHistory() {
        this(new ArrayList<>());
    }

    public GameHistory(List<HistoryRecord> records) {
        this.records = records;
    }

    public void add(Color color, Move move) {
        add(new HistoryRecord(color, move));
    }

    public void add(HistoryRecord record) {
        records.add(record);
    }

    public Move getLastMoveOf(Color color) {
        for (int i = records.size() - 1; i >= 0; i--) {
            HistoryRecord record = records.get(i);
            if (record.color == color) {
                return record.move;
            }
        }
        return null;
    }

    @SuppressWarnings("unused")
    public HistoryRecord getLastRecord() {
        return records.isEmpty() ? null : records.get(records.size() - 1);
    }


    public static class HistoryRecord {

        private final Color color;
        private final Move move;

        public HistoryRecord(Color color, Move move) {
            this.color = color;
            this.move = move;
        }

        public Color getColor() {
            return color;
        }

        public Move getMove() {
            return Move.decode(move.command());
        }
    }
}
