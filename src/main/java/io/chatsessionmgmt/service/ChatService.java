package io.chatsessionmgmt.service;

import io.chatsessionmgmt.dtos.ChatMessageDTO;
import io.chatsessionmgmt.dtos.ChatSessionDTO;
import io.chatsessionmgmt.dtos.MessageRequest;
import io.chatsessionmgmt.entity.ChatMessage;
import io.chatsessionmgmt.entity.ChatSession;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ChatService {

    List<ChatSessionDTO> getChatSessions(int limit);

    ChatSessionDTO createSession(String name);

    ChatSessionDTO renameSession(UUID sessionId, String newName);

    ChatSessionDTO markFavorite(UUID sessionId, boolean favorite);

    void deleteSession(UUID sessionId);

    void addMessage(UUID sessionId, MessageRequest messageRequest);

    List<ChatMessageDTO> getMessagesBefore(UUID sessionId, Instant beforeTimestamp, int limit);

    List<ChatMessageDTO> getMessagesAfter(UUID sessionId, Instant afterTimestamp, int limit);
}
