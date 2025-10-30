package com.ringme.service.loyalty;

import com.ringme.common.Helper;
import com.ringme.common.UploadFile;
import com.ringme.config.AppConfig;
import com.ringme.config.LocaleFactory;
import com.ringme.dto.loyalty.VoucherGroupDto;
import com.ringme.model.voucher.VoucherGroup;
import com.ringme.model.voucher.VoucherGroupSubMerchant;
import com.ringme.repository.voucher.VoucherGroupRepository;
import com.ringme.repository.voucher.VoucherGroupSubMerchantRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Path;
import java.util.Set;

@Log4j2
@Service
public final class VoucherGroupServiceImpl implements VoucherGroupService {
    @Autowired
    VoucherGroupRepository repository;
    @Autowired
    LocaleFactory localeFactory;
    @Autowired
    UploadFile uploadFile;
    @Autowired
    AppConfig appConfig;
    @Autowired
    VoucherGroupSubMerchantRepository voucherGroupSubMerchantRepository;

    @Override
    public Page<VoucherGroup> search(Integer pageNo, Integer pageSize, String title, Integer status, String dateExpireds) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        VoucherGroupDto dto = searchInputHandler(title, status, dateExpireds);

        return repository.search(pageable, dto.getName(), dto.getStatus(), dto.getDateExpiredStart(), dto.getDateExpiredEnd());
    }

    @Override
    public void delete(Long id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            log.error("Exception: ", e);
        }
    }


    @Override
    public VoucherGroup findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void save(VoucherGroup object) {
        try {
            repository.save(object);
        } catch (Exception e) {
            log.error("ERROR: {}", e.getMessage(), e);
        }
    }

    @Override
    public int saveHandler(RedirectAttributes redirectAttributes, VoucherGroupDto dto, String thumbUpload1) {
        try {
            Long id = dto.getId();
            VoucherGroup obj;
            if(id == null) {
                obj = new VoucherGroup();
                redirectAttributes.addFlashAttribute("success", localeFactory.getMessage("title.create.success"));
            } else {
                obj = findById(id);
                redirectAttributes.addFlashAttribute("success", localeFactory.getMessage("title.update.success"));

                if(dto.getQuantityTotal() < obj.getQuantityExchanged()) {
                    redirectAttributes.addFlashAttribute("error", localeFactory.getMessage("title.update.error.total-voucher"));
                    return 1;
                }
            }

            obj.setTopicId(dto.getTopicId());
            obj.setIdMerchant(dto.getIdMerchant());
            obj.setName(dto.getName());
            obj.setStatus(dto.getStatus());
            obj.setPoint(dto.getPoint());
            obj.setDiscountUnit("HTG");
            obj.setDescription(dto.getDescription());
            obj.setStartDate(Helper.convertDate(dto.getStartDate()));
            obj.setEndDate(Helper.convertDate(dto.getEndDate()));
            obj.setQuantityTotal(dto.getQuantityTotal());
            obj.setAmount(dto.getAmount());
            obj.setPointUnit("points");

            Path fileNameVideoImage1 = uploadFile.createImageFileV1(thumbUpload1, "images", appConfig.getFileInDBPrefix(), appConfig.getRootPath());
            if (fileNameVideoImage1 != null)
                obj.setImageUrl(fileNameVideoImage1.toString());
            save(obj);
            saveVoucherGroupSubMerchantHandler(obj.getId(), dto.getSubMerchantIds());
            return 0;
        } catch (Exception e) {
            log.error("ERROR: {}", e.getMessage(), e);
        }

        return 2;
    }

    private void saveVoucherGroupSubMerchantHandler(long voucherGroupId, Set<String> subMerchantIds) {
        try {
            voucherGroupSubMerchantRepository.deleteAllByVoucherGroupId(voucherGroupId);

            for (String subMerchantId : subMerchantIds) {
                VoucherGroupSubMerchant vgsm = new VoucherGroupSubMerchant();
                vgsm.setIdVoucherGroup(voucherGroupId);
                vgsm.setIdSubMerchant(Long.valueOf(subMerchantId));
                voucherGroupSubMerchantRepository.save(vgsm);
            }
        } catch (Exception e) {
            log.error("ERROR: {}", e.getMessage(), e);
        }
    }

    private VoucherGroupDto searchInputHandler(String name, Integer status, String dateExpireds) {
        VoucherGroupDto dto = new VoucherGroupDto();

        dto.setName(name);
        dto.setStatus(status);

        String[] dateRanges = Helper.reportDate(dateExpireds);
        dto.setDateExpiredStart(Helper.convertDateV2(dateRanges[0]));
        dto.setDateExpiredEnd(Helper.convertDateV2(dateRanges[1]));

        return dto;
    }
}
