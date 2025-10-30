package com.ringme.repository.voucher;

import com.ringme.model.voucher.VoucherTopic;
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
public interface VoucherTopicRepository extends JpaRepository<VoucherTopic, Long> {
    @Query(value = """
            SELECT a FROM VoucherTopic a
                where (:name is null or a.name LIKE CONCAT('%', :name, '%'))
                and (:status is null or a.status = :status)
            order by a.updatedAt desc
            """,
            countQuery = """
                    SELECT count(*) FROM Merchant a
                        where (:name is null or a.name LIKE CONCAT('%', :name, '%'))
                        and (:status is null or a.status = :status)
            order by a.updatedAt desc
                    """)
    Page<VoucherTopic> search(
            Pageable pageable,
            @Param("name") String name,
            @Param("status") Integer status);

    @Query(value = """
            SELECT id AS `id`, name AS `text` FROM voucher_topic
            where (:input is null or name like concat('%', :input, '%')) and status = 1
            """, nativeQuery = true)
    List<String[]> ajaxSearch(@Param("input") String input);
}
