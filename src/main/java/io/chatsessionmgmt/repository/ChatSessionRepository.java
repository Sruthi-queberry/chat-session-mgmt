package io.chatsessionmgmt.repository;

import io.chatsessionmgmt.dtos.ChatSessionDTO;
import io.chatsessionmgmt.entity.ChatSession;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ChatSessionRepository extends JpaRepository<ChatSession, UUID> {
    List<ChatSession> findByUserId(String userId, Pageable pageable);
}
