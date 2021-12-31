package com.wpp.devtools.repository;

import com.wpp.devtools.domain.entity.GameRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @program: devtools-server
 * @description:
 * @author: wpp
 * @create: 2020-09-17
 **/
@Repository
public interface GameRecordRepository extends JpaRepository<GameRecord, String> {

    GameRecord findByUserId(String userId);

    @Query(value = "SELECT a.game_level gameLevel, a.seconds, b.nick_name nickName, b.head_img headImg " +
            "FROM `game_record` a " +
            "LEFT JOIN user b ON b.id = a.user_id " +
            "WHERE a.game_code = ?1 AND a.seconds IS NOT NULL ORDER BY a.seconds ASC limit 1000", nativeQuery = true)
    List<Map<String, Object>> findGameRank(String gameCode);


}
