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


import com.codenjoy.dojo.chess.engine.model.Color;
import com.codenjoy.dojo.chess.engine.model.Event;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;

public class Player extends GamePlayer<ChessPlayerHero, Chess> {
    private ChessPlayerHero hero;

    public Player(EventListener listener, GameSettings settings) {
        super(listener, settings);
    }

    @Override
    public ChessPlayerHero getHero() {
        return hero;
    }

    @Override
    public void newHero(Chess game) {
        Color color = game.getAvailableColor();
        this.hero = new ChessPlayerHero(color, game);
    }

    @Override
    public boolean isAlive() {
        return hero.isAlive();
    }

    @Override
    public boolean isWin() {
        return hero.isWinner();
    }

    @Override
    public void event(Object eventObj) {
        super.event(eventObj);
        Event event = (Event) eventObj;
        switch (event) {
            case WIN:
            case GAME_OVER:
                break;
        }
    }

    public Color getColor() {
        return hero == null ? null : hero.getColor();
    }

    public Move makeMove() {
        hero.tick();
        hero.getEvents().forEach(this::event);
        return hero.getLastMove();
    }
}
