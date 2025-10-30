package com.ringme.model.mynatcom;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "shop")
public class Shop {
    @Id
    @Column(name = "ID", length = 32, nullable = false)
    private String id;

    @Column(name = "NAME", length = 100, nullable = false)
    private String name;

    @Column(name = "ADDR", length = 200)
    private String addr;

    @Column(name = "OPEN_TIME", length = 20)
    private String openTime;

    @Column(name = "LONGITUDE", precision = 9, scale = 6)
    private BigDecimal longitude;

    @Column(name = "LATITUDE", precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(name = "PROVINCE_ID")
    private Integer provinceId;

    @Column(name = "DISTRICT_ID")
    private Integer districtId;

    @Column(name = "ISDN", length = 20)
    private String isdn;

    @Column(name = "TYPE")
    private Byte type = 0;

    @Column(name = "STATUS")
    private Byte status = 1;

    @Column(name = "CREATED_TIME", columnDefinition = "DATETIME(6)")
    private Date createdTime;

    @Column(name = "CREATED_BY", length = 256)
    private String createdBy;

    @Column(name = "LAST_UPDATED_TIME", columnDefinition = "DATETIME(6)")
    private Date lastUpdatedTime;

    @Column(name = "LAST_UPDATED_BY", length = 256)
    private String lastUpdatedBy;

    @Column(name = "LA")
    private Integer la;

    @Column(name = "LO")
    private Integer lo;

    @Column(name = "SHOP_ORDER")
    private Byte shopOrder;
}
