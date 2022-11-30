package com.example.trafficproject.domain.member;

import com.example.trafficproject.domain.member.entity.Member;
import com.example.trafficproject.util.MemberFixtureFactoryTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MemberTest {
    //insert, select에 대한 테스트 코드도 짜보자.

    @DisplayName("회원은 닉네임 변경이 가능하다.")
    @Test
    public void testChangeName() {
        Member member = MemberFixtureFactoryTest.create();
        String expectedName = "pnu";

        member.changeNickname(expectedName);
        Assertions.assertEquals(expectedName, member.getNickname());
    }

    @DisplayName("회원의 닉네임은 10자를 초과할 수 없다")
    @Test
    public void testNicknameMaxLength() {
        Member member = MemberFixtureFactoryTest.create();
        String overMaxLength = "pnu12345678910";

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> member.changeNickname(overMaxLength)
        );
    }


}
