package com.example.trafficproject.application.usecase;

import com.example.trafficproject.domain.follow.entity.Follow;
import com.example.trafficproject.domain.follow.service.FollowReadService;
import com.example.trafficproject.domain.post.entity.Post;
import com.example.trafficproject.domain.post.service.PostReadService;
import com.example.trafficproject.util.CursorRequest;
import com.example.trafficproject.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetTimelinePostsUsecase {

    private final FollowReadService followReadService;
    private final PostReadService postReadService;

    /***
     * 1. memberId로 follower 조회
     * 2. 위의 결과로 게시물 조회.
     * @param memberId
     * @param cursorRequest
     * @return
     */
    public PageCursor<Post> execute(Long memberId, CursorRequest cursorRequest){
        List<Follow> followings = followReadService.getFollowing(memberId);
        List<Long> followingsMemberIds = followings
                .stream()
                .map(Follow::getToMemberId)
                .toList();
        return postReadService.getPosts(followingsMemberIds, cursorRequest);
    }
}
