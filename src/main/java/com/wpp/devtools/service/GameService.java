package com.wpp.devtools.service;

import com.wpp.devtools.domain.bo.PlayGameBo;
import com.wpp.devtools.domain.entity.GameRecord;
import com.wpp.devtools.repository.GameRecordRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GameService {

    @Autowired
    private GameRecordRepository gameRecordRepository;

    /**
     * 游戏开始接口
     * @param playGameBo
     * @param userId
     * @return
     */
    public void playGame(PlayGameBo playGameBo, String userId) {
        if(StringUtils.isBlank(userId)) {
            return;
        }

        GameRecord gameRecord = GameRecord.builder()
                .userId(userId)
                .gameCode(playGameBo.getGameCode())
                .gameLevel(playGameBo.getGameLevel())
                .startTime(new Timestamp(new Date().getTime()))
                .build();
        gameRecordRepository.save(gameRecord);
    }


    /**
     * 游戏结束接口
     * @param userId
     * @return
     */
    public void gameEnd(String userId) {
        if(StringUtils.isBlank(userId)) {
            return;
        }

        GameRecord gameRecord = gameRecordRepository.findByUserId(userId);
        if(ObjectUtils.isEmpty(gameRecord)) {
            return;
        }

        Timestamp endTimestamp = new Timestamp(new Date().getTime());
        //计算耗时多少秒
        Long seconds = (endTimestamp.getTime() - gameRecord.getStartTime().getTime()) / 1000;
        gameRecord.setEndTime(endTimestamp);
        gameRecord.setSeconds(seconds);
        gameRecordRepository.save(gameRecord);
    }


    /**
     * 游戏排行榜查询
     * @param gameCode
     * @return
     */
    public Map<String, List<Map<String, Object>>> gameRank(String gameCode) {
        List<Map<String, Object>> rankList = gameRecordRepository.findGameRank(gameCode);
        Map<String, List<Map<String, Object>>> mapRank = rankList.stream().collect(Collectors.groupingBy(e -> e.get("gameLevel").toString()));
        return mapRank;
    }
}
