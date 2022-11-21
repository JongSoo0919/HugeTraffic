package com.example.trafficproject.application.controller;

import com.example.trafficproject.domain.post.dto.PostCommand;
import com.example.trafficproject.domain.post.service.PostWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostWriteService postWriteService;

    @PostMapping("")
    public Long create(PostCommand command){
        return postWriteService.create(command);
    }
}
