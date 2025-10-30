package com.ringme.repository.selfcare;

import com.ringme.model.selfcare.ServiceCategory;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, Long> {
    @Query(value = """
            SELECT a FROM ServiceCategory a
                where (:name is null or a.name LIKE CONCAT('%', :name, '%'))
                and (:categoryLevel is null or a.categoryLevel = :categoryLevel)
                and (:status is null or a.status = :status)
            order by a.createdAt desc
            """,
            countQuery = """
                    SELECT count(*) FROM ServiceCategory a
                        where (:name is null or a.name LIKE CONCAT('%', :name, '%'))
                        and (:categoryLevel is null or a.categoryLevel = :categoryLevel)
                        and (:status is null or a.status = :status)
                    """)
    Page<ServiceCategory> search(
            Pageable pageable,
            @Param("name") String name,
            @Param("categoryLevel") Integer categoryLevel,
            @Param("status") Integer status);

    @Query(value = """
            SELECT id AS `id`, name AS `text` FROM sc_service_category
            where (:input is null or name like concat('%', :input, '%'))
            and (:noChild is null or 
                id NOT IN (
                    SELECT DISTINCT parent_id
                    FROM selfcare.sc_service_category
                    WHERE parent_id IS NOT NULL
                )                 
            )
            and status = 1
            """, nativeQuery = true)
    List<String[]> ajaxSearch(@Param("input") String input,
                              @Param("noChild") Boolean noChild);
}
