package io.chatsessionmgmt.dtos;

import io.chatsessionmgmt.entity.ChatMessage;

import java.time.Instant;
import java.util.UUID;

public record ChatMessageDTO(UUID id, String content, String sender, Instant timestamp) {
    public static ChatMessageDTO from(ChatMessage entity) {
        return new ChatMessageDTO(entity.getId(), entity.getContent(), entity.getSender(), entity.getTimestamp());
    }
}
