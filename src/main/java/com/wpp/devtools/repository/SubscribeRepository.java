package com.wpp.devtools.repository;

import com.wpp.devtools.domain.entity.Subscribe;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @program: devtools-server
 * @description:
 * @author: wpp
 * @create: 2020-09-17
 **/
@Repository
public interface SubscribeRepository extends JpaRepository<Subscribe, String> {

    @Query(value = "SELECT a.* FROM `subscribe` a "
            + "LEFT JOIN `subscribe_record` b ON b.`subscribe_id` = a.`id` AND TO_DAYS(b.`create_time`) = TO_DAYS(NOW()) "
            + "WHERE a.hour = ?1 "
            + "AND b.id IS NULL "
            + "AND a.`enabled` = 1 "
            + "AND a.cancel = 0 ", nativeQuery = true)
    List<Subscribe> findAllSubscribe(int hour);

    @Query(value = "SELECT * FROM `subscribe` WHERE email = ?1 ", nativeQuery = true)
    Subscribe findCancelByEmail(String email);

    @Query(value = "SELECT a.*, COUNT(b.id) `count` FROM `subscribe` a "
            + "LEFT JOIN `subscribe_record` b ON b.`subscribe_id` = a.`id` "
            + "WHERE a.user_id = ?1 "
            + "GROUP BY a.id "
            + "ORDER BY a.create_time ASC ", nativeQuery = true)
    List<Map<String, Object>> findList(String userId);
}
