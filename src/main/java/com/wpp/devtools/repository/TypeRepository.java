package com.wpp.devtools.repository;

import com.wpp.devtools.domain.entity.Type;
import com.wpp.devtools.domain.enums.TypeEnum;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @program: volvo-sever
 * @description:
 * @author: wpp
 * @create: 2020-08-24
 **/
@Repository
public interface TypeRepository extends JpaRepository<Type, String> {

    List<Type> findByTypeOrderBySort(TypeEnum t);
}
