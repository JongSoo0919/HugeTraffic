package com.example.trafficproject.application.controller;

import com.example.trafficproject.domain.member.dto.MemberDto;
import com.example.trafficproject.domain.member.dto.MemberNicknameHistoryDto;
import com.example.trafficproject.domain.member.dto.RegisterMemberCommand;
import com.example.trafficproject.domain.member.service.MemberReadService;
import com.example.trafficproject.domain.member.service.MemberWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberWriteService memberWriteService;
    private final MemberReadService memberReadService;

    @PostMapping("/")
    public MemberDto register(@RequestBody RegisterMemberCommand command){
        return memberReadService.toDto(memberWriteService.create(command));
    }

    @GetMapping("/{id}")
    public MemberDto getMember(@PathVariable long id){
        return memberReadService.getMember(id);
    }

    @PutMapping("/{id}/name")
    public MemberDto changeNickname(@PathVariable long id, @RequestBody String nickname){
        memberWriteService.changeNickname(id,nickname);
        return memberReadService.getMember(id);
    }

    @GetMapping("/{memberId}/nickname-histories")
    public List<MemberNicknameHistoryDto> getNicknameHistories(@PathVariable Long memberId){
        return memberReadService.getNicknameHistories(memberId);
    }
}
