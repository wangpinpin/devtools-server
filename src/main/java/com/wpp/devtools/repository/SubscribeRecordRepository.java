package com.wpp.devtools.repository;

import com.wpp.devtools.domain.entity.SubscribeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @program: devtools-server
 * @description:
 * @author: wpp
 * @create: 2020-09-21
 **/
@Repository
public interface SubscribeRecordRepository extends JpaRepository<SubscribeRecord, String> {

}
