package com.ringme.dto.loyalty;

import com.ringme.common.Helper;
import com.ringme.dto.OptionDto;
import com.ringme.model.voucher.Merchant;
import com.ringme.model.voucher.SubMerchant;
import com.ringme.model.voucher.VoucherGroup;
import com.ringme.model.voucher.VoucherTopic;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class VoucherGroupDto {
    private Long id;
    private Long topicId;
    private Long idMerchant;
    private Integer status; // 0 - Lock, 1 - Active
    private String name;
    private Integer quantityTotal;
    private Integer quantityExchanged;
    private Integer point;
    private String startDate;
    private String endDate;
    private String description;
    private String imageUrl;
    private Double amount;
    private String discountUnit;
    private String pointUnit;

    private Date dateExpiredStart;
    private Date dateExpiredEnd;

    private Set<String> subMerchantIds;
    private List<OptionDto> subMerchantOptions;

    private VoucherTopic topic;
    private Merchant merchant;

    public VoucherGroupDto(VoucherGroup voucherGroup) {
        this.id = voucherGroup.getId();
        this.topicId = voucherGroup.getTopicId();
        this.idMerchant = voucherGroup.getIdMerchant();
        this.status = voucherGroup.getStatus();
        this.name = voucherGroup.getName();
        this.quantityTotal = voucherGroup.getQuantityTotal();
        this.quantityExchanged = voucherGroup.getQuantityExchanged();
        this.point = voucherGroup.getPoint();
        startDate = Helper.convertDateToString(voucherGroup.getStartDate());
        endDate = Helper.convertDateToString(voucherGroup.getEndDate());
        description = voucherGroup.getDescription();
        imageUrl = voucherGroup.getImageUrl();
        amount = voucherGroup.getAmount();
        discountUnit = voucherGroup.getDiscountUnit();
        pointUnit = voucherGroup.getPointUnit();

        topic = voucherGroup.getTopic();
        merchant = voucherGroup.getMerchant();

        List<SubMerchant> subMerchants = voucherGroup.getSubMerchants();
        List<OptionDto> list = new ArrayList<>();
        if(voucherGroup.getSubMerchants() != null) {
            for (SubMerchant subMerchant : subMerchants) {
                String value = String.valueOf(subMerchant.getId());
                String text = subMerchant.getName();
                list.add(new OptionDto(value, text));
            }
        }
        subMerchantOptions = list;
    }
}
