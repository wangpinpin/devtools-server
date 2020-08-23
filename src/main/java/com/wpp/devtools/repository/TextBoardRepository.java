package com.wpp.devtools.repository;

import com.wpp.devtools.domain.entity.TextBoard;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * @program: volvo-sever
 * @description:
 * @author: wpp
 * @create: 2020-08-18
 **/
@Repository
public interface TextBoardRepository extends JpaRepository<TextBoard, String> {


    @Query(value="SELECT a.id, a.content, a.praise_count praiseCount, a.create_time createTime, CASE WHEN b.id IS NULL THEN FALSE ELSE TRUE END AS praise " +
            "FROM text_board a " +
            "LEFT JOIN text_board_praise b ON b.text_board_id = a.id  AND b.ip = ?3 " +
            "ORDER BY a.create_time DESC  LIMIT ?1, ?2 ", nativeQuery = true)
    List<Map<String, Object>> findAllByPage(int pageNo, int pageSize, String ip);

    @Transactional
    @Modifying
    @Query(value = "UPDATE `text_board` SET praise_count = praise_count + 1 WHERE id = ?1", nativeQuery = true)
    void addPraiseCount(String id);

}
