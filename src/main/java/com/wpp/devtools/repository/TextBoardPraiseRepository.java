package com.wpp.devtools.repository;

import com.wpp.devtools.domain.entity.TextBoardPraise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @program: volvo-sever
 * @description:
 * @author: wpp
 * @create: 2020-08-18
 **/
@Repository
public interface TextBoardPraiseRepository extends JpaRepository<TextBoardPraise, String> {


    @Query(value="SELECT COUNT(*) FROM `text_board_praise` WHERE ip = ?1 AND text_board_id = ?2", nativeQuery = true)
    int findParaiseRecordCount(String ip, String msgId);
}
