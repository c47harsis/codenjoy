package com.codenjoy.dojo.chess.engine.service;

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


import com.codenjoy.dojo.chess.engine.level.Level;
import com.codenjoy.dojo.chess.engine.model.Color;
import com.codenjoy.dojo.chess.engine.model.Event;
import com.codenjoy.dojo.chess.engine.model.item.piece.Piece;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Chess implements GameField<Player> {
    private static final Logger LOGGER = LoggerFactory.getLogger(Chess.class);

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final Dice dice;

    private final GameSettings settings;
    private final List<Player> players;
    private final GameBoard board;
    private final GameHistory history;
    private Color currentColor;

    public Chess(Level level, Dice dice, GameSettings settings) {
        this.board = new GameBoard(level);
        this.dice = dice;
        this.settings = settings;
        this.players = Lists.newLinkedList();
        history = new GameHistory();
        this.currentColor = getColors().stream()
                .min(Comparator.comparingInt(Color::getPriority))
                .orElseThrow(() -> new IllegalArgumentException("Level " + level + " is invalid"));
    }

    @Override
    public void tick() {
        Player player = getPlayer(currentColor);
        Move move = player.makeMove();
        if (move == null) {
            LOGGER.warn("Player {} did wrong move", player);
            return;
        }
        checkGameOvers();
        checkWinner();
    }

    private void checkGameOvers() {
        players.stream()
                .filter(p -> !p.isAlive())
                .forEach(p -> p.event(Event.GAME_OVER));
    }


    private void checkWinner() {
        List<Player> alivePlayers = players.stream()
                .filter(Player::isAlive)
                .collect(Collectors.toList());
        if (alivePlayers.size() == 1) {
            alivePlayers.get(0).event(Event.WIN);
        }
    }

    @Override
    public void newGame(Player player) {
        if (players.size() == getColors().size()) {
            LOGGER.warn("Trying to add new player [{}], but the game is already full", player);
            return;
        }

        players.add(player);
        player.newHero(this);
    }

    @Override
    public void remove(Player player) {
        players.remove(player);
    }

    @Override
    public GameSettings settings() {
        return settings;
    }

    @Override
    public BoardReader reader() {
        return new BoardReader() {

            @Override
            public int size() {
                return Chess.this.board.getSize();
            }

            @Override
            public Iterable<? extends Point> elements() {
                ArrayList<ReaderElement> result = Lists.newArrayList();

                List<ReaderElement> squares = board.getSquares().stream()
                        .map(ReaderElement::create)
                        .collect(Collectors.toList());

                List<ReaderElement> barriers = board.getBarriers().stream()
                        .map(ReaderElement::create)
                        .collect(Collectors.toList());

                List<ReaderElement> pieces = board.getPieces().stream()
                        .filter(Piece::isAlive)
                        .map(ReaderElement::create)
                        .collect(Collectors.toList());

                result.addAll(barriers);
                result.addAll(pieces);
                result.addAll(squares);
                return result;
            }
        };
    }

    public Color getCurrentColor() {
        return currentColor;
    }

    public List<Color> getColors() {
        return board.getColors();
    }

    public Color getAvailableColor() {
        List<Color> usedColors = players.stream()
                .map(Player::getColor)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return board.getColors().stream()
                .filter(c -> !usedColors.contains(c))
                .findAny()
                .orElse(null);
    }

    private Player getPlayer(Color color) {
        return players.stream()
                .filter(p -> p.getColor() == color)
                .findAny()
                .orElse(null);
    }

    public GameBoard getBoard() {
        return board;
    }

    public void commitMove(Color color, Move move) {
        history.add(color, move);
        currentColor = nextColor();
    }

    private List<Player> getAlivePlayers() {
        return players.stream()
                .filter(Player::isAlive)
                .collect(Collectors.toList());
    }

    private Color nextColor() {
        List<Player> alivePlayers = getAlivePlayers();
        alivePlayers.sort(Comparator.comparingInt(p -> p.getColor().getPriority()));
        return alivePlayers.stream()
                .map(Player::getColor)
                .filter(color -> color.getPriority() > currentColor.getPriority())
                .findAny()
                .orElse(alivePlayers.get(0).getColor());
    }

    public Move lastMoveOf(Color color) {
        return history.getLastMoveOf(color);
    }
}