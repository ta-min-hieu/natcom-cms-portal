package com.ringme.repository.voucher;

import com.ringme.model.voucher.VoucherGroup;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
@Transactional
public interface VoucherGroupRepository extends JpaRepository<VoucherGroup, Long> {
    @Query(value = """
            SELECT a FROM VoucherGroup a
                where (:title is null or a.name LIKE CONCAT('%', :title, '%'))
                and (:status is null or a.status = :status)
                and (:dateStart is null or (a.endDate >= :dateStart and a.endDate <= :dateEnd))
            order by a.updatedAt desc
            """,
            countQuery = """
                    SELECT count(*) FROM VoucherGroup a
                        where (:title is null or a.name LIKE CONCAT('%', :title, '%'))
                        and (:status is null or a.status = :status)
                        and (:dateStart is null or (a.endDate >= :dateStart and a.endDate <= :dateEnd))
                    """)
    Page<VoucherGroup> search(
            Pageable pageable,
            @Param("title") String title,
            @Param("status") Integer status,
            @Param("dateStart") Date dateStart,
            @Param("dateEnd") Date dateEnd);
}
