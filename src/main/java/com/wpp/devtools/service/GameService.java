package com.wpp.devtools.service;

import com.wpp.devtools.domain.bo.PlayGameBo;
import com.wpp.devtools.domain.entity.GameRecord;
import com.wpp.devtools.repository.GameRecordRepository;
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
     * @return
     */
    public String playGame(PlayGameBo playGameBo) {
        String userUUID = UUID.randomUUID().toString();
        GameRecord gameRecord = GameRecord.builder()
                .userUUID(userUUID)
                .name(playGameBo.getName())
                .gameCode(playGameBo.getGameCode())
                .gameLevel(playGameBo.getGameLevel())
                .startTime(new Timestamp(new Date().getTime()))
                .build();
        gameRecordRepository.save(gameRecord);
        return userUUID;
    }


    /**
     * 游戏结束接口
     * @param userUUID
     * @return
     */
    public void gameEnd(String userUUID) {
        GameRecord gameRecord = gameRecordRepository.findByUserUUID(userUUID);

        if(ObjectUtils.isEmpty(gameRecord)) {
            return ;
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
    public Map<String, List<GameRecord>> gameRank(String gameCode) {
        List<GameRecord> rankList = gameRecordRepository.findGameRank(gameCode);
        Map<String, List<GameRecord>> mapRank= rankList.stream().collect(Collectors.groupingBy(e -> e.getGameLevel()));
        return mapRank;
    }
}
