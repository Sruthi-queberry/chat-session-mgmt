package io.chatsessionmgmt.service;

import io.chatsessionmgmt.config.UserContext;
import io.chatsessionmgmt.dtos.ChatMessageDTO;
import io.chatsessionmgmt.dtos.ChatSessionDTO;
import io.chatsessionmgmt.dtos.MessageRequest;
import io.chatsessionmgmt.entity.ChatMessage;
import io.chatsessionmgmt.entity.ChatSession;
import io.chatsessionmgmt.exception.ResourceNotFoundException;
import io.chatsessionmgmt.repository.ChatSessionRepository;
import io.chatsessionmgmt.repository.ChatMessageRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ChatServiceImpl implements ChatService {

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Override
    public List<ChatSessionDTO> getChatSessions(int limit){
        Pageable pageable = PageRequest.of(0, limit);
        log.info("Getting chat sessions {}",UserContext.get() );
        return chatSessionRepository.findByUserId(UserContext.get(),pageable).stream().map(ChatSessionDTO::from).toList();
    }

    @Override
    public ChatSessionDTO createSession(String name) {
        ChatSession session = ChatSession.builder().name(name).favorite(false).userId(UserContext.get()).build();
        chatSessionRepository.save(session);
        return ChatSessionDTO.from(session);
    }

    @Override
    public ChatSessionDTO renameSession(UUID sessionId, String newName) {
        ChatSession session = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
        session.setName(newName);
        chatSessionRepository.save(session);
        return ChatSessionDTO.from(session);

    }

    @Override
    public ChatSessionDTO markFavorite(UUID sessionId, boolean favorite) {
        ChatSession session = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
        session.setFavorite(favorite);
        chatSessionRepository.save(session);
        return ChatSessionDTO.from(session);
    }

    @Override
    public void deleteSession(UUID sessionId) {
        ChatSession session = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
        chatSessionRepository.delete(session);
    }

    @Override
    public void addMessage(UUID sessionId, MessageRequest messageRequest) {
        ChatSession session = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
        ChatMessage message = ChatMessage.builder()
                .chatSession(session)
                .sender(messageRequest.getSender())
                .content(messageRequest.getContent())
                .build();
        chatMessageRepository.save(message);
//        return message;
    }

    @Override
    public List<ChatMessageDTO> getMessagesAfter(UUID sessionId, Instant afterTimestamp, int limit) {
        chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
        Instant timestamp = afterTimestamp == null ? Instant.now() : afterTimestamp;
        Pageable pageable = PageRequest.of(0, limit);
        return chatMessageRepository.findMessagesAfter(sessionId, UserContext.get(), timestamp, pageable).stream().map(ChatMessageDTO::from).toList();
    }

    @Override
    public List<ChatMessageDTO> getMessagesBefore(UUID sessionId, Instant beforeTimestamp, int limit) {
        chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
        Instant timestamp = beforeTimestamp == null ? Instant.now() : beforeTimestamp;
        Pageable pageable = PageRequest.of(0, limit);
        return chatMessageRepository.findMessagesBefore(sessionId, timestamp, UserContext.get(),pageable).stream().map(ChatMessageDTO::from).toList();
    }

}
