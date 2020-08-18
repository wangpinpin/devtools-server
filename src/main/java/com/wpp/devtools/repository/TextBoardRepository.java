package com.wpp.devtools.repository;

import com.wpp.devtools.domain.entity.TextBoard;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @program: volvo-sever
 * @description:
 * @author: wpp
 * @create: 2020-08-18
 **/
@Repository
public interface TextBoardRepository extends JpaRepository<TextBoard, String> {


    @Query(value="SELECT content, create_time createTime FROM text_board ORDER BY create_time DESC LIMIT ?1, ?2", nativeQuery = true)
    List<Map<String, Object>> findAllByPage(int pageNo, int pageSize);
}
