package com.ringme.repository.voucher;

import com.ringme.model.voucher.Voucher;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    @Query(value = """
            SELECT a FROM Voucher a
                where (:voucherGroupName is null or a.voucherGroupName LIKE CONCAT('%', :voucherGroupName, '%'))
                and (:isdn is null or a.isdn = :isdn)
                and (:topicId is null or a.topicId = :topicId)
                and (:merchantId is null or a.idMerchant = :merchantId)
            order by a.updatedAt desc
            """,
            countQuery = """
                    SELECT count(*) FROM Voucher a
                    where (:voucherGroupName is null or a.voucherGroupName LIKE CONCAT('%', :voucherGroupName, '%'))
                    and (:isdn is null or a.isdn = :isdn)
                    and (:topicId is null or a.topicId = :topicId)
                    and (:merchantId is null or a.idMerchant = :merchantId)
                    """)
    Page<Voucher> search(
            Pageable pageable,
            @Param("isdn") String isdn,
            @Param("voucherGroupName") String voucherGroupName,
            @Param("topicId") Long topicId,
            @Param("merchantId") Long idMerchant);

    @Modifying
    @Query(value = """
            update Voucher set status = 1, usedDate = now() where id = :id
                    """)
    void updateUsedStatus(@Param("id") long id);
}
