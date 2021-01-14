package com.wpp.devtools.repository;

import com.wpp.devtools.domain.entity.Notebook;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @program: devtools-server
 * @description:
 * @author: wpp
 * @create: 2021-01-14
 **/
@Repository
public interface NotebookRepository extends JpaRepository<Notebook, String> {


    List<Notebook> findAllByUserIdOrderByIndexAscIndexTimestampDesc(String userId);

    @Transactional
    @Modifying
    @Query(value = "update notebook set index  = ?2, index_timestamp = ?3 where id = ?1 ", nativeQuery = true)
    void updateIndexById(String id, long index, long indexTimestamp);

}
