package com.example.fastcampusmysql.domain.member.service;

import com.example.fastcampusmysql.domain.member.dto.RegisterMemberCommand;
import com.example.fastcampusmysql.domain.member.entity.Member;
import com.example.fastcampusmysql.domain.member.entity.MemberNicknameHistory;
import com.example.fastcampusmysql.domain.member.repository.MemberNicknameHistoryRepository;
import com.example.fastcampusmysql.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberWriteService {

    private final MemberRepository memberRepository;
    private final MemberNicknameHistoryRepository memberNicknameHistoryRepository;
    /**
     * 목표 - 회원정보 등록(이메일, 닉네임, 생년월일)
     * 닉네임은 10자를 넘길 수 없다.
     */
    public Member create(RegisterMemberCommand command){
        Member member = Member.builder()
                .nickname(command.nickname())
                .email(command.email())
                .birthday(command.birthday())
                .build();
        Member savedMember =  memberRepository.save(member);
        saveMemberNicknameHistory(savedMember);
        return savedMember;
    }

    /***
     * 1. 회원 이름을 변경
     * 2. 변경 내역을 저장
     * @param memberId
     * @param nickname
     */
    public void changeNickname(long memberId, String nickname){
        Member member = memberRepository.findById(memberId).orElseThrow();
        member.changeNickname(nickname);
        memberRepository.save(member);

        saveMemberNicknameHistory(member);
        // TODO : 변경 내역 히스토리 저장
    }

    private void saveMemberNicknameHistory(Member member) {
        MemberNicknameHistory memberNicknameHistory = MemberNicknameHistory.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .build();
        memberNicknameHistoryRepository.save(memberNicknameHistory);
    }
}
