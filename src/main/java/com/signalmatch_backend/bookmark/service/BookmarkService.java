package com.signalmatch_backend.bookmark.service;

import com.signalmatch_backend.bookmark.domain.Bookmark;
import com.signalmatch_backend.bookmark.dto.BookmarkRequest;
import com.signalmatch_backend.bookmark.dto.BookmarkResponse;
import com.signalmatch_backend.bookmark.repository.BookmarkRepository;
import com.signalmatch_backend.common.exception.CustomException;
import com.signalmatch_backend.common.exception.ErrorCode;
import com.signalmatch_backend.investor.domain.Investor;
import com.signalmatch_backend.investor.repository.InvestorRepository;
import com.signalmatch_backend.startup.domain.Startup;
import com.signalmatch_backend.startup.repository.StartupRepository;
import com.signalmatch_backend.user.UserFinder;
import com.signalmatch_backend.user.domain.User;
import com.signalmatch_backend.user.domain.enums.UserRole;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserFinder userFinder;
    private final InvestorRepository investorRepository;
    private final StartupRepository startupRepository;
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

    public void deleteBookmark(Long userId, Long targetUserId) {
        User targetUser = userFinder.findByUserId(targetUserId);

        if (targetUser.getUserRole() == UserRole.INVESTOR) {
            Investor investor = investorRepository.findByOwner(targetUser)
                .orElseThrow(() -> new CustomException(ErrorCode.INVESTOR_NOT_FOUND));
            deleteInvestorBookmark(userId, investor);
        } else {
            Startup startup = startupRepository.findByOwner(targetUser)
                .orElseThrow(() -> new CustomException(ErrorCode.STARTUP_NOT_FOUND));
            deleteStartupBookmark(userId, startup);
        }

    }
    private void deleteInvestorBookmark(Long userId, Investor investor) {
        Bookmark bookmark = bookmarkRepository.findByUserIdAndInvestorId(userId, investor.getInvestorId())
            .orElseThrow(() -> new CustomException(ErrorCode.BOOKMARK_NOT_FOUND));
        bookmarkRepository.delete(bookmark);
    }

    private void deleteStartupBookmark(Long userId, Startup startup) {
        Bookmark bookmark = bookmarkRepository.findByUserIdAndStartupId(userId, startup.getStartupId())
            .orElseThrow(() -> new CustomException(ErrorCode.BOOKMARK_NOT_FOUND));
        bookmarkRepository.delete(bookmark);
    }

}
