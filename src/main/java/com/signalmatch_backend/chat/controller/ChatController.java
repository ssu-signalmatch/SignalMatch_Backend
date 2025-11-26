package com.signalmatch_backend.chat.controller;

import com.signalmatch_backend.chat.dto.*;
import com.signalmatch_backend.chat.service.ChatService;
import com.signalmatch_backend.common.domain.ApiResponse;
import com.signalmatch_backend.user.domain.User;
import com.signalmatch_backend.user.domain.enums.UserRole;
import com.signalmatch_backend.user.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "채팅방 생성",description = "투자자와 스타트업간 채팅방을 생성합니다.")
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
    @Operation(summary = "메시지 생성",description = "투자자와 스타트업간 메시지를 전송합니다.")
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
    @Operation(summary = "메시지 조회",description = "투자자와 스타트업간 메시지를 조회합니다.")
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
    @Operation(summary = "메시지 삭제",description = "투자자와 스타트업간 메시지를 삭제합니다.")
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

    @GetMapping
    @Operation(summary = "채팅방 목록 조회", description = "현재 로그인한 사용자의 전체 채팅방 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<ChatRoomListResponse>>> getChatRooms(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUser().getUserId();
        UserRole role = userDetails.getUser().getUserRole();

        List<ChatRoomListResponse> responses = chatService.getChatRooms(userId, role);

        return ResponseEntity.ok(
                ApiResponse.success("채팅방 목록 조회 성공", responses)
        );
    }

}
