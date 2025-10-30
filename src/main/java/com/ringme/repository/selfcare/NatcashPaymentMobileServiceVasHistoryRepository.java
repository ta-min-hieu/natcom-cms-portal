package com.ringme.repository.selfcare;

import com.ringme.model.selfcare.NatcashPaymentMobileServiceVasHistory;
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
public interface NatcashPaymentMobileServiceVasHistoryRepository extends JpaRepository<NatcashPaymentMobileServiceVasHistory, Long> {
    @Query(value = """
            SELECT a FROM NatcashPaymentMobileServiceVasHistory a
                where (:isdn is null or a.isdn = :isdn)
                and (:packageCode is null or a.packageCode LIKE CONCAT('%', :packageCode, '%'))
                and (:orderNumber is null or a.orderNumber = :orderNumber)
                and (:errorCode is null or a.errorCode = :errorCode)
                and (:startDate is null or (a.createdAt >= :startDate and a.createdAt <= :endDate))
                order by a.createdAt desc
            """,
            countQuery = """
                    SELECT count(*) FROM NatcashPaymentMobileServiceVasHistory a
                    where (:isdn is null or a.isdn = :isdn)
                and (:packageCode is null or a.packageCode LIKE CONCAT('%', :packageCode, '%'))
                and (:orderNumber is null or a.orderNumber = :orderNumber)
                and (:errorCode is null or a.errorCode = :errorCode)
                and (:startDate is null or (a.createdAt >= :startDate and a.createdAt <= :endDate))
                    """)
    Page<NatcashPaymentMobileServiceVasHistory> search(
            Pageable pageable,
            @Param("isdn") String isdn,
            @Param("packageCode") String packageCode,
            @Param("errorCode") String errorCode,
            @Param("orderNumber") String orderNumber,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );
}
