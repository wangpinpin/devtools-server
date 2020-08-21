package com.wpp.devtools.repository;

import com.wpp.devtools.domain.entity.EveryDayText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @program: volvo-sever
 * @description:
 * @author: wpp
 * @create: 2020-08-21
 **/
@Repository
public interface EveryDayTextRepository extends JpaRepository<EveryDayText, Long> {

    EveryDayText findByTitle(String title);

    @Query(value = "SELECT * FROM every_day_text ORDER BY RAND() LIMIT 1", nativeQuery = true)
    EveryDayText findOneTextByRandom();
}
