package com.example.fastcampusmysql.domain.follow.service;

import com.example.fastcampusmysql.domain.follow.entitiy.Follow;
import com.example.fastcampusmysql.domain.follow.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FollowReadService {
    private final FollowRepository followRepository;

    // 외부 API로 나가는 경우조차 없을 것 같기에 dto가 아닌 객체 본인이 나가도 되긴 할듯.
    // dto만들어서 쓰는게 좋음
    public List<Follow> getFollowing(Long memberId){
        return followRepository.findAllByFromMemberId(memberId);
    }
}
