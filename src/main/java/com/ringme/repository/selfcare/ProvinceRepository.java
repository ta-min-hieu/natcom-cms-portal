package com.ringme.repository.selfcare;

import com.ringme.model.selfcare.Province;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface ProvinceRepository extends JpaRepository<Province, String> {

    @Query(value = """
            SELECT id AS `id`, name AS `text` FROM province
            where (:input is null or name like concat('%', :input, '%'))
            """, nativeQuery = true)
    List<String[]> ajaxSearch(@Param("input") String input);
}
