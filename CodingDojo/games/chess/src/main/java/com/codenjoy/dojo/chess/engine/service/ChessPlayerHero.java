package com.codenjoy.dojo.chess.engine.service;

import com.codenjoy.dojo.chess.engine.model.Color;
import com.codenjoy.dojo.chess.engine.model.Event;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChessPlayerHero extends PlayerHero<Chess> implements NoDirectionsJoystick {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChessPlayerHero.class);

    private final Color color;

    private final List<Event> events = new ArrayList<>();
    private Move lastMove;
    private Move action;

    public ChessPlayerHero(Color color, Chess field) {
        this.color = color;
        this.field = field;
    }

    @Override
    public void act(int... codes) {
        if (field.getCurrentColor() != getColor()) {
            LOGGER.warn(
                    "Player with color {} tried to send an act command when active color was {}",
                    getColor(), field.getCurrentColor()
            );
            return;
        }
        action = Move.decode(codes);
        if (action == null) {
            LOGGER.warn(
                    "Hero with color {} received invalid action parameters: {}",
                    getColor(), Arrays.toString(codes)
            );
        }
    }

    private void clearBeforeTick() {
        events.clear();
        lastMove = null;
    }

    @Override
    public void tick() {
        clearBeforeTick();
        if (color != field.getCurrentColor()) {
            LOGGER.warn("{} player trying to make a move, but current player is {}", color, field.getCurrentColor());
        }
        if (field.getBoard().tryMove(color, action)) {
            field.commitMove(color, action);
            lastMove = action;
        } else {
            events.add(Event.WRONG_MOVE);
        }
    }

    public Color getColor() {
        return color;
    }

    public List<Event> getEvents() {
        return events;
    }

    public Move getLastMove() {
        return lastMove;
    }

    public boolean isAlive() {
        return field.getBoard().isAlive(color);
    }

    public boolean isWinner() {
        return field.getBoard().isWinner(color);
    }
}
