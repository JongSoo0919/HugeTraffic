package com.example.fastcampusmysql.domain.follow.service;

import com.example.fastcampusmysql.domain.follow.entitiy.Follow;
import com.example.fastcampusmysql.domain.follow.repository.FollowRepository;
import com.example.fastcampusmysql.domain.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class FollowWriteService {
    private final FollowRepository followRepository;
    /***
     * from , to 회원 정보를 받아서 저장
     * from <-> to validate.
     */
    public void create(MemberDto fromMember, MemberDto toMember){
        Assert.isTrue(!Objects.equals(fromMember.id(), toMember.id()),"From, To 회원이 동일합니다.");

        followRepository.save(Follow.builder()
                .fromMemberId(fromMember.id())
                .toMemberId(toMember.id())
                .build());
    }
}
