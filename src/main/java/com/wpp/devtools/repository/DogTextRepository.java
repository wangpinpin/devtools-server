package com.wpp.devtools.repository;

import com.wpp.devtools.domain.entity.DogText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @program: devtools-server
 * @description:
 * @author: wpp
 * @create: 2020-08-13
 **/
@Repository
public interface DogTextRepository extends JpaRepository<DogText, Long> {

    DogText findByContent(String content);

    @Query(value = "SELECT content FROM dog_text WHERE type_id = ?1 ORDER BY RAND() LIMIT 1", nativeQuery = true)
    String findContentByTypeIdAndRandom(String typeId);
}
