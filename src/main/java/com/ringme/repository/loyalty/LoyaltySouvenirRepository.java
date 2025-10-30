package com.ringme.repository.loyalty;

import com.ringme.model.loyalty.LoyaltySouvenir;
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
public interface LoyaltySouvenirRepository extends JpaRepository<LoyaltySouvenir, Long> {
    @Query(value = """
            SELECT a FROM LoyaltySouvenir a
                where (:title is null or a.title LIKE CONCAT('%', :title, '%'))
                and (:status is null or a.status = :status)
                and (:dateStart is null or (a.dateExpired >= :dateStart and a.dateExpired <= :dateEnd))
            order by a.updatedAt desc
            """,
            countQuery = """
                    SELECT count(*) FROM LoyaltySouvenir a
                        where (:title is null or a.title LIKE CONCAT('%', :title, '%'))
                        and (:status is null or a.status = :status)
                        and (:dateStart is null or (a.dateExpired >= :dateStart and a.dateExpired <= :dateEnd))
                    """)
    Page<LoyaltySouvenir> search(
            Pageable pageable,
            @Param("title") String title,
            @Param("status") Integer status,
            @Param("dateStart") Date dateStart,
            @Param("dateEnd") Date dateEnd);

    @Modifying
    @Query(value = """
            update LoyaltySouvenir set quantityReal = quantityReal + 1 where id = :id
                    """)
    void updateNumberOfQuantityReal(@Param("id") long id);
}
