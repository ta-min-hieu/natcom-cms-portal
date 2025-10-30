package com.ringme.dao;

import com.ringme.dto.selfcare.NatcashPaymentMobileServiceVasHistoryDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Log4j2
@Repository
public class NatcashHistoryDao {
    @Autowired
    @Qualifier("selfcareJdbcTemplate")
    NamedParameterJdbcTemplate jdbcTemplate;

    public String getRequestIdByOrderNumber(String orderNumber) {
        try {
            String sql = """
                    select request_id
                    from natcash_merchant_payment_history
                     where order_number = :order_number
                    """;

            List<String> list = jdbcTemplate.query(sql, new MapSqlParameterSource()
                            .addValue("order_number", orderNumber),
                    (rs, rowNum) -> {
                        return rs.getString("request_id");
                    });

            if(list != null && !list.isEmpty())
                return list.getFirst();

        } catch (Exception e) {
            log.error("orderNumber: {}, error: {}", orderNumber, e.getMessage(), e);
        }
        return null;
    }

    public String getAmountByOrderNumber(String orderNumber) {
        try {
            String sql = """
                    select amount
                    from natcash_merchant_payment_history
                     where order_number = :order_number
                    """;

            List<String> list = jdbcTemplate.query(sql, new MapSqlParameterSource()
                            .addValue("order_number", orderNumber),
                    (rs, rowNum) -> {
                        return rs.getString("amount").split(" ")[0].replace(",", "");
                    });

            if(list != null && !list.isEmpty())
                return list.getFirst();

        } catch (Exception e) {
            log.error("orderNumber: {}, error: {}", orderNumber, e.getMessage(), e);
        }
        return null;
    }

    public List<NatcashPaymentMobileServiceVasHistoryDto> getNatcashPaymentHistory(int page, int size, String isdn, String packageCode, String orderNumber, String errorCode, Date startDate, Date endDate) {
        String sql = """
                    select isdn, package_code, money, error_code, order_number, user_msg, created_at
                    from ({{sql}}) AS t
                    order by created_at desc
                    LIMIT :size OFFSET :offset
                    """.replace("{{sql}}", sqlGetNatcashPaymentHistory());

        try {
            List<NatcashPaymentMobileServiceVasHistoryDto> list = jdbcTemplate.query(sql, new MapSqlParameterSource()
                            .addValue("size", size)
                            .addValue("offset", (page - 1) * size)
                            .addValue("isdn", isdn)
                            .addValue("packageCode", packageCode)
                            .addValue("orderNumber", orderNumber)
                            .addValue("errorCode", errorCode)
                            .addValue("startDate", startDate)
                            .addValue("endDate", endDate)
                    ,(rs, rowNum) -> {
                        NatcashPaymentMobileServiceVasHistoryDto dto = new NatcashPaymentMobileServiceVasHistoryDto();
                        dto.setIsdn(rs.getString("isdn"));
                        dto.setPackageCode(rs.getString("package_code"));
                        dto.setMoney(rs.getDouble("money"));
                        dto.setErrorCode(rs.getString("error_code"));
                        dto.setOrderNumber(rs.getString("order_number"));
                        dto.setUserMsg(rs.getString("user_msg"));
                        dto.setCreatedAt(rs.getDate("created_at"));
                        return dto;
                    });

            log.info("success sql: {}, page: {}, size: {}, packageCode: {}, orderNumber: {}, errorCode: {}, startDate: {}, endDate: {}", sql, page, size, packageCode, orderNumber, errorCode, startDate, endDate);
            return list;
        } catch (Exception e) {
            log.error("sql: {}, page: {}, size: {}, packageCode: {}, orderNumber: {}, errorCode: {}, startDate: {}, endDate: {}, error: {}", sql, page, size, packageCode, orderNumber, errorCode, startDate, endDate, e.getMessage(), e);
        }
        return null;
    }

    public int countNatcashPaymentHistory(String isdn, String packageCode, String orderNumber, String errorCode, Date startDate, Date endDate) {
        String sql = """
                    select count(*)
                    from ({{sql}})
                    as a
                    """.replace("{{sql}}", sqlGetNatcashPaymentHistory());

        try {
            Integer rs = jdbcTemplate.queryForObject(sql, new MapSqlParameterSource()
                            .addValue("isdn", isdn)
                            .addValue("packageCode", packageCode)
                            .addValue("orderNumber", orderNumber)
                            .addValue("errorCode", errorCode)
                            .addValue("startDate", startDate)
                            .addValue("endDate", endDate),
                    Integer.class);

            if(rs == null)
                return 0;
            return rs;
        } catch (Exception e) {
            log.error("ERROR|{}", e.getMessage(), e);
        }
        return 0;
    }

    private String sqlGetNatcashPaymentHistory() {
        return """
                SELECT isdn, package_code, money, error_code, order_number, user_msg, created_at
                FROM natcash_payment_mobile_service_vas_history a
                where (:isdn is null or a.isdn = :isdn)
                and (:packageCode is null or a.package_code LIKE CONCAT('%', :packageCode, '%'))
                and (:orderNumber is null or a.order_number = :orderNumber)
                and (:errorCode is null or a.error_code = :errorCode)
                and (:startDate is null or (a.created_at >= :startDate and a.created_at <= :endDate))
                
                union all
                
                SELECT isdner `isdn`, package_code, money, error_code, order_number, user_msg, created_at
                FROM natcash_share_plan_history b
                where (:isdn is null or b.isdner = :isdn)
                and (:packageCode is null or b.package_code LIKE CONCAT('%', :packageCode, '%'))
                and (:orderNumber is null or b.order_number = :orderNumber)
                and (:errorCode is null or b.error_code = :errorCode)
                and (:startDate is null or (b.created_at >= :startDate and b.created_at <= :endDate))
                """;
    }
}
