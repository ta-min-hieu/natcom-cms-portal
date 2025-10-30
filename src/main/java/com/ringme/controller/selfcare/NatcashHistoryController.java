package com.ringme.controller.selfcare;

import com.ringme.config.AppConfig;
import com.ringme.config.LocaleFactory;
import com.ringme.dao.NatcashHistoryDao;
import com.ringme.dto.record.Page;
import com.ringme.dto.selfcare.MerchantResponse;
import com.ringme.dto.selfcare.NatcashPaymentMobileServiceVasHistoryDto;
import com.ringme.model.selfcare.NatcashPaymentMobileServiceVasHistory;
import com.ringme.service.selfcare.NatcashHistoryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Log4j2
@RequestMapping("/selfcare/natcash-history")
public class NatcashHistoryController {
    @Autowired
    NatcashHistoryService service;
    @Autowired
    NatcashHistoryDao dao;
    @Autowired
    AppConfig appConfig;
    @Autowired
    LocaleFactory localeFactory;

    @GetMapping(value = {"/payment-data-vas/{page}", "/payment-data-vas"})
    public String getAllPage(@PathVariable(required = false) Integer page,
                             @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                             @RequestParam(name = "isdn", required = false, defaultValue = "") String isdn,
                             @RequestParam(name = "packageCode", required = false, defaultValue = "") String packageCode,
                             @RequestParam(name = "errorCode", required = false, defaultValue = "") String errorCode,
                             @RequestParam(name = "orderNumber", required = false, defaultValue = "") String orderNumber,
                             @RequestParam(name = "dateRanger", required = false, defaultValue = "") String dateRanger,
                             Model model) {
        if(page == null)
            page = 1;

        Page<NatcashPaymentMobileServiceVasHistoryDto> objectPage = service.search(page, pageSize, isdn, packageCode, errorCode, orderNumber, dateRanger);

        model.addAttribute("title", localeFactory.getMessage("title.list.LoyaltySouvenirOrder"));
        model.addAttribute("isdn", isdn);
        model.addAttribute("packageCode", packageCode);
        model.addAttribute("errorCode", errorCode);
        model.addAttribute("orderNumber", orderNumber);
        model.addAttribute("dateRanger", dateRanger);

        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", objectPage.totalPage());
        model.addAttribute("totalItems", objectPage.totalRecord());
        model.addAttribute("models", objectPage.data());
        return "natcash-history/payment-data-vas";
    }

    @GetMapping(value = {"/refund", "/refund/{page}"})
    public String delete(@PathVariable(required = false) Integer page,
                         @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                         @RequestParam(name = "orderNumber") String orderNumber,
                         RedirectAttributes redirectAttributes) {
        if(page == null)
            page = 1;
        log.info("orderNumber|{}", orderNumber);
        String requestId = dao.getRequestIdByOrderNumber(orderNumber);
        String amount = dao.getAmountByOrderNumber(orderNumber);

        MerchantResponse response = callApiRefund(requestId, orderNumber, amount);


        redirectAttributes.addFlashAttribute("success", response == null ? "Fail" : response.getMessage());
        return "redirect:/selfcare/natcash-history/payment-data-vas/" + page + "?pageSize=" + pageSize;
    }

    private MerchantResponse callApiRefund(String requestId, String orderNumber, String amount) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            String url = appConfig.getApiBaseUrl() + "/natcash/test/cancel-trans-v2";

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("requestId", requestId);
            body.add("orderNumber", orderNumber);
            body.add("amount", "-" + amount);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<MerchantResponse> response = restTemplate.postForEntity(url, requestEntity, MerchantResponse.class);

            log.info("Response: " + response.getBody());
            return response.getBody();
        } catch (Exception e) {
            log.error("requestId: {}, orderNumber: {}, error: {}", requestId, orderNumber, e.getMessage(), e);
        }
        return null;
    }
}
