package com.ringme.repository.sys;


import com.ringme.model.sys.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

//    @Query(value = "SELECT * FROM menu where menu.name =?1 order by modified_date desc", nativeQuery = true)
//    Optional<Menu> findMenuByName(String name);

//    @Query(value = "SELECT * FROM menu WHERE menu.name = (SELECT value FROM message WHERE key = CONCAT('menu.', ?1)) ORDER BY modified_date DESC", nativeQuery = true)
//    Optional<Menu> findMenuByName(String name);
@Query(value = "SELECT * FROM tbl_menu " +
        "where (:id is null or id = :id) or (:id is null or parent_name_id = :id) " +
        "ORDER BY parent_name_id DESC, order_num ASC, created_at DESC",
        countQuery = "SELECT count(*) FROM tbl_menu " +
                "where (:id is null or id = :id) or (:id is null or parent_name_id = :id) ",
        nativeQuery = true)
Page<Menu> getAll(@Param("id") Long id, Pageable pageable);

    @Query(value = "SELECT * FROM tbl_menu order by order_num",nativeQuery = true)
    List<Menu> findAll();

    List<Menu> findByParentNameIsNullOrderByOrderNumAsc();

    @Query(value = "SELECT * FROM tbl_menu where parent_name_id = :parentId order by order_num",nativeQuery = true)
    List<Menu> findByParentNameId(@Param("parentId") Long parentId);

    List<Menu> findAllById(Iterable<Long> longs);

    <S extends Menu> S save(S entity);

    void deleteById(Long aLong);

    void delete(Menu entity);
    @Query(value = "SELECT id AS `id`, name_en AS `text` FROM tbl_menu " +
            "WHERE (:input is null or (id = :input OR name_en LIKE CONCAT('%', :input, '%'))) LIMIT 20", nativeQuery = true)
    List<String[]> ajaxSearch(@Param("input") String input);

    @Query(value = "SELECT name_en FROM tbl_menu where id = :id order by order_num",nativeQuery = true)
    String getNameEn(@Param("id") Long id);
}
