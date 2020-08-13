package com.wpp.devtools.repository;

import com.wpp.devtools.domain.entity.Wb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @program: volvo-sever
 * @description:
 * @author: wpp
 * @create: 2020-08-13
 **/
@Repository
public interface WbRepository extends JpaRepository<Wb, Long> {

}
