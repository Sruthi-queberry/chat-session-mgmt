package io.chatsessionmgmt.service;

import io.chatsessionmgmt.dtos.ChatMessageDTO;
import io.chatsessionmgmt.dtos.MessageRequest;
import io.chatsessionmgmt.entity.ChatMessage;
import io.chatsessionmgmt.entity.ChatSession;
import org.springframework.data.domain.Page;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ChatService {

    List<ChatSession> getChatSessions(int limit);

    ChatSession createSession(String name);

    ChatSession renameSession(UUID sessionId, String newName);

    ChatSession markFavorite(UUID sessionId, boolean favorite);

    void deleteSession(UUID sessionId);

    void addMessage(UUID sessionId, MessageRequest messageRequest);

    List<ChatMessageDTO> getMessagesBefore(UUID sessionId, Instant beforeTimestamp, int limit);

    List<ChatMessageDTO> getMessagesAfter(UUID sessionId, Instant afterTimestamp, int limit);
}
