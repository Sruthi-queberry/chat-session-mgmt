package io.chatsessionmgmt.dtos;

import lombok.Data;

@Data
public class MessageRequest {
    private String sender;
    private String content;
}
