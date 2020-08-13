package com.wpp.devtools.repository;

import com.wpp.devtools.domain.entity.DogText;
import com.wpp.devtools.domain.entity.Wb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @program: volvo-sever
 * @description:
 * @author: wpp
 * @create: 2020-08-13
 **/
@Repository
public interface DogTextRepository extends JpaRepository<DogText, Long> {

    DogText findByContent(String content);

    @Query(value = "SELECT content FROM dog_text ORDER BY RAND() LIMIT 1", nativeQuery = true)
    String findContentByRandom();
}
