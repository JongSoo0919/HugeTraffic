package com.example.trafficproject.application.usecase;

import com.example.trafficproject.domain.follow.entity.Follow;
import com.example.trafficproject.domain.follow.service.FollowReadService;
import com.example.trafficproject.domain.member.dto.MemberDto;
import com.example.trafficproject.domain.member.service.MemberReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GetFollowingMemberUsecase {
    private final MemberReadService memberReadService;
    private final FollowReadService followReadService;
    /***
     * 1. fromMemberId = memberId -> Follow List
     * 2. 1번을 순회하며 회원정보 찾기.
     * @param memberId
     * @return
     */
    public List<MemberDto> execute(Long memberId){
        List<Follow> followings = followReadService.getFollowing(memberId);
        List<Long> followingMemberIds = followings.stream().map(Follow::getToMemberId).toList();
        return memberReadService.getMembers(followingMemberIds);
    }
}
