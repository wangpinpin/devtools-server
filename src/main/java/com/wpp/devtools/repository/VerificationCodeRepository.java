package com.wpp.devtools.repository;

import com.wpp.devtools.domain.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @program: devtools-server
 * @description:
 * @author: wpp
 * @create: 2020-09-17
 **/
@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, String> {

    VerificationCode findByEmailAndCode(String email, String code);

}
