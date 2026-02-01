package io.chatsessionmgmt.config;


import io.chatsessionmgmt.entity.ChatMessage;
import io.chatsessionmgmt.entity.ChatSession;
import io.chatsessionmgmt.repository.ChatSessionRepository;
import io.chatsessionmgmt.repository.ChatMessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Component
@Profile("dev")
public class DataLoader implements CommandLineRunner {

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;

    public DataLoader(ChatSessionRepository sessionRepo, ChatMessageRepository messageRepo) {
        this.chatSessionRepository = sessionRepo;
        this.chatMessageRepository = messageRepo;
    }

    @Override
    public void run(String... args) throws Exception {

        if(chatSessionRepository.count() == 0) {
            ChatSession session = new ChatSession();
            session.setId(UUID.randomUUID());
            session.setCreatedAt(Instant.now());
            session.setUpdatedAt(Instant.now());
            session.setName("Test Session");
            chatSessionRepository.save(session);
            for (int i = 1; i <= 100; i++) {
                ChatMessage message = new ChatMessage();
                message.setChatSession(session);
                message.setContent("Message " + i);
                message.setSender("User");
                message.setTimestamp(Instant.now().minusSeconds(100 - i));
                chatMessageRepository.save(message);
            }
        }
        log.info("Sample chat session and messages loaded");
    }
}
