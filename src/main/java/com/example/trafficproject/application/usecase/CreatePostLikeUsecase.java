package com.example.trafficproject.application.usecase;

import com.example.trafficproject.domain.member.dto.MemberDto;
import com.example.trafficproject.domain.member.entity.Member;
import com.example.trafficproject.domain.member.service.MemberReadService;
import com.example.trafficproject.domain.post.entity.Post;
import com.example.trafficproject.domain.post.service.PostLikeWriteService;
import com.example.trafficproject.domain.post.service.PostReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatePostLikeUsecase {
    private final PostReadService postReadService;
    private final MemberReadService memberReadService;
    private final PostLikeWriteService postLikeWriteService;

    public void execute(Long postId, Long memberId){
        Post post = postReadService.getPost(postId);
        MemberDto member = memberReadService.getMember(memberId);
        postLikeWriteService.create(post, member);
    }
}
