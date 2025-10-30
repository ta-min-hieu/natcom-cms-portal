package com.ringme.repository.selfcare;

import com.ringme.model.selfcare.ScSubService;
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
public interface ScSubServiceRepository extends JpaRepository<ScSubService, Long> {
    @Query(value = """
            SELECT a FROM ScSubService a
                where (:name is null or a.name LIKE CONCAT('%', :name, '%'))
                and (:code is null or a.code = :code)
                and (:status is null or a.status = :status)
                and (:serviceId is null or a.serviceId = :serviceId)
            order by a.updatedAt desc
            """,
            countQuery = """
                    SELECT count(*) FROM ScSubService a
                        where (:name is null or a.name LIKE CONCAT('%', :name, '%'))
                        and (:code is null or a.code = :code)
                        and (:status is null or a.status = :status)
                        and (:serviceId is null or a.serviceId = :serviceId)
                    """)
    Page<ScSubService> search(
            Pageable pageable,
            @Param("name") String name,
            @Param("code") String code,
            @Param("status") Integer status,
            @Param("serviceId") Long serviceId
            );

    @Query(value = """
            SELECT a FROM ScSubService a
                where a.serviceId = :serviceId
            order by a.updatedAt desc
            """)
    List<ScSubService> getList(
            @Param("serviceId") long serviceId
            );
}
