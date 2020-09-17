package com.wpp.devtools.repository;

import com.wpp.devtools.domain.entity.Activity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @program: devtools-server
 * @description:
 * @author: wpp
 * @create: 2020-09-17
 **/
@Repository
public interface ActivityRepository extends JpaRepository<Activity, String> {

    List<Activity> findAllByOrderBySort();

}
