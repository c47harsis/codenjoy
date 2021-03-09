package com.codenjoy.dojo.services;

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

import com.codenjoy.dojo.services.dao.Chat;
import com.codenjoy.dojo.web.controller.Validator;
import com.codenjoy.dojo.web.rest.pojo.PMessage;
import com.codenjoy.dojo.web.rest.pojo.PTopic;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ChatService {

    private final Validator validator;
    private final Chat chat;
    private final TimeService time;

    public List<PTopic> getTopics(String room, int count,
                                  Integer afterId, Integer beforeId,
                                  String playerId)
    {
        validator.checkPlayerInRoom(playerId, room);
        List<Chat.Topic> topics;
        if (afterId != null && beforeId != null) {
            topics = chat.getTopicsBetween(room, afterId, beforeId);
        } else if (afterId != null) {
            topics = chat.getTopicsAfter(room, count, afterId);
        } else if (beforeId != null) {
            topics = chat.getTopicsBefore(room, count, beforeId);
        } else {
            topics = chat.getTopics(room, count);
        }

        return topics.stream()
                .map(PTopic::from)
                .collect(Collectors.toList());
    }

    public PTopic getMessage(int messageId, String room, String playerId) {
        validator.checkPlayerInRoom(playerId, room);

        Chat.Topic topic = chat.getTopicById(messageId);

        if (topic == null || !topic.getChatId().equals(room)) {
            throw new IllegalArgumentException(
                    "There is no message with id: " + messageId +
                            " in room with id: " + room);
        }
        return PTopic.from(topic);
    }

    public PTopic postMessage(String text, String room, String playerId) {
        validator.checkPlayerInRoom(playerId, room);

        Chat.Topic topic = Chat.Topic.builder()
                .chatId(room)
                .playerId(playerId)
                .time(time.now())
                .text(text)
                .build();

        chat.saveTopic(topic);

        return PTopic.from(topic);
    }

    public boolean deleteMessage(int messageId, String room, String playerId) {
        validator.checkPlayerInRoom(playerId, room);

        chat.deleteTopic(messageId);

        return true;
    }

    public List<PMessage> getMessages(int topicId, String room, String playerId) {
        validator.checkPlayerInRoom(playerId, room);

        return chat.getMessagesByTopicId(topicId).stream()
                .map(PMessage::from)
                .collect(Collectors.toList());
    }
}
