package com.signalmatch_backend.bookmark.service;

import com.signalmatch_backend.bookmark.domain.Bookmark;
import com.signalmatch_backend.bookmark.dto.BookmarkRequest;
import com.signalmatch_backend.bookmark.dto.BookmarkResponse;
import com.signalmatch_backend.bookmark.repository.BookmarkRepository;
import com.signalmatch_backend.common.exception.CustomException;
import com.signalmatch_backend.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    @Transactional
    public BookmarkResponse addBookmark(Long userId, BookmarkRequest request){
        Long investorId = request.investorId();
        Long startupId = request.startupId();

        if((investorId == null && startupId == null)|| (investorId != null && startupId != null)){
            throw new CustomException(ErrorCode.BOOKMARK_ONLY_ONE_TARGET_ALLOWED);
        }

        if (bookmarkRepository.existsByUserIdAndInvestorIdAndStartupId(userId, investorId, startupId)){
            throw new CustomException(ErrorCode.BOOKMARK_ALREADY_EXISTS);
        }
        Bookmark bookmark= Bookmark.builder()
            .userId(userId)
            .investorId(investorId)
            .startupId(startupId)
            .build();
        Bookmark savedBookmark = bookmarkRepository.save(bookmark);

        return new BookmarkResponse(savedBookmark.getBookmarkId(),savedBookmark.getInvestorId(),savedBookmark.getStartupId());
    }

}
