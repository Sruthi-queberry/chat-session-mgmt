package io.chatsessionmgmt.repository;

import io.chatsessionmgmt.entity.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {

    // Fetch messages after a cursor (for new messages / infinite scroll)
    @Query("""
        select c from ChatMessage c
        where c.chatSession.id = :sessionId
        and c.timestamp > :afterTimestamp
        order by c.timestamp asc
    """)
    List<ChatMessage> findMessagesAfter(
            @Param("sessionId") UUID sessionId,
            @Param("afterTimestamp") Instant afterTimestamp,
            Pageable pageable
    );

    // For scrolling up older messages
    @Query("""
        select c from ChatMessage c
        where c.chatSession.id = :sessionId
        and c.timestamp < :beforeTimestamp
        order by c.timestamp desc
    """)
    List<ChatMessage> findMessagesBefore(
            @Param("sessionId") UUID sessionId,
            @Param("beforeTimestamp") Instant beforeTimestamp,
            Pageable pageable
    );
}
