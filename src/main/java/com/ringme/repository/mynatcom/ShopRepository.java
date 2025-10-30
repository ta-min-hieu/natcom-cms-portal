package com.ringme.repository.mynatcom;

import com.ringme.model.mynatcom.Shop;
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
public interface ShopRepository extends JpaRepository<Shop, String> {
    @Query(value = """
            SELECT a FROM Shop a
            """,
            countQuery = """
                    SELECT count(*) FROM Shop
                    """)
    Page<Shop> search(Pageable pageable);

    @Query(value = """
            SELECT id, name FROM shop
            where (:input is null or name LIKE '%' || :input || '%')
            """, nativeQuery = true)
    List<String[]> ajaxSearch(@Param("input") String input);
}
