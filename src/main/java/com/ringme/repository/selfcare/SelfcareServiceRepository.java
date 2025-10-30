package com.ringme.repository.selfcare;

import com.ringme.model.selfcare.SelfcareService;
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
public interface SelfcareServiceRepository extends JpaRepository<SelfcareService, Long> {
    @Query(value = """
            SELECT a FROM SelfcareService a
                where (:name is null or a.name LIKE CONCAT('%', :name, '%'))
                and (:serviceCategoryId is null or a.serviceCategoryId = :serviceCategoryId)
                and (:status is null or a.status = :status)
            order by a.updatedAt desc
            """,
            countQuery = """
                    SELECT count(*) FROM SelfcareService a
                        where (:name is null or a.name LIKE CONCAT('%', :name, '%'))
                        and (:serviceCategoryId is null or a.serviceCategoryId = :serviceCategoryId)
                        and (:status is null or a.status = :status)
                    """)
    Page<SelfcareService> search(
            Pageable pageable,
            @Param("name") String name,
            @Param("serviceCategoryId") Long serviceCategoryId,
            @Param("status") Integer status);

    @Query(value = """
            SELECT id AS `id`, name AS `text` FROM sc_service
            where (:input is null or name like concat('%', :input, '%'))
            and status = 1
            """, nativeQuery = true)
    List<String[]> ajaxSearch(@Param("input") String input);
}
