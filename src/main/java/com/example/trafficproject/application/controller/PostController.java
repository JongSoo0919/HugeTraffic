package com.example.trafficproject.application.controller;

import com.example.trafficproject.application.usecase.CreatePostLikeUsecase;
import com.example.trafficproject.application.usecase.CreatePostUsecase;
import com.example.trafficproject.application.usecase.GetTimelinePostsUsecase;
import com.example.trafficproject.domain.member.dto.PostDto;
import com.example.trafficproject.domain.post.dto.DailyPostCount;
import com.example.trafficproject.domain.post.dto.DailyPostCountRequest;
import com.example.trafficproject.domain.post.dto.PostCommand;
import com.example.trafficproject.domain.post.entity.Post;
import com.example.trafficproject.domain.post.service.PostReadService;
import com.example.trafficproject.domain.post.service.PostWriteService;
import com.example.trafficproject.util.CursorRequest;
import com.example.trafficproject.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostWriteService postWriteService;
    private final PostReadService postReadService;
    private final GetTimelinePostsUsecase getTimelinePostsUsecase;
    private final CreatePostUsecase createPostUsecase;
    private final CreatePostLikeUsecase createPostLikeUsecase;

    @PostMapping("")
    public Long create(PostCommand command){
        return createPostUsecase.execute(command);
    }

    @GetMapping("/daily-post-counts")
    public List<DailyPostCount> getDailyPostCounts(DailyPostCountRequest request){
        return postReadService.getDailyPostCount(request);
    }

    @GetMapping("/members/{memberId}")
    public Page<PostDto> getPosts(
            @PathVariable Long memberId,
            @RequestParam Integer page,
            @RequestParam Integer size

    ){
        return postReadService.getPosts(memberId, PageRequest.of(page,size));
    }

    @GetMapping("/members/{memberId}/by-cursor")
    public PageCursor<Post> getPostsByCursor(
            @PathVariable Long memberId,
            CursorRequest cursorRequest

    ){
        return postReadService.getPosts(memberId, cursorRequest);
    }

    @GetMapping("/members/{memberId}/timeline")
    public PageCursor<Post> getTimeline(
            @PathVariable Long memberId,
            CursorRequest cursorRequest

    ){
        return getTimelinePostsUsecase.executeByTimeline(memberId, cursorRequest);
    }

//    @PostMapping("/{postId}/like")
//    public void likePost(@PathVariable Long postId){
//        postWriteService.likePost(postId);
//    }

    @PostMapping("/{postId}/like/v1")
    public void likePost(@PathVariable Long postId){
//        postWriteService.likePost(postId);
        postWriteService.likePostByOptimistickLock(postId);
    }

    @PostMapping("/{postId}/like/v2")
    public void likePostV2(@PathVariable Long postId, @RequestParam Long memberId){
        createPostLikeUsecase.execute(postId, memberId);
    }


}
