package com.example.fastcampusmysql.domain.member;

import com.example.fastcampusmysql.domain.member.entity.Member;
import com.example.fastcampusmysql.util.MemberFixtureFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class MemberTest {
    //insert, select에 대한 테스트 코드도 짜보자.

    @DisplayName("회원은 닉네임 변경이 가능하다.")
    @Test
    public void testChangeName() {
        Member member = MemberFixtureFactory.create();
        String expectedName = "pnu";

        member.changeNickname(expectedName);
        Assertions.assertEquals(expectedName, member.getNickname());
    }

    @DisplayName("회원의 닉네임은 10자를 초과할 수 없다")
    @Test
    public void testNicknameMaxLength() {
        Member member = MemberFixtureFactory.create();
        String overMaxLength = "pnu12345678910";

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> member.changeNickname(overMaxLength)
        );
    }


}
