package com.ringme.repository.selfcare;

import com.ringme.model.selfcare.SelfcareSoapWS;
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
public interface SelfcareSoapWSRepository extends JpaRepository<SelfcareSoapWS, Long> {

    @Query(value = """
                    SELECT a FROM SelfcareSoapWS a
                    where (:name is null or a.name like concat('%', :name, '%'))
                    and (:status is null or a.status = :status)
                    order by a.updatedAt desc
                """,
            countQuery = """
                            SELECT count(*) FROM SelfcareSoapWS a
                            where (:name is null or a.name like concat('%', :name, '%'))
                            and (:status is null or a.status = :status)
                            """)
    Page<SelfcareSoapWS> search(Pageable pageable,
                                @Param("name") String name,
                                @Param("status") Integer status);

    @Query(value = """
            SELECT id AS `id`, name AS `text` FROM sc_soap_ws
            where (:input is null or name like concat('%', :input, '%'))
            and status = 1
            """, nativeQuery = true)
    List<String[]> ajaxSearch(@Param("input") String input);
}
