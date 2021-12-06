package com.wpp.devtools.repository;

import com.wpp.devtools.domain.entity.Activity;
import com.wpp.devtools.domain.entity.GameRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: devtools-server
 * @description:
 * @author: wpp
 * @create: 2020-09-17
 **/
@Repository
public interface GameRecordRepository extends JpaRepository<GameRecord, String> {

    GameRecord findByUserUUID(String userUUID);

    @Query(value = "SELECT name, gameLevel, seconds FROM `game_record` WHERE gameCode = ?1 AND seconds IS NOT NULL ORDER seconds ASC limit 1000", nativeQuery = true)
    List<GameRecord> findGameRank(String gameCode);


}
