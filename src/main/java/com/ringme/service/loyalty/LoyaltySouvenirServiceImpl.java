package com.ringme.service.loyalty;

import com.ringme.common.Helper;
import com.ringme.dto.loyalty.LoyaltySouvenirDto;
import com.ringme.model.loyalty.LoyaltySouvenir;
import com.ringme.repository.loyalty.LoyaltySouvenirRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public final class LoyaltySouvenirServiceImpl implements LoyaltySouvenirService {
    @Autowired
    LoyaltySouvenirRepository repository;

    @Override
    public Page<LoyaltySouvenir> search(Integer pageNo, Integer pageSize, String title, Integer status, String dateExpireds) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        LoyaltySouvenirDto dto = searchInputHandler(title, status, dateExpireds);

        return repository.search(pageable, dto.getTitle(), dto.getStatus(), dto.getDateExpiredStart(), dto.getDateExpiredEnd());
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
    public LoyaltySouvenir findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void save(LoyaltySouvenir object) {
        try {
            repository.save(object);
        } catch (Exception e) {
            log.error("ERROR: {}", e.getMessage(), e);
        }
    }

    private LoyaltySouvenirDto searchInputHandler(String title, Integer status, String dateExpireds) {
        LoyaltySouvenirDto dto = new LoyaltySouvenirDto();

        dto.setTitle(title);
        dto.setStatus(status);

        String[] dateRanges = Helper.reportDate(dateExpireds);
        dto.setDateExpiredStart(Helper.convertDateV2(dateRanges[0]));
        dto.setDateExpiredEnd(Helper.convertDateV2(dateRanges[1]));

        return dto;
    }
}
