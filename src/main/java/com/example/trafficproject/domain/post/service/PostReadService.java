package com.example.trafficproject.domain.post.service;

import com.example.trafficproject.domain.post.dto.DailyPostCount;
import com.example.trafficproject.domain.post.dto.DailyPostCountRequest;
import com.example.trafficproject.domain.post.entity.Post;
import com.example.trafficproject.domain.post.repository.PostRepository;
import com.example.trafficproject.util.CursorRequest;
import com.example.trafficproject.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PostReadService {
    private final PostRepository postRepository;

    /***
     * 작성일자, 작성회원, 작성 게시물 갯수 list 반환
     * @param request
     * @return
     */
    public List<DailyPostCount> getDailyPostCount(DailyPostCountRequest request){
        // select * from post where memberId = :memberId and createdDate between :firstDate and :lastDate group by createdDate memberId
        return postRepository.groupByCreateDate(request);
    }

    public Page<Post> getPosts(Long memberId, PageRequest pageRequest){
        return postRepository.findAllByMemberId(memberId,pageRequest);
    }

    public PageCursor<Post> getPosts(Long memberId, CursorRequest cursorRequest){
        List<Post> posts = findAllById(memberId, cursorRequest);
        Long nextKey = posts.stream()
                .mapToLong(Post::getId)
                .min()
                .orElse(CursorRequest.EMPTY_KEY);
        return new PageCursor<>(cursorRequest.next(nextKey), posts);
    }

    private List<Post> findAllById(Long memberId, CursorRequest cursorRequest){
        if(cursorRequest.hasKey()){
           return postRepository.findAllByLessThanIdAndMemberIdAndOrderByIdDesc(cursorRequest.key(), memberId, cursorRequest.size());
        }
        return postRepository.findAllByMemberIdAndOrderByIdDesc(memberId, cursorRequest.size());
    }
}
