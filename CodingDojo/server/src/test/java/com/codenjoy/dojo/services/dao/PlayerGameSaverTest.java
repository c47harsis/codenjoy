package com.codenjoy.dojo.services.dao;

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


import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.jdbc.SqliteConnectionThreadPoolFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayerGameSaverTest {

    private PlayerGameSaver saver;

    @Before
    public void removeAll() {
        String dbFile = "target/saves.db" + new Random().nextInt();
        saver = new PlayerGameSaver(
                new SqliteConnectionThreadPoolFactory(false, dbFile,
                        new ContextPathGetter() {
                            @Override
                            public String getContext() {
                                return "context";
                            }
                        }));
    }

    @After
    public void cleanUp() {
        saver.removeDatabase();
    }

    @Test
    public void shouldSaveLoadPlayerGame() {
        // given
        PlayerScores scores = getScores(10);
        Information info = getInfo("Some info");
        GameService gameService = getGameService(scores);
        Player player = new Player("vasia", "http://127.0.0.1:8888", PlayerTest.mockGameType("game"), scores, info);
        player.setRoom("room");

        // when
        long now = System.currentTimeMillis();
        saver.saveGame(player, "{'key':'value'}", now);

        PlayerSave loaded = saver.loadGame("vasia");
        
        // then
        assertEqualsProperties(player, loaded);
        assertEquals("{'key':'value'}", loaded.getSave());

        // when
        saver.delete("vasia");

        // then
        assertEquals("[]", saver.getSavedList().toString());
    }

    private GameType getGameType(PlayerScores scores) {
        GameType gameType = mock(GameType.class);
        when(gameType.getPlayerScores(anyInt(), any())).thenReturn(scores);
        return gameType;
    }

    private GameService getGameService(PlayerScores scores) {
        GameService gameService = mock(GameService.class);
        GameType gameType = getGameType(scores);
        when(gameService.getGameType(anyString())).thenReturn(gameType);
        when(gameService.getGameType(anyString(), anyString())).thenReturn(gameType);
        when(gameService.exists(anyString())).thenReturn(true);
        return gameService;
    }

    private Information getInfo(String string) {
        Information info = mock(Information.class);
        when(info.getMessage()).thenReturn(string);
        return info;
    }

    private PlayerScores getScores(int value) {
        PlayerScores scores = mock(PlayerScores.class);
        when(scores.getScore()).thenReturn(value);
        return scores;
    }

    private void assertEqualsProperties(Player expected, PlayerSave actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getCallbackUrl(), actual.getCallbackUrl());
        assertEquals(expected.getScore().toString(), actual.getScore().toString());
        assertEquals(expected.getRoom(), actual.getRoom());
    }

    @Test
    public void shouldGetSavedList() {
        // given
        Player player1 = new Player("vasia", "http://127.0.0.1:8888", PlayerTest.mockGameType("game"), getScores(10), getInfo("Some other info"));
        Player player2 = new Player("katia", "http://127.0.0.3:7777", PlayerTest.mockGameType("game"), getScores(20), getInfo("Some info"));
        player1.setRoom("room");
        player2.setRoom("room");

        // when
        long now = System.currentTimeMillis();

        saver.saveGame(player1, "{'key':'value'}", now);
        saver.saveGame(player2, "{'key':'value'}", now);

        // then
        assertEquals("[vasia, katia]", saver.getSavedList().toString());
    }

    @Test
    public void shouldGetSavedList_caseInRoom() {
        // given
        Player player1 = new Player("vasia", "http://127.0.0.1:8888", PlayerTest.mockGameType("game"), getScores(10), getInfo("Some other info"));
        Player player2 = new Player("katia", "http://127.0.0.3:7777", PlayerTest.mockGameType("game"), getScores(20), getInfo("Some info"));
        Player player3 = new Player("maria", "http://127.0.0.5:9999", PlayerTest.mockGameType("game"), getScores(30), getInfo("Some another info"));
        player1.setRoom("room");
        player2.setRoom("room");
        player3.setRoom("otherRoom");

        // when
        long now = System.currentTimeMillis();

        saver.saveGame(player1, "{'key':'value'}", now);
        saver.saveGame(player2, "{'key':'value'}", now);
        saver.saveGame(player3, "{'key':'value'}", now);

        // then
        assertEquals("[vasia, katia]", saver.getSavedList("room").toString());

        // then
        assertEquals("[maria]", saver.getSavedList("otherRoom").toString());
    }
}
