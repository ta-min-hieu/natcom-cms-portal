package com.ringme.repository.voucher;

import com.ringme.model.voucher.VoucherGroupSubMerchant;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface VoucherGroupSubMerchantRepository extends JpaRepository<VoucherGroupSubMerchant, Long> {
    @Modifying
    @Query(value = """
            delete from VoucherGroupSubMerchant where idVoucherGroup = :id
                    """)
    void deleteAllByVoucherGroupId(@Param("id") long id);
}
