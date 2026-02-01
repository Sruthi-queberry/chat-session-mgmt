package io.chatsessionmgmt.dtos;

import io.chatsessionmgmt.entity.ChatSession;

import java.time.Instant;
import java.util.UUID;

public record ChatSessionDTO(UUID id, String name, boolean favorite, Instant createdAt) {
    public static ChatSessionDTO from(ChatSession entity) {
        return new ChatSessionDTO(entity.getId(), entity.getName(), entity.isFavorite(), entity.getCreatedAt());
    }
}