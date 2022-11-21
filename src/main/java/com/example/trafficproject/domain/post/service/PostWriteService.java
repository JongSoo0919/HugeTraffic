package com.example.trafficproject.domain.post.service;

import com.example.trafficproject.domain.post.dto.PostCommand;
import com.example.trafficproject.domain.post.entity.Post;
import com.example.trafficproject.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
