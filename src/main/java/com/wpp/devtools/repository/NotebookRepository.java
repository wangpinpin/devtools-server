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


    List<Notebook> findAllByUserIdOrderBySortAsc(String userId);

    @Transactional
    @Modifying
    @Query(value = "update notebook set sort = sort + ?2 where user_id = ?1  and sort >= ?4 and sort < ?3 ", nativeQuery = true)
    void updateSortAllTopById(String userId, long deviation, long oldIndex, long newIndex);

    @Transactional
    @Modifying
    @Query(value = "update notebook set sort = sort + ?2 where user_id = ?1  and sort > ?3 and sort <= ?4 ", nativeQuery = true)
    void updateSortAllDownById(String userId, long deviation, long oldIndex, long newIndex);

    @Transactional
    @Modifying
    @Query(value = "update notebook set sort  = ?3 where id = ?1  and sort = ?2 ", nativeQuery = true)
    void updateSortById(String id, long oldIndex, long newIndex);

}
