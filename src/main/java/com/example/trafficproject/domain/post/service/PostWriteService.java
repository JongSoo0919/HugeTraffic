package com.example.trafficproject.domain.post.service;

import com.example.trafficproject.domain.post.dto.PostCommand;
import com.example.trafficproject.domain.post.entity.Post;
import com.example.trafficproject.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostWriteService {

    private final PostRepository postRepository;

    public Long create(PostCommand command){
        Post post = Post.builder()
                .memberId(command.memberId())
                .contents(command.contents())
                .build();

        return postRepository.save(post).getId();
    }

    //데이터를 조회하고 변경하고, 저장하는 트랜잭션으로 인한 동시성 문제가 발생하기 좋은 구간
    @Transactional
    public void likePost(Long postId){
        Post post = postRepository.findById(postId,true).orElseThrow();
        post.increamentLikeCount();
        postRepository.save(post);
    }

    @Transactional
    public void likePostByOptimistickLock(Long postId){
        Post post = postRepository.findById(postId,false).orElseThrow();
        post.increamentLikeCount();
        postRepository.save(post);
    }

}
