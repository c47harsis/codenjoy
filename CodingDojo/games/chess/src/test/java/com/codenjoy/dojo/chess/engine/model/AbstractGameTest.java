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

import com.codenjoy.dojo.chess.engine.service.Chess;
import com.codenjoy.dojo.chess.engine.service.Player;
import com.codenjoy.dojo.chess.engine.level.Level;
import com.codenjoy.dojo.chess.engine.model.item.piece.Piece;
import com.codenjoy.dojo.chess.engine.service.GameSettings;
import com.codenjoy.dojo.chess.engine.service.Move;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Before;
import org.mockito.ArgumentCaptor;
import org.mockito.stubbing.OngoingStubbing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SuppressWarnings({"rawtypes", "unused", "unchecked", "SpellCheckingInspection"})
public abstract class AbstractGameTest {
    private final Map<Color, Player> players = new HashMap<>();

    private final Map<Player, EventListener> listeners = new HashMap<>();

    private String board;

    protected Chess game;
    protected Dice dice;
    protected PrinterFactory printerFactory;
    protected GameSettings settings;

    protected static String classicBoard() {
        return "rkbqwbkr" +
                "pppppppp" +
                "........" +
                "........" +
                "........" +
                "........" +
                "PPPPPPPP" +
                "RKBQWBKR";
    }

    @Before
    public void setup() {
        dice = mock(Dice.class);
        printerFactory = new PrinterFactoryImpl();
        settings = new GameSettings();
    }

    protected void reset() {
        setup();
        givenFl(board);
    }

    protected void dice(int... ints) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i : ints) {
            when = when.thenReturn(i);
        }
    }

    public void neverFired(Color color, Event event) {
        EventListener eventListener = listeners.get(players.get(color));
        verify(eventListener, never()).event(event);
    }

    public void neverFired(Event event) {
        for (Map.Entry<Player, EventListener> entry: listeners.entrySet()) {
            try {
                verify(entry.getValue(), never()).event(event);
            } catch (Throwable ex) {
                System.err.println("Color: " + entry.getKey().getColor());
                throw ex;
            }
        }
    }

    public void fired(Color color, int times, Event event) {
        EventListener eventListener = listeners.get(players.get(color));
        verify(eventListener, times(times)).event(event);
    }

    public void fired(Color color, Event event) {
        fired(color, 1, event);
    }

    public void fired(Color color, Event... events) {
        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        EventListener eventListener = listeners.get(players.get(color));
        verify(eventListener, times(events.length)).event(captor.capture());
        assertEquals(Arrays.asList(events), captor.getAllValues());
    }

    protected void givenFl(String board) {
        this.board = board;
        players.clear();
        listeners.clear();
        Level level = new Level(board);
        game = new Chess(level, dice, settings);
        for (int i = 0; i < level.presentedColors().size(); i++) {
            EventListener listener = mock(EventListener.class);
            Player player = new Player(listener, settings);
            game.newGame(player);
            players.put(player.getColor(), player);
            listeners.put(player, listener);
        }
    }

    protected void assertE(String expected) {
        assertE(expected, Color.WHITE);
    }

    protected void assertE(String expected, Color playerColor) {
        assertEquals(TestUtils.injectN(expected), printerFactory.getPrinter(
                game.reader(), players.get(playerColor)).print());
    }

    protected void move(Color color, Move move) {
        players.get(color).getHero().act(move.command());
        game.tick();
    }

    protected Piece getPieceAt(int x, int y) {
        return getPieceAt(new PointImpl(x, y));
    }

    protected Piece getPieceAt(Point position) {
        return game.getBoard().getPieces().stream()
                .filter(p -> p.getPosition().equals(position))
                .findFirst()
                .orElse(null);
    }

    protected int getBoardSize() {
        return game.getBoard().getSize();
    }
}
