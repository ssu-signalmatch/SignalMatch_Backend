package com.signalmatch_backend.bookmark.service;

import com.signalmatch_backend.bookmark.domain.Bookmark;
import com.signalmatch_backend.bookmark.dto.BookmarkListResponse;
import com.signalmatch_backend.bookmark.dto.BookmarkRequest;
import com.signalmatch_backend.bookmark.dto.BookmarkResponse;
import com.signalmatch_backend.bookmark.dto.StartupBookmarkInfo;
import com.signalmatch_backend.bookmark.repository.BookmarkRepository;
import com.signalmatch_backend.common.exception.CustomException;
import com.signalmatch_backend.common.exception.ErrorCode;
import com.signalmatch_backend.investor.InvestorFinder;
import com.signalmatch_backend.investor.domain.Investor;
import com.signalmatch_backend.investor.repository.InvestorRepository;
import com.signalmatch_backend.startup.StartupFinder;
import com.signalmatch_backend.startup.domain.Startup;
import com.signalmatch_backend.startup.repository.StartupRepository;
import com.signalmatch_backend.user.UserFinder;
import com.signalmatch_backend.user.domain.User;
import com.signalmatch_backend.user.domain.enums.UserRole;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserFinder userFinder;
    private final InvestorRepository investorRepository;
    private final StartupRepository startupRepository;
    private final InvestorFinder investorFinder;
    private final StartupFinder startupFinder;
    public BookmarkResponse addBookmark(Long userId, BookmarkRequest request){
        Long investorId = request.investorId();
        Long startupId = request.startupId();
        Long targetUserId;
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

        if (investorId != null) {
            Investor investor = investorFinder.findByInvestorId(investorId);
            targetUserId = investor.getOwner().getUserId();
        } else {
            Startup startup = startupFinder.findByStartupId(startupId);
            targetUserId = startup.getOwner().getUserId();
        }
        return new BookmarkResponse(savedBookmark.getBookmarkId(),userId,targetUserId);
    }

    public List<BookmarkListResponse> getBookmarkList(Long userId){
        List<Bookmark> bookmarks = bookmarkRepository.findByUserId(userId);

        return bookmarks.stream().map(
            bookmark -> {
                if (bookmark.getInvestorId() != null) {
                    Investor investor = investorRepository.findById(bookmark.getInvestorId())
                        .orElseThrow(() -> new CustomException(ErrorCode.INVESTOR_NOT_FOUND));

                    return new BookmarkListResponse(
                        "INVESTOR",
                        investor.getInvestorId(),
                        investor.getInvestorName()
                    );
                } else if (bookmark.getStartupId() != null) {
                    Startup startup = startupRepository.findById(bookmark.getStartupId())
                        .orElseThrow(() -> new CustomException(ErrorCode.STARTUP_NOT_FOUND));
                    return new BookmarkListResponse(
                        "STARTUP",
                        startup.getStartupId(),
                        startup.getStartupName()
                    );
                } else {
                    throw  new CustomException(ErrorCode.BOOKMARK_NOT_FOUND);
                }
            }).toList();

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

    public List<StartupBookmarkInfo> getTop10Startups() {
        Pageable pageable = PageRequest.of(0, 10);
        return bookmarkRepository.findTop10ByBookmarkCount(pageable);
    }

}
