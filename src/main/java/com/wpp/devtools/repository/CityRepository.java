package com.wpp.devtools.repository;

import com.wpp.devtools.domain.entity.City;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @program: devtools-server
 * @description:
 * @author: wpp
 * @create: 2020-10-15
 **/
@Repository
public interface CityRepository extends JpaRepository<City, String> {

    List<City> findByPid(int pid);
}
