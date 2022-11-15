package com.example.trafficproject.application.controller;

import com.example.trafficproject.application.usecase.CreateFollowMemberUsecase;
import com.example.trafficproject.application.usecase.GetFollowingMemberUsecase;
import com.example.trafficproject.domain.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/follow")
public class FollowController {
    private final CreateFollowMemberUsecase createFollowMemberUsecase;
    private final GetFollowingMemberUsecase getFollowingMemberUsecase;
    @PostMapping("/{fromId}/{toId}")
    public void create(@PathVariable Long fromId, @PathVariable Long toId){
        createFollowMemberUsecase.execute(fromId,toId);
    }

    @GetMapping("/members/{fromId}")
    public List<MemberDto> select(@PathVariable Long fromId){
        return getFollowingMemberUsecase.execute(fromId);
    }
}
