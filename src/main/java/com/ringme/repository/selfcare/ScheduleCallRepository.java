package com.ringme.repository.selfcare;

import com.ringme.model.selfcare.ScheduleCall;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
@Transactional
public interface ScheduleCallRepository extends JpaRepository<ScheduleCall, Long> {
    @Query(value = """
            SELECT a FROM ScheduleCall a
                where (:supportType is null or a.supportType = :supportType)
                and (:isdn is null or a.isdn = :isdn)
                and (:status is null or a.status = :status)
                and (:startDate is null or (a.createdAt >= :startDate and a.createdAt <= :endDate))
                order by a.updatedAt desc
            """,
            countQuery = """
                    SELECT count(*) FROM ScheduleCall a
                    where (:supportType is null or a.supportType = :supportType)
                    and (:isdn is null or a.isdn = :isdn)
                    and (:status is null or a.status = :status)
                    and (:startDate is null or (a.createdAt >= :startDate and a.createdAt <= :endDate))
                    """)
    Page<ScheduleCall> search(
            Pageable pageable,
            @Param("supportType") String supportType,
            @Param("isdn") String isdn,
            @Param("status") Integer status,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );

    @Modifying
    @Query(value = """
            update ScheduleCall set status = 1, processDate = now() where id = :id
                    """)
    void updateProcessStatus(@Param("id") long id);

    @Modifying
    @Query(value = """
            update ScheduleCall set status = 2, successDate = now(), note = :note where id = :id
                    """)
    void updateSuccessStatus(@Param("id") long id,
                             @Param("note") String note);

    @Modifying
    @Query(value = """
            update ScheduleCall set status = 3, cancelDate = now(), note = :note where id = :id
                    """)
    void updateCancelStatus(@Param("id") long id,
                            @Param("note") String note);
}
