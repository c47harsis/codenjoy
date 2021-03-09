package com.codenjoy.dojo.web.rest.pojo;

import com.codenjoy.dojo.services.dao.Chat;
import lombok.Data;

@Data
public class PMessage {
    private final int id;
    private final int topicId;
    private final String playerId;
    private final long time;
    private final String text;

    public static PMessage from(Chat.Message message) {
        return new PMessage(
                message.getId(),
                message.getTopicId(),
                message.getPlayerId(),
                message.getTime(),
                message.getText()
        );
    }
}
