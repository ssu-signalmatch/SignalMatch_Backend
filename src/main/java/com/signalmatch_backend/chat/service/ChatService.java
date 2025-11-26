package com.signalmatch_backend.chat.service;


import com.signalmatch_backend.chat.ChatMessageFinder;
import com.signalmatch_backend.chat.ChatRoomFinder;
import com.signalmatch_backend.chat.domain.ChatMessage;
import com.signalmatch_backend.chat.domain.ChatRoom;
import com.signalmatch_backend.chat.dto.ChatMessageResponse;
import com.signalmatch_backend.chat.dto.ChatRoomCreateRequest;
import com.signalmatch_backend.chat.dto.ChatRoomListResponse;
import com.signalmatch_backend.chat.dto.ChatRoomResponse;
import com.signalmatch_backend.chat.repository.ChatMessageRepository;
import com.signalmatch_backend.chat.repository.ChatRoomRepository;
import com.signalmatch_backend.common.exception.CustomException;
import com.signalmatch_backend.common.exception.ErrorCode;
import com.signalmatch_backend.document.Service.DocumentService;
import com.signalmatch_backend.investor.InvestorFinder;
import com.signalmatch_backend.startup.StartupFinder;
import com.signalmatch_backend.user.domain.User;
import com.signalmatch_backend.user.domain.enums.UserRole;
import com.signalmatch_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomFinder chatRoomFinder;
    private final ChatMessageFinder chatMessageFinder;
    private final StartupFinder startupFinder;
    private final InvestorFinder investorFinder;
    private final DocumentService documentService;
    private final UserRepository userRepository;

    @Transactional
    public ChatRoomResponse createOrGetRoom(User user, ChatRoomCreateRequest request) {

        UserRole role = user.getUserRole();

        Long startupId;
        Long investorId;

        if (role == UserRole.STARTUP) {
            if (request.investorId() == null) {
                throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
            }
            Long myStartupId = startupFinder.findByOwner(user).getStartupId();
            startupId = myStartupId;
            investorId = request.investorId();
        } else if (role == UserRole.INVESTOR) {
            if (request.startupId() == null) {
                throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
            }
            Long myInvestorId = investorFinder.findByOwner(user).getInvestorId();
            startupId = request.startupId();
            investorId = myInvestorId;
        } else {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        ChatRoom room = chatRoomRepository
                .findByStartupIdAndInvestorId(startupId, investorId)
                .orElseGet(() -> chatRoomRepository.save(
                        new ChatRoom(startupId, investorId)
                ));

        return new ChatRoomResponse(room.getId(), room.getStartupId(), room.getInvestorId());
    }

    //메시지 전송
    @Transactional
    public ChatMessageResponse sendMessage(Long roomId, Long senderId, UserRole senderRole, String content) {

        ChatRoom room = chatRoomFinder.findById(roomId);

        validateParticipant(room, senderId, senderRole);

        ChatMessage message = new ChatMessage(
                room,
                senderRole,
                senderId,
                content
        );

        return ChatMessageResponse.from(chatMessageRepository.save(message));
    }



    private void validateSender(ChatRoom room, UserRole role, Long senderId) {
        if (role == UserRole.STARTUP && !room.getStartupId().equals(senderId)) {
            throw new CustomException(ErrorCode.CHAT_SENDER_INVALID);
        }

        if (role == UserRole.INVESTOR && !room.getInvestorId().equals(senderId)) {
            throw new CustomException(ErrorCode.CHAT_SENDER_INVALID);
        }
    }

    @Transactional
    public List<ChatMessageResponse> getMessages(
            Long roomId,
            Long requesterId,
            UserRole requesterRole,
            Long lastMessageId,
            int size
    ) {
        ChatRoom room = chatRoomFinder.findById(roomId);

        validateParticipant(room, requesterId, requesterRole);

        List<ChatMessage> messages;

        if (lastMessageId != null) {
            messages = chatMessageRepository
                    .findByChatRoomIdAndIdGreaterThanAndDeletedFalseOrderByIdAsc(roomId, lastMessageId);
        } else {
            messages = chatMessageRepository
                    .findByChatRoomIdAndDeletedFalseOrderByIdAsc(
                            roomId,
                            PageRequest.of(0, size)
                    );
        }

        for (ChatMessage message : messages) {
            if (!message.getSenderId().equals(requesterId)) {
                message.markRead(requesterRole);
            }
        }

        return messages.stream()
                .map(ChatMessageResponse::from)
                .toList();
    }

    @Transactional
    public void deleteMessage(Long messageId, Long requesterId, UserRole requesterRole) {

        ChatMessage message = chatMessageFinder.findById(messageId);

        if (!message.getSenderId().equals(requesterId)
                || !message.getSenderRole().equals(requesterRole)) {
            throw new CustomException(ErrorCode.CHAT_SENDER_INVALID);
        }

        message.softDelete();
    }

    /**
     * 이 유저가 방에 참여한 사람인지 검증
     */
    private void validateParticipant(ChatRoom room, Long userId, UserRole role) {

        if (role == UserRole.STARTUP && !room.getStartupId().equals(userId)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        if (role == UserRole.INVESTOR && !room.getInvestorId().equals(userId)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
    }

    public List<ChatRoomListResponse> getChatRooms(Long userId, UserRole role) {

        // 1) 현재 유저가 참여한 모든 채팅방 조회
        List<ChatRoom> rooms = chatRoomRepository.findAllByUser(userId, role.name());

        if (rooms.isEmpty()) {
            return List.of();
        }

        // 2) 상대방 userId 목록 추출
        List<Long> opponentIds = rooms.stream()
                .map(room -> role == UserRole.STARTUP ? room.getInvestorId() : room.getStartupId())
                .distinct()
                .toList();

        // 3) 상대 유저 정보 한 번에 조회 후 Map으로 보관 (id -> User)
        Map<Long, User> opponentMap = userRepository.findAllById(opponentIds)
                .stream()
                .collect(Collectors.toMap(User::getUserId, u -> u));

        List<ChatRoomListResponse> result = new ArrayList<>();

        for (ChatRoom room : rooms) {
            Long opponentId = (role == UserRole.STARTUP)
                    ? room.getInvestorId()
                    : room.getStartupId();

            User opponent = opponentMap.get(opponentId);
            if (opponent == null) {
                // 혹시라도 user가 삭제되어 없는 경우
                throw new CustomException(ErrorCode.USER_NOT_FOUND);
            }

            // 4) 마지막 메시지 조회
            Optional<ChatMessage> lastOpt =
                    chatMessageRepository.findTopByChatRoomIdAndDeletedFalseOrderByIdDesc(room.getId());

            String lastContent = lastOpt.map(ChatMessage::getContent).orElse(null);
            LocalDateTime lastTime = lastOpt.map(ChatMessage::getCreatedAt).orElse(null);

            // 5) 안 읽은 메시지 개수 계산 (상대가 보낸 & 내가 아직 안 읽은 것만)
            long unreadCount;
            if (role == UserRole.STARTUP) {
                unreadCount = chatMessageRepository
                        .countByChatRoomIdAndDeletedFalseAndSenderRoleNotAndReadByStartupFalse(
                                room.getId(), role
                        );
            } else { // INVESTOR
                unreadCount = chatMessageRepository
                        .countByChatRoomIdAndDeletedFalseAndSenderRoleNotAndReadByInvestorFalse(
                                room.getId(), role
                        );
            }

            // 6) 상대방 최신 프로필 이미지 URL (없으면 null)
            String opponentProfileImageUrl = documentService.getLatestProfileImageUrl(opponentId);

            // 7) DTO 빌드
            result.add(
                    ChatRoomListResponse.builder()
                            .roomId(room.getId())
                            .opponentId(opponentId)
                            .opponentName(opponent.getName())
                            .opponentProfileImage(opponentProfileImageUrl)
                            .lastMessage(lastContent)
                            .lastMessageTime(lastTime)
                            .hasUnread(unreadCount > 0)
                            .build()
            );
        }

        // 8) 마지막 메시지 시간 기준으로 내림차순 정렬 (최신 채팅방이 위로 오게)
        result.sort(
                Comparator.comparing(
                        ChatRoomListResponse::lastMessageTime,
                        Comparator.nullsLast(Comparator.reverseOrder())
                )
        );

        return result;
    }

}
