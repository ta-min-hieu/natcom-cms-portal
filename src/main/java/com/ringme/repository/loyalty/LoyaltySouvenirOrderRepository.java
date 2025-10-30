package com.ringme.repository.loyalty;

import com.ringme.model.loyalty.LoyaltySouvenirOrder;
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
public interface LoyaltySouvenirOrderRepository extends JpaRepository<LoyaltySouvenirOrder, Long> {
    @Query(value = """
            SELECT a FROM LoyaltySouvenirOrder a
            where (:isdn is null or a.isdn like concat("%", :isdn, "%"))
            and (:orderCode is null or a.orderCode like concat("%", :orderCode, "%"))
            and (:title is null or a.title like concat("%", :title, "%"))
            and (:status is null or a.status = :status)
            and (:showroomId is null or a.showroomId = :showroomId)
            order by a.createdAt desc
            """,
            countQuery = """
                    SELECT count(*) FROM LoyaltySouvenirOrder a
                    where (:isdn is null or a.isdn like concat("%", :isdn, "%"))
                    and (:orderCode is null or a.orderCode like concat("%", :orderCode, "%"))
                    and (:title is null or a.title like concat("%", :title, "%"))
                    and (:status is null or a.status = :status)
                    and (:showroomId is null or a.showroomId = :showroomId)
                    """)
    Page<LoyaltySouvenirOrder> search(Pageable pageable,
                                      @Param("showroomId") String showroomId,
                                      @Param("isdn") String isdn,
                                      @Param("orderCode") String orderCode,
                                      @Param("title") String title,
                                      @Param("status") Integer status
                                      );

    @Modifying
    @Query(value = """
            update LoyaltySouvenirOrder set status = 2, processDate = now() where id = :id
                    """)
    void updateProcessingStatus(@Param("id") long id);

    @Modifying
    @Query(value = """
            update LoyaltySouvenirOrder set status = 3, confirmDate = now() where id = :id
                    """)
    void updateValidStatus(@Param("id") long id);

    @Modifying
    @Query(value = """
            update LoyaltySouvenirOrder set status = 4, receiveDate = now() where id = :id
                    """)
    void updateReceivedStatus(@Param("id") long id);

    @Modifying
    @Query(value = """
            update LoyaltySouvenirOrder set status = 5, cancelDate = now() where id = :id
                    """)
    void updateCancelStatus(@Param("id") long id);
}
