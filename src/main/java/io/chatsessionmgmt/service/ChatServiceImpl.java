package io.chatsessionmgmt.service;

import io.chatsessionmgmt.dtos.ChatMessageDTO;
import io.chatsessionmgmt.dtos.MessageRequest;
import io.chatsessionmgmt.entity.ChatMessage;
import io.chatsessionmgmt.entity.ChatSession;
import io.chatsessionmgmt.exception.ResourceNotFoundException;
import io.chatsessionmgmt.repository.ChatSessionRepository;
import io.chatsessionmgmt.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatServiceImpl implements ChatService {

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Override
    public List<ChatSession> getChatSessions(int limit){
        Pageable pageable = PageRequest.of(0, limit);
        return chatSessionRepository.findAll(pageable).getContent();
    }

    @Override
    public ChatSession createSession(String name) {
        ChatSession session = ChatSession.builder().name(name).favorite(false).build();
        return chatSessionRepository.save(session);
    }

    @Override
    public ChatSession renameSession(UUID sessionId, String newName) {
        ChatSession session = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
        session.setName(newName);
        return chatSessionRepository.save(session);
    }

    @Override
    public ChatSession markFavorite(UUID sessionId, boolean favorite) {
        ChatSession session = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
        session.setFavorite(favorite);
        return chatSessionRepository.save(session);
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
        return chatMessageRepository.findMessagesAfter(sessionId, timestamp, pageable).stream().map(ChatMessageDTO::from).toList();
    }

    @Override
    public List<ChatMessageDTO> getMessagesBefore(UUID sessionId, Instant beforeTimestamp, int limit) {
        chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
        Instant timestamp = beforeTimestamp == null ? Instant.now() : beforeTimestamp;
        Pageable pageable = PageRequest.of(0, limit);
        return chatMessageRepository.findMessagesBefore(sessionId, timestamp, pageable).stream().map(ChatMessageDTO::from).toList();
    }
}
