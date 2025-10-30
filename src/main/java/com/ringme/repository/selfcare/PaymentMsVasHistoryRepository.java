package com.ringme.repository.selfcare;

import com.ringme.model.selfcare.PaymentMsVasHistory;
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
public interface PaymentMsVasHistoryRepository extends JpaRepository<PaymentMsVasHistory, Long> {
    @Query(value = """
            SELECT a FROM PaymentMsVasHistory a
                where (:isdn is null or a.isdn LIKE CONCAT('%', :isdn, '%'))
                and (:serviceCode is null or a.serviceCode LIKE CONCAT('%', :serviceCode, '%'))
                and (:status is null or a.status = :status)
                and (:url is null or a.url like concat('%', :url, '%'))
                and (:startDate is null or (a.createdAt >= :startDate and a.createdAt <= :endDate))
            order by a.createdAt desc
            """,
            countQuery = """
                    SELECT count(*) FROM PaymentMsVasHistory a
                        where (:isdn is null or a.isdn LIKE CONCAT('%', :isdn, '%'))
                        and (:serviceCode is null or a.serviceCode LIKE CONCAT('%', :serviceCode, '%'))
                        and (:status is null or a.status = :status)
                        and (:url is null or a.url like concat('%', :url, '%'))
                        and (:startDate is null or (a.createdAt >= :startDate and a.createdAt <= :endDate))
                    """)
    Page<PaymentMsVasHistory> search(
            Pageable pageable,
            @Param("isdn") String isdn,
            @Param("serviceCode") String serviceCode,
            @Param("status") Integer status,
            @Param("url") String url,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );
}
