package com.codenjoy.dojo.services.dao;

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

import com.codenjoy.dojo.services.jdbc.ConnectionThreadPoolFactory;
import com.codenjoy.dojo.services.jdbc.CrudPrimaryKeyConnectionThreadPool;
import com.codenjoy.dojo.services.jdbc.JDBCTimeUtils;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Chat {
    private final CrudPrimaryKeyConnectionThreadPool pool;

    public Chat(ConnectionThreadPoolFactory factory) {
        pool = (CrudPrimaryKeyConnectionThreadPool) factory.create(
                "CREATE TABLE IF NOT EXISTS topics (" +
                        "id integer_primary_key, " +
                        "chat_id varchar(255), " +
                        "player_id varchar(255), " +
                        "time varchar(255), " +
                        "text varchar(255));",

                "CREATE TABLE IF NOT EXISTS messages (" +
                        "id integer_primary_key, " +
                        "topic_id integer, " +
                        "player_id varchar(255), " +
                        "time varchar(255), " +
                        "text varchar(255));"
        );
    }

    public void removeDatabase() {
        pool.removeDatabase();
    }

    /**
     * @return {@param count} последних сообщений
     *        для текущего чата {@param chatId},
     *        посортированных в порядке возрастания времени
     */
    public List<Topic> getTopics(String chatId, int count) {
        return pool.select("SELECT * FROM " +
                        "(SELECT * FROM topics " +
                        "WHERE chat_id = ? " +
                        "ORDER BY time DESC " +
                        "LIMIT ?)" +
                        "ORDER BY time ASC;",
                new Object[]{chatId, count},
                Chat::parseTopics
        );
    }

    /**
     * @return все сообщения в диапазоне ({@param afterId}...{@param beforeId})
     *        для текущего чата {@param chatId},
     *        посортированных в порядке возрастания времени.
     */
    public List<Topic> getTopicsBetween(String chatId, int afterId, int beforeId) {
        if (afterId > beforeId) {
            throw new IllegalArgumentException("afterId in interval should be smaller than beforeId");
        }
        return pool.select("SELECT * FROM topics " +
                        "WHERE chat_id = ? AND id > ? AND id < ?" +
                        "ORDER BY time ASC;",
                new Object[]{chatId, afterId, beforeId},
                Chat::parseTopics
        );
    }

    /**
     * @return {@param count} первых сообщений начиная с {@param afterId} (но не включая его)
     *        для текущего чата {@param chatId},
     *        посортированных в порядке возрастания времени.
     */
    public List<Topic> getTopicsAfter(String chatId, int count, int afterId) {
        return pool.select("SELECT * FROM topics " +
                        "WHERE chat_id = ? AND id > ?" +
                        "ORDER BY time ASC " +
                        "LIMIT ?;",
                new Object[]{chatId, afterId, count},
                Chat::parseTopics
        );
    }

    /**
     * @return {@param count} последних сообщений перед {@param beforeId} (но не включая его)
     *        для текущего чата {@param chatId},
     *        посортированных в порядке возрастания времени.
     */
    public List<Topic> getTopicsBefore(String chatId, int count, int beforeId) {
        return pool.select("SELECT * FROM " +
                        "(SELECT * FROM topics " +
                        "WHERE chat_id = ? AND id < ?" +
                        "ORDER BY time DESC " +
                        "LIMIT ?) " +
                        "ORDER BY time ASC;",
                new Object[]{chatId, beforeId, count},
                Chat::parseTopics
        );
    }

    public Topic getTopicById(int id) {
        return pool.select("SELECT * FROM topics WHERE id = ?",
                new Object[]{id},
                rs -> rs.next() ? new Topic(rs) : null
        );
    }

    public synchronized Topic saveTopic(Topic topic) {
        // synchronized тут потому что создание записи и получение новой id
        // созданной записи должны быть одной атомарной операцией
        pool.update("INSERT INTO topics " +
                        "(chat_id, player_id, time, text) " +
                        "VALUES (?, ?, ?, ?);",
                new Object[]{
                        topic.getChatId(),
                        topic.getPlayerId(),
                        JDBCTimeUtils.toString(new Date(topic.getTime())),
                        topic.getText()
                }
        );
        topic.setId(pool.lastInsertId("topics", "id"));
        return topic;
    }

    public void deleteTopic(int id) {
        pool.update("DELETE FROM topics WHERE id = ?", new Object[]{id});
    }

    private static List<Topic> parseTopics(ResultSet rs) throws SQLException {
        List<Topic> topics = new ArrayList<>();
        while (rs.next()) {
            topics.add(new Topic(rs));
        }
        return topics;
    }

    public List<Message> getMessagesByTopicId(int topicId) {
        return pool.select("SELECT * FROM messages " +
                "WHERE topic_id = ? " +
                "ORDER BY time ASC;",
                new Object[]{topicId},
                Chat::parseMessages);
    }

    private static List<Message> parseMessages(ResultSet rs) throws SQLException {
        List<Message> result = new ArrayList<>();
        while (rs.next()) {
            result.add(new Message(rs));
        }
        return result;
    }

    public void removeAll() {
        pool.clearLastInsertedId("topics", "id");
        pool.update("DELETE FROM topics");
        pool.clearLastInsertedId("messages", "id");
        pool.update("DELETE FROM messages");
    }

    @Data
    public static class Topic {
        private Integer id;
        private String chatId;
        private String playerId;
        private long time;
        private String text;

        @Builder
        public Topic(String chatId, String playerId, long time, String text) {
            this.chatId = chatId;
            this.playerId = playerId;
            this.time = time;
            this.text = text;
        }

        private Topic(ResultSet rs) throws SQLException {
            this.id = rs.getInt("id");
            this.chatId = rs.getString("chat_id");
            this.playerId = rs.getString("player_id");
            this.time = JDBCTimeUtils.getTimeLong(rs);
            this.text = rs.getString("text");
        }
    }

    @Data
    public static class Message {
        private Integer id;
        private Integer topicId;
        private String playerId;
        private long time;
        private String text;

        @Builder
        public Message(Integer topicId, String playerId, long time, String text) {
            this.topicId = topicId;
            this.playerId = playerId;
            this.time = time;
            this.text = text;
        }

        private Message(ResultSet rs) throws SQLException {
            this.id = rs.getInt("id");
            this.topicId = rs.getInt("topic_id");
            this.playerId = rs.getString("player_id");
            this.time = JDBCTimeUtils.getTimeLong(rs);
            this.text = rs.getString("text");
        }
    }
}
