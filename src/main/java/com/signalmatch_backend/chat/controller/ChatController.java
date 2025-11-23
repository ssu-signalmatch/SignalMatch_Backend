package com.signalmatch_backend.chat.controller;

import com.signalmatch_backend.chat.dto.ChatMessageRequest;
import com.signalmatch_backend.chat.dto.ChatMessageResponse;
import com.signalmatch_backend.chat.service.ChatService;
import com.signalmatch_backend.common.domain.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats")
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/startups/{startupId}/investors/{investorId}/messages")
    public ResponseEntity<ApiResponse<ChatMessageResponse>> sendMessage(
            @PathVariable Long startupId,
            @PathVariable Long investorId,
            @RequestBody ChatMessageRequest request
    ) {
        ChatMessageResponse response = chatService.sendMessage(startupId, investorId, request);
        return ResponseEntity.ok(ApiResponse.success("메시지가 전송되었습니다.",response));
    }

}
