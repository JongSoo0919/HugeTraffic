package com.example.fastcampusmysql.util;

import com.example.fastcampusmysql.domain.member.entity.Member;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

/***
 * 테스트에 필요한 데이터들에대한 케이스를 랜덤하게 생성
 * EasyRandom
 */
public class MemberFixtureFactory {
    /***
     * default seed
     * @return
     */
    public static Member create(){
        EasyRandomParameters param = new EasyRandomParameters();
        return new EasyRandom(param).nextObject(Member.class);
    }
    /***
     * 테스트 케이스에 필요한 데이터 생성
     * @return
     */
    public static Member create(long seed){
        EasyRandomParameters param = new EasyRandomParameters().seed(seed);
        return new EasyRandom(param).nextObject(Member.class);
    }
}
