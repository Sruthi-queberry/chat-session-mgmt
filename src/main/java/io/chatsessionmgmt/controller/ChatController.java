package io.chatsessionmgmt.controller;

import io.chatsessionmgmt.dtos.ChatMessageDTO;
import io.chatsessionmgmt.dtos.MessageRequest;
import io.chatsessionmgmt.entity.ChatMessage;
import io.chatsessionmgmt.entity.ChatSession;
import io.chatsessionmgmt.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/sessions")
    public ResponseEntity<List<ChatSession>> getChatMessages(@RequestParam(defaultValue = "10") int limit){
        return ResponseEntity.ok(chatService.getChatSessions(limit));
    }

    @PostMapping("/session")
    public ResponseEntity<ChatSession> createSession(@RequestParam String name) {
        return ResponseEntity.ok(chatService.createSession(name));
    }

    @PutMapping("/session/{id}/rename")
    public ResponseEntity<ChatSession> renameSession(@PathVariable UUID id, @RequestParam String name) {
        return ResponseEntity.ok(chatService.renameSession(id, name));
    }

    @PutMapping("/session/{id}/favorite")
    public ResponseEntity<ChatSession> markFavorite(@PathVariable UUID id, @RequestParam boolean favorite) {
        return ResponseEntity.ok(chatService.markFavorite(id, favorite));
    }

    @DeleteMapping("/session/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable UUID id) {
        chatService.deleteSession(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/session/{id}/message/add")
    public ResponseEntity<String> addMessage(@PathVariable UUID id, @RequestBody MessageRequest messageRequest
                                             ) {
        chatService.addMessage(id, messageRequest); // as we are using lazy loading just sending a success message
        return ResponseEntity.ok("Message added");
    }

    @GetMapping("/session/{sessionId}/messages/after")
    public List<ChatMessageDTO> getMessagesAfter(
            @PathVariable UUID sessionId,
            @RequestParam(required = false) Instant after,
            @RequestParam(defaultValue = "50") int limit
    ) {
        return chatService.getMessagesAfter(sessionId, after, limit);
    }

    @GetMapping("/session/{sessionId}/messages/before")
    public List<ChatMessageDTO> getMessagesBefore(
            @PathVariable UUID sessionId,
            @RequestParam(required = false) Instant before,
            @RequestParam(defaultValue = "50") int limit
    ) {
        return chatService.getMessagesBefore(sessionId, before, limit);
    }
}
