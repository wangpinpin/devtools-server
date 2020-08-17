package com.wpp.devtools.repository;

import com.wpp.devtools.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @program: volvo-sever
 * @description:
 * @author: wpp
 * @create: 2020-08-17
 **/
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findByOpenid(String openId);
}
