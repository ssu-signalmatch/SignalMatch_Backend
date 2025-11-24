package com.signalmatch_backend.chat;

import com.signalmatch_backend.chat.domain.ChatRoom;
import com.signalmatch_backend.chat.repository.ChatRoomRepository;
import com.signalmatch_backend.common.exception.CustomException;
import com.signalmatch_backend.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatRoomFinder {

    private final ChatRoomRepository chatRoomRepository;

    public ChatRoom findById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_FOUND));
    }

    public ChatRoom findByStartupAndInvestor(Long startupId, Long investorId) {
        return chatRoomRepository.findByStartupIdAndInvestorId(startupId, investorId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_FOUND));
    }
}