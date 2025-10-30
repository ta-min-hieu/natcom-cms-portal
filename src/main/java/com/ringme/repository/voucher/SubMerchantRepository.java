package com.ringme.repository.voucher;

import com.ringme.model.voucher.SubMerchant;
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
public interface SubMerchantRepository extends JpaRepository<SubMerchant, Long> {
    @Query(value = """
            SELECT a FROM SubMerchant a
                where (:name is null or a.name LIKE CONCAT('%', :name, '%'))
                        and (:status is null or a.status = :status)
            order by a.updatedAt desc
            """,
            countQuery = """
                    SELECT count(*) FROM SubMerchant a
                        where (:name is null or a.name LIKE CONCAT('%', :name, '%'))
                        and (:status is null or a.status = :status)
                    """)
    Page<SubMerchant> search(
            Pageable pageable,
            @Param("name") String name,
            @Param("status") Integer status);

    @Query(value = """
            SELECT id AS `id`, name AS `text` FROM sub_merchant
            where (:input is null or name like concat('%', :input, '%'))
            and status = 1
            and id_merchant = :idMerchant
            """, nativeQuery = true)
    List<String[]> ajaxSearch(@Param("input") String input,
                              @Param("idMerchant") String idMerchant);
}
