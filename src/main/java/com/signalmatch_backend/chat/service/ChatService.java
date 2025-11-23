package com.signalmatch_backend.chat.service;


import com.signalmatch_backend.chat.ChatMessageFinder;
import com.signalmatch_backend.chat.ChatRoomFinder;
import com.signalmatch_backend.chat.domain.ChatMessage;
import com.signalmatch_backend.chat.domain.ChatRoom;
import com.signalmatch_backend.chat.dto.ChatMessageResponse;
import com.signalmatch_backend.chat.dto.ChatRoomCreateRequest;
import com.signalmatch_backend.chat.dto.ChatRoomResponse;
import com.signalmatch_backend.chat.repository.ChatMessageRepository;
import com.signalmatch_backend.chat.repository.ChatRoomRepository;
import com.signalmatch_backend.common.exception.CustomException;
import com.signalmatch_backend.common.exception.ErrorCode;
import com.signalmatch_backend.investor.InvestorFinder;
import com.signalmatch_backend.startup.StartupFinder;
import com.signalmatch_backend.user.domain.User;
import com.signalmatch_backend.user.domain.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

}
