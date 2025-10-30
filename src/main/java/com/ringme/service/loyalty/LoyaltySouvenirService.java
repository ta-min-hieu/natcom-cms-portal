package com.ringme.service.loyalty;

import com.ringme.model.loyalty.LoyaltySouvenir;
import org.springframework.data.domain.Page;

public sealed interface LoyaltySouvenirService permits LoyaltySouvenirServiceImpl {
    Page<LoyaltySouvenir> search (Integer pageNo, Integer pageSize, String title, Integer status, String dateExpireds);

    void delete (Long id);

    LoyaltySouvenir findById (Long id);

    void save (LoyaltySouvenir obj);
}
