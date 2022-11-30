package com.example.trafficproject.util;

import com.example.trafficproject.domain.post.entity.Post;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.function.Predicate;

import static org.jeasy.random.FieldPredicates.*;

public class PostFixtureFactoryTest {

    /***
     *
     * @param memberId
     * @param firstDate
     * @param lastDate
     * @return
     */
    public static EasyRandom get(Long memberId, LocalDate firstDate, LocalDate lastDate){
        Predicate<Field> memberIdField = named("memberId")
                .and(ofType(Long.class))
                .and(inClass(Post.class));
        Predicate<Field> idField = named("id")
                .and(ofType(Long.class))
                .and(inClass(Post.class));

        EasyRandomParameters param = new EasyRandomParameters()
                .excludeField(idField)
                .dateRange(firstDate,lastDate)
                .randomize(memberIdField, () -> memberId);
        return new EasyRandom(param);
    }
}
