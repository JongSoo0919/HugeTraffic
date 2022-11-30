package com.example.trafficproject.domain.post;

import com.example.trafficproject.domain.post.entity.Post;
import com.example.trafficproject.domain.post.repository.PostRepository;
import com.example.trafficproject.util.PostFixtureFactoryTest;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

@Transactional
@SpringBootTest
public class PostBulkInsertTest {
    @Autowired
    private PostRepository postRepository;

    @DisplayName("대량 데이터 삽입 테스트")
//    @Test
    public void bulkInsert(){
        EasyRandom easyRandom = PostFixtureFactoryTest.get(
                3L,
                LocalDate.of(1970,1,1),
                LocalDate.of(2022,2,1));

        StopWatch stopWatch = new StopWatch();
        StopWatch queryStopWatch = new StopWatch();
        stopWatch.start();
        // Spring JDBC를 사용하여 리스트 형태로 전달이 가능
        List<Post> posts = IntStream.range(0,1000000)
                .mapToObj(i -> easyRandom.nextObject(Post.class))
                .toList();
        stopWatch.stop();
        System.out.println("객체 생성 총 시간 : " + stopWatch.getTotalTimeSeconds());
        // 추 후 DB단에만 부하를 걸어보는 것이 아닌, API 호출 단 부터 부하를 걸어보는 것도 좋음.
        queryStopWatch.start();
        postRepository.bulkInsert(posts);
        queryStopWatch.stop();
        System.out.println("query Time : "+queryStopWatch.getTotalTimeSeconds());

    }
}
