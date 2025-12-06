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
import com.signalmatch_backend.investor.domain.Investor;
import com.signalmatch_backend.startup.StartupFinder;
import com.signalmatch_backend.startup.domain.Startup;
import com.signalmatch_backend.user.UserFinder;
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
    private final UserFinder userFinder;

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

        User me = userFinder.findByUserId(userId);

        List<ChatRoom> rooms;
        if (role == UserRole.STARTUP) {
            Startup myStartup = startupFinder.findByOwner(me);
            Long myStartupId = myStartup.getStartupId();

            rooms = chatRoomRepository.findAllByStartupId(myStartupId);

        } else if (role == UserRole.INVESTOR) {
            Investor myInvestor = investorFinder.findByOwner(me);
            Long myInvestorId = myInvestor.getInvestorId();

            rooms = chatRoomRepository.findAllByInvestorId(myInvestorId);

        } else {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        if (rooms.isEmpty()) {
            return List.of();
        }

        if (role == UserRole.STARTUP) {
            List<Long> opponentInvestorIds = rooms.stream()
                    .map(ChatRoom::getInvestorId)
                    .distinct()
                    .toList();

            Map<Long, User> opponentUserMap = opponentInvestorIds.stream()
                    .map(investorId -> investorFinder.findByInvestorId(investorId))
                    .collect(Collectors.toMap(
                            inv -> inv.getInvestorId(),
                            Investor::getOwner
                    ));

            return buildChatRoomListResponses(
                    rooms,
                    role,
                    opponentUserMap,
                    true
            );

        } else {
            List<Long> opponentStartupIds = rooms.stream()
                    .map(ChatRoom::getStartupId)
                    .distinct()
                    .toList();

            Map<Long, User> opponentUserMap = opponentStartupIds.stream()
                    .map(startupId -> startupFinder.findByStartupId(startupId))
                    .collect(Collectors.toMap(
                            st -> st.getStartupId(),
                            Startup::getOwner
                    ));

            return buildChatRoomListResponses(
                    rooms,
                    role,
                    opponentUserMap,
                    false
            );
        }
    }
    private List<ChatRoomListResponse> buildChatRoomListResponses(
            List<ChatRoom> rooms,
            UserRole meRole,
            Map<Long, User> opponentUserMap,
            boolean isStartupSide
    ) {
        List<ChatRoomListResponse> result = new ArrayList<>();

        for (ChatRoom room : rooms) {

            // 상대 도메인 PK (InvestorId 또는 StartupId)
            Long opponentDomainId = isStartupSide ? room.getInvestorId() : room.getStartupId();

            User opponentUser = opponentUserMap.get(opponentDomainId);
            if (opponentUser == null) {
                throw new CustomException(ErrorCode.USER_NOT_FOUND);
            }

            // 마지막 메시지
            Optional<ChatMessage> lastOpt =
                    chatMessageRepository.findTopByChatRoomIdAndDeletedFalseOrderByIdDesc(room.getId());

            String lastContent = lastOpt.map(ChatMessage::getContent).orElse(null);
            LocalDateTime lastTime = lastOpt.map(ChatMessage::getCreatedAt).orElse(null);

            // 안 읽은 메시지 개수
            long unreadCount;
            if (meRole == UserRole.STARTUP) {
                unreadCount = chatMessageRepository
                        .countByChatRoomIdAndDeletedFalseAndSenderRoleNotAndReadByStartupFalse(
                                room.getId(), meRole
                        );
            } else {
                unreadCount = chatMessageRepository
                        .countByChatRoomIdAndDeletedFalseAndSenderRoleNotAndReadByInvestorFalse(
                                room.getId(), meRole
                        );
            }

            Long opponentUserId = opponentUser.getUserId();
            String opponentProfileImageUrl = documentService.getLatestProfileImageUrl(opponentUserId);

            result.add(
                    ChatRoomListResponse.builder()
                            .roomId(room.getId())
                            .opponentId(opponentDomainId)            // startupId 또는 investorId
                            .opponentName(opponentUser.getName())
                            .opponentProfileImage(opponentProfileImageUrl)
                            .lastMessage(lastContent)
                            .lastMessageTime(lastTime)
                            .hasUnread(unreadCount > 0)
                            .build()
            );
        }

        result.sort(
                Comparator.comparing(
                        ChatRoomListResponse::lastMessageTime,
                        Comparator.nullsLast(Comparator.reverseOrder())
                )
        );

        return result;
    }
}