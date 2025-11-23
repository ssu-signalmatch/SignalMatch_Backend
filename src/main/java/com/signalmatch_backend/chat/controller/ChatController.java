package com.signalmatch_backend.chat.controller;

import com.signalmatch_backend.chat.dto.ChatMessageRequest;
import com.signalmatch_backend.chat.dto.ChatMessageResponse;
import com.signalmatch_backend.chat.service.ChatService;
import com.signalmatch_backend.common.domain.ApiResponse;
import com.signalmatch_backend.user.domain.User;
import com.signalmatch_backend.user.domain.enums.UserRole;
import com.signalmatch_backend.user.jwt.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats")
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/startups/{startupId}/investors/{investorId}/messages")
    public ResponseEntity<ApiResponse<ChatMessageResponse>> sendMessage(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long startupId,
            @PathVariable Long investorId,
            @Valid @RequestBody ChatMessageRequest request
    ) {
        User user = customUserDetails.getUser();
        Long userId = user.getUserId();
        UserRole userRole = user.getUserRole(); // STARTUP or INVESTOR

        ChatMessageResponse response = chatService.sendMessage(
                startupId,
                investorId,
                userId,
                userRole,
                request.content()
        );

        return ResponseEntity.ok(
                ApiResponse.success("메시지가 전송되었습니다.", response)
        );
    }


    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<ApiResponse<Void>> deleteMessage(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long messageId
    ) {
        User user = customUserDetails.getUser();
        Long userId = user.getUserId();
        UserRole userRole = user.getUserRole();

        chatService.deleteMessage(messageId, userId, userRole);

        return ResponseEntity.ok(
                ApiResponse.success("메시지가 삭제되었습니다.")
        );
    }

}
