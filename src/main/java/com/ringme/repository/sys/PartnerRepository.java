package com.ringme.repository.sys;

import com.ringme.model.sys.Partner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Integer> {
    @Query(value = """
            SELECT p from Partner p where (:name is null or p.name LIKE CONCAT('%', :name, '%'))
            and status != -1
            ORDER BY p.createdAt DESC
            """,
            countQuery = """
                    SELECT count(p) from Partner p where (:name is null or p.name LIKE CONCAT('%', :name, '%'))
                    and status != -1
                    """)
    Page<Partner> get(@Param("name") String name, Pageable pageable);

    @Query(value = """
            SELECT p.id, p.name, p.code FROM Partner p
            WHERE (:input is null or (p.name LIKE CONCAT('%', :input, '%') OR p.code LIKE CONCAT('%', :input, '%')))
            and status = 1
            """)
    List<String[]> ajaxSearch(Pageable pageable, @Param("input") String input);

    @Modifying
    @Query(value = "update Partner p set p.status = -1 where p.id = :id")
    void deleteById(@Param("id") Integer id);

    @Modifying
    @Query(value = "update Partner p set p.status = 1 where p.id = :id")
    void activeById(@Param("id") Integer id);

    @Modifying
    @Query(value = "update Partner p set p.status = 0 where p.id = :id")
    void deactiveById(@Param("id") Integer id);

    @Query(value = """
            SELECT p FROM Partner p
            where (:name is null or p.name LIKE CONCAT('%', :name, '%'))
            and p.status = 1
            """)
    Partner findByName(@Param("name") String name);

    @Query(value = """
            SELECT p FROM Partner p
            where (:partnerCode is null or p.code LIKE CONCAT('%', :partnerCode, '%'))
            and p.status = 1
            """)
    Partner findByPartnerCode(@Param("partnerCode") String partnerCode);

    @Modifying
    @Query(value = "select p from Partner p where p.status = 1")
    List<Partner> findAllPartner();

    @Query(value = """
            SELECT p FROM Partner p
            where (:partnerId is null or p.code LIKE CONCAT('%', :partnerId, '%'))
            and p.status = 1
            """)
    Optional<Partner> findNamePartnerById(String partnerId);

    @Query(value = "SELECT p FROM Partner p where p.code = :partnerCode")
    Partner getPartnerByPartnerCode(@Param("partnerCode") String partnerCode);
}
