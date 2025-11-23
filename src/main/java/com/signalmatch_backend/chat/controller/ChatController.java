package com.signalmatch_backend.chat.controller;

import com.signalmatch_backend.chat.dto.ChatMessageCreateRequest;
import com.signalmatch_backend.chat.dto.ChatMessageResponse;
import com.signalmatch_backend.chat.dto.ChatRoomCreateRequest;
import com.signalmatch_backend.chat.dto.ChatRoomResponse;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats")
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/rooms")
    public ResponseEntity<ApiResponse<ChatRoomResponse>> createOrGetRoom(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody ChatRoomCreateRequest request
    ) {
        User user = userDetails.getUser();

        ChatRoomResponse response = chatService.createOrGetRoom(user, request);

        return ResponseEntity.ok(
                ApiResponse.success("채팅방 생성/조회 성공", response)
        );
    }


    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<ApiResponse<List<ChatMessageResponse>>> getMessages(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long roomId,
            @RequestParam(required = false) Long lastMessageId,
            @RequestParam(defaultValue = "50") int size
    ) {
        Long userId = userDetails.getUser().getUserId();
        UserRole role = userDetails.getUser().getUserRole();

        List<ChatMessageResponse> responses = chatService.getMessages(
                roomId,
                userId,
                role,
                lastMessageId,
                size
        );

        return ResponseEntity.ok(
                ApiResponse.success("메시지 조회 성공", responses)
        );
    }


    @PostMapping("/rooms/{roomId}/messages")
    public ResponseEntity<ApiResponse<ChatMessageResponse>> sendMessage(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long roomId,
            @Valid @RequestBody ChatMessageCreateRequest request
    ) {
        Long senderId = userDetails.getUser().getUserId();
        UserRole role = userDetails.getUser().getUserRole();

        ChatMessageResponse response = chatService.sendMessage(
                roomId,
                senderId,
                role,
                request.content()
        );

        return ResponseEntity.ok(
                ApiResponse.success("메시지 전송 성공", response)
        );
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<ApiResponse<Void>> deleteMessage(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long messageId
    ) {
        Long userId = userDetails.getUser().getUserId();
        UserRole role = userDetails.getUser().getUserRole();

        chatService.deleteMessage(messageId, userId, role);

        return ResponseEntity.ok(
                ApiResponse.success("메시지 삭제 성공")
        );
    }
}
