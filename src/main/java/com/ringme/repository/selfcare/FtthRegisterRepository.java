package com.ringme.repository.selfcare;

import com.ringme.model.selfcare.FtthRegister;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@Transactional
public interface FtthRegisterRepository extends JpaRepository<FtthRegister, Long> {
    @Query(value = """
            SELECT a FROM FtthRegister a
                where (:branchId is null or a.branchId = :branchId)
                and (:orderCode is null or a.orderCode = :orderCode)
                and (:packageCode is null or a.packageCode = :packageCode)
                and (:isdner is null or a.isdner LIKE CONCAT('%', :isdner, '%'))
                and (:isdnee is null or a.isdnee LIKE CONCAT('%', :isdnee, '%'))
                and (:name is null or a.name LIKE CONCAT('%', :name, '%'))
                and (:proviceId is null or a.proviceId = :proviceId)
                and (:status is null or a.status = :status)
                and (:startDate is null or (a.createdAt >= :startDate and a.createdAt <= :endDate))
                order by a.createdAt desc
            """,
            countQuery = """
                    SELECT count(*) FROM FtthRegister a
                    where (:branchId is null or a.branchId = :branchId)
                    and (:orderCode is null or a.orderCode = :orderCode)
                    and (:isdner is null or a.isdner LIKE CONCAT('%', :isdner, '%'))
                    and (:isdnee is null or a.isdnee LIKE CONCAT('%', :isdnee, '%'))
                    and (:name is null or a.name LIKE CONCAT('%', :name, '%'))
                    and (:proviceId is null or a.proviceId = :proviceId)
                    and (:status is null or a.status = :status)
                    and (:startDate is null or (a.createdAt >= :startDate and a.createdAt <= :endDate))
                    """)
    Page<FtthRegister> search(
            Pageable pageable,
            @Param("branchId") Long branchId,
            @Param("orderCode") String orderCode,
            @Param("packageCode") String packageCode,
            @Param("isdner") String isdner,
            @Param("isdnee") String isdnee,
            @Param("name") String name,
            @Param("proviceId") String proviceId,
            @Param("status") Integer status,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );

    @Query(value = """
            SELECT a FROM FtthRegister a
                where (:branchId is null or a.branchId = :branchId)
                and (:orderCode is null or a.orderCode = :orderCode)
                and (:packageCode is null or a.packageCode = :packageCode)
                and (:isdner is null or a.isdner LIKE CONCAT('%', :isdner, '%'))
                and (:isdnee is null or a.isdnee LIKE CONCAT('%', :isdnee, '%'))
                and (:name is null or a.name LIKE CONCAT('%', :name, '%'))
                and (:proviceId is null or a.proviceId = :proviceId)
                and (:status is null or a.status = :status)
                and (:startDate is null or (a.createdAt >= :startDate and a.createdAt <= :endDate))
                order by a.createdAt
            """)
    List<FtthRegister> getList(
            @Param("branchId") Long branchId,
            @Param("orderCode") String orderCode,
            @Param("packageCode") String packageCode,
            @Param("isdner") String isdner,
            @Param("isdnee") String isdnee,
            @Param("name") String name,
            @Param("proviceId") String proviceId,
            @Param("status") Integer status,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate
    );

    @Modifying
    @Query(value = """
            update FtthRegister set status = 1, callDate = now() where id = :id
                    """)
    void updateCallingStatus(@Param("id") long id);

    @Modifying
    @Query(value = """
            update FtthRegister
            set status = 3, surveyDate = now(), callNumber = COALESCE(callNumber, 0) + 1,
            customerFeedback = :customerFeedback
            where id = :id
            """)
    void updateCallSuccessStatus(@Param("id") long id,
                                 @Param("customerFeedback") String customerFeedback);

    @Modifying
    @Query(value = """
            update FtthRegister
            set status = 2, callFailDate = now(), callNumber = COALESCE(callNumber, 0) + 1,
            reasonCanNotContactSuccess = CONCAT(COALESCE(reasonCanNotContactSuccess, ''), :reasonCanNotContactSuccess)
            where id = :id
            """)
    void updateCallFailStatus(@Param("id") long id,
                              @Param("reasonCanNotContactSuccess") String reasonCanNotContactSuccess);

    @Modifying
    @Query(value = """
            update FtthRegister set status = 5, deployDate = now() where id = :id
                    """)
    void updateDeployStatus(@Param("id") long id);

    @Modifying
    @Query(value = """
            update FtthRegister set status = 4, signContractDate = now() where id = :id
                    """)
    void updateSignContractStatus(@Param("id") long id);

    @Modifying
    @Query(value = """
            update FtthRegister set status = 6, successDate = now(), remarkAdditionInfo = :remarkAdditionInfo where id = :id
                    """)
    void updateSuccessStatus(@Param("id") long id,
                             @Param("remarkAdditionInfo") String remarkAdditionInfo);

    @Modifying
    @Query(value = """
            update FtthRegister set status = 7, cancelDate = now(), remarkAdditionInfo = :remarkAdditionInfo where id = :id
                    """)
    void updateCancelStatus(@Param("id") long id,
                            @Param("remarkAdditionInfo") String remarkAdditionInfo);

    @Modifying
    @Query(value = """
            update FtthRegister set branchId = :branchId where id = :id
                    """)
    void updateBranch(@Param("id") long id,
                      @Param("branchId") long branchId);
}
