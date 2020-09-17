package com.wpp.devtools.repository;

import com.wpp.devtools.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @program: devtools-server
 * @description:
 * @author: wpp
 * @create: 2020-08-17
 **/
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findByEmailAndPassword(String email, String paasword);

    User findByEmail(String email);

}
