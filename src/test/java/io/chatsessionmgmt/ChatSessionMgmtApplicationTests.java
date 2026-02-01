package io.chatsessionmgmt;

import io.chatsessionmgmt.dtos.ChatMessageDTO;
import io.chatsessionmgmt.entity.ChatMessage;
import io.chatsessionmgmt.entity.ChatSession;
import io.chatsessionmgmt.repository.ChatMessageRepository;
import io.chatsessionmgmt.repository.ChatSessionRepository;
import io.chatsessionmgmt.service.ChatService;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@SpringBootTest
class ChatSessionMgmtApplicationTests {

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatSessionRepository chatSessionRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    private UUID sessionId;

    private final Dotenv dotenv = Dotenv.load();

    @BeforeEach
    void setUp() {
        ChatSession session = new ChatSession();
        session.setId(UUID.randomUUID());
        session.setCreatedAt(Instant.now());
        session.setUpdatedAt(Instant.now());
        session.setUserId(dotenv.get("API_KEY_USER1"));
        session.setName("UnitTest Session");
        chatSessionRepository.save(session);
        sessionId = session.getId();

        for (int i = 1; i <= 20; i++) {
            ChatMessage msg = new ChatMessage();
            msg.setChatSession(session);
            msg.setContent("Test message " + i);
            msg.setSender("User");
            msg.setTimestamp(Instant.now().minusSeconds(20 - i));
            chatMessageRepository.save(msg);
        }
    }

    @Test
    void testGetMessagesBefore_WithLimit() {
        int limit = 5;
        Instant cursor = Instant.now();
        List<ChatMessageDTO> response = chatService.getMessagesBefore(sessionId, cursor, limit);

        Assertions.assertEquals(limit, response.size());
        log.info(response.toString());
    }

    @Test
    void testGetMessagesBefore_NoCursor() {
        int limit = 5;
        List<ChatMessageDTO> response = chatService.getMessagesBefore(sessionId, null, limit);

        Assertions.assertEquals(limit, response.size());
        log.info(response.toString());
    }
}
