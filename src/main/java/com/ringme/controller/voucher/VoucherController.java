//package com.ringme.controller.voucher;
//
//import com.ringme.common.AppUtils;
//import com.ringme.dto.voucher.VoucherGroupFormDto;
//import com.ringme.model.voucher.HistoryVersionVoucher;
//import com.ringme.model.voucher.SubMerchant;
//import com.ringme.model.voucher.Voucher;
//import com.ringme.model.voucher.VoucherGroup;
//import com.ringme.repository.voucher.HistoryVersionVoucherRepository;
//import com.ringme.repository.voucher.MerchantRepository;
//import com.ringme.repository.voucher.VoucherGroupRepository;
//import com.ringme.service.voucher.PermissionVoucher;
//import com.ringme.service.voucher.VoucherGroupService;
//import lombok.extern.log4j.Log4j2;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.validation.Valid;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.NoSuchElementException;
//
//@Controller
//@Log4j2
//@RequestMapping("/voucher")
//public class VoucherGroupController {
//    @Autowired
//    private VoucherGroupService voucherGroupService;
//    @Autowired
//    private MerchantRepository merchantRepository;
//    @Autowired
//    private PermissionVoucher permissionVoucher;
//    @Autowired
//    private VoucherGroupRepository voucherGroupRepository;
//    @Autowired
//    private ModelMapper modelMapper;
//    @Autowired
//    private HistoryVersionVoucherRepository hvvRepo;
//
//    @GetMapping(value = {"/index"})
//    public String index(@RequestParam(name = "pageNo", required = false, defaultValue = "1") Integer pageNo,
//                        @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
//                        @RequestParam(name = "code", required = false) String code,
//                        @RequestParam(name = "idMerchant", required = false) Long idMerchant,
//                        @RequestParam(name = "status", required = false) Integer status,
//                        @RequestParam(name = "rangerDate", required = false) String rangerDate,
//                        ModelMap model) {
//        log.info("pageNo: {} | pageSize: {} | code: {} | idMerchant: {} | status: {} | rangerDate: {}",
//                pageNo, pageSize, code, idMerchant, status, rangerDate);
//        if (pageNo == null || pageNo <= 0) pageNo = 1;
//        if (pageSize == null || pageSize <= 0) pageSize = 10;
//
//        Page<VoucherGroup> models = voucherGroupService.getPage(code, idMerchant, status, rangerDate, pageNo, pageSize);
//        model.put("title", "Voucher Management");
//        model.put("pageNo", pageNo);
//        model.put("pageSize", pageSize);
//        model.put("totalPage", models.getTotalPages());
//        model.put("models", models.toList());
//        model.put("code", code);
//        model.put("idMerchant", idMerchant);
//        if (idMerchant != null) {
//            merchantRepository.findById(idMerchant).ifPresent(item -> {
//                model.put("nameMerchant", item.getName());
//            });
//        }
//        model.put("status", status);
//        model.put("rangerDate", rangerDate);
//        return "voucher/index";
//    }
//
//    @GetMapping(value = {"/detail"})
//    @PreAuthorize("@permissionVoucher.checkAdminVoucher() || @permissionVoucher.isVoucherGroupOwner(#id)")
//    public String detail(@RequestParam(name = "pageNo", required = false, defaultValue = "1") Integer pageNo,
//                         @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
//                         @RequestParam(name = "code", required = false) String code,
//                         @RequestParam(name = "id", required = false) Long id,
//                         @RequestParam(name = "status", required = false) Integer status,
//                         ModelMap model) {
//        log.info("pageNo: {} | pageSize: {} | code: {} | id: {} | status: {}",
//                pageNo, pageSize, code, id, status);
//        if (pageNo == null || pageNo <= 0) pageNo = 1;
//        if (pageSize == null || pageSize <= 0) pageSize = 10;
//
//        VoucherGroup voucherGroup = voucherGroupRepository.findById(id)
//                .orElseThrow(() -> new NoSuchElementException("Not found by id " + id));
//        Page<Voucher> models = voucherGroupService.getPageDetail(id, code, status, pageNo, pageSize);
//        model.put("title", "Detail list");
//        model.put("pageNo", pageNo);
//        model.put("pageSize", pageSize);
//        model.put("totalPage", models.getTotalPages());
//        model.put("models", models.toList());
//        model.put("code", code);
//        model.put("id", id);
//        model.put("status", status);
//        model.put("voucherGroup", voucherGroup);
//        return "voucher/detail";
//    }
//
//    @PostMapping("/disapproveAll")
//    @PreAuthorize("@permissionVoucher.checkAdminVoucher()")
//    public ResponseEntity<?> disapproveAll(@RequestParam("ids") List<Long> ids,
//                                           @RequestParam("reason") String reason) {
//        log.info("Ids change status: {}", ids);
//        try {
//            voucherGroupService.disapproveAll(ids, reason);
//            return ResponseEntity.ok("Success");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
//        }
//    }
//
//    @PostMapping("/approveAll")
//    @PreAuthorize("@permissionVoucher.checkAdminVoucher()")
//    public ResponseEntity<?> approveAll(@RequestParam("ids") List<Long> ids) {
//        log.info("Ids change status: {}", ids);
//        try {
//            voucherGroupService.approveAll(ids);
//            return ResponseEntity.ok("Success");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
//        }
//    }
//
//    @PostMapping("/lockAll")
//    @PreAuthorize("@permissionVoucher.checkAdminVoucher()")
//    public ResponseEntity<?> lockAll(@RequestParam("ids") List<Long> ids) {
//        log.info("Ids change status: {}", ids);
//        try {
//            voucherGroupService.lockAll(ids);
//            return ResponseEntity.ok("Success");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
//        }
//    }
//
//    @PostMapping("/unlockAll")
//    @PreAuthorize("@permissionVoucher.checkAdminVoucher()")
//    public ResponseEntity<?> unlockAll(@RequestParam("ids") List<Long> ids) {
//        log.info("Ids change status: {}", ids);
//        try {
//            voucherGroupService.unlockAll(ids);
//            return ResponseEntity.ok("Success");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
//        }
//    }
//
//    @GetMapping("/form")
//    @PreAuthorize("@permissionVoucher.checkAdminVoucher() || @permissionVoucher.isVoucherGroupOwner(#id)")
//    public String form(@RequestParam(value = "id", required = false) Long id, ModelMap model) {
//        log.info("id: {}", id);
//        try {
//            VoucherGroupFormDto formDto = (VoucherGroupFormDto) model.getOrDefault("form", new VoucherGroupFormDto());
//            VoucherGroup voucherGroup = new VoucherGroup();
//            if (id != null) {
//                voucherGroup = voucherGroupRepository.findById(id).orElseThrow();
//                if (!model.containsAttribute("form")) {
//                    formDto = modelMapper.map(voucherGroup, VoucherGroupFormDto.class);
//
//                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//                    formDto.setRangeDateValid(sdf.format(voucherGroup.getStartDate()) + " - " + sdf.format(voucherGroup.getEndDate()));
//                    formDto.setIdSubMerchants(voucherGroup.getSubMerchants()
//                            .stream().map(SubMerchant::getId).toList());
//                }
//                model.put("image", voucherGroup.getImage());
//                model.put("status", voucherGroup.getStatus());
//            }
//            model.putIfAbsent("form", formDto);
//            model.put("voucherGroup", voucherGroup);
//
//            // Xử lý get các name || options cho select2 || multiple select
//            if (formDto.getIdMerchant() != null) {
//                merchantRepository.findById(formDto.getIdMerchant()).ifPresent(item -> {
//                    model.put("nameMerchant", item.getName());
//                });
//            }
//
//            if (permissionVoucher.checkAdminVoucher() || (voucherGroup.getStatus() != null && voucherGroup.getStatus() == 3)) {
//                model.put("title", "Detail voucher group");
//            } else {
//                model.put("title", id != null ? "Edit voucher group" : "Create voucher group");
//            }
//
//            return "voucher/form";
//        } catch (Exception e) {
//            log.error("Exception: {}", e.getMessage(), e);
//            return "404";
//        }
//    }
//
//    @PostMapping("/save")
//    @PreAuthorize("@permissionVoucher.isVoucherGroupOwner(#formDto.id)")
//    public String save(@Valid @ModelAttribute("form") VoucherGroupFormDto formDto,
//                       BindingResult bindingResult,
//                       RedirectAttributes redirectAttributes,
//                       HttpServletRequest request,
//                       ModelMap model) {
//        try {
//            log.info("Form DTO Information: ID: {}, Name: {}, Quantity: {}, Point: {}, Amount: {}, Unit: {}, Range Date Valid: {}, " +
//                            "Merchant ID: {}, Description: {}, Sub Merchants: {}, Image Upload: {}, Check Submit: {}",
//                    formDto.getId(), formDto.getName(), formDto.getQuantity(), formDto.getPoint(), formDto.getAmount(),
//                    formDto.getUnit(), formDto.getRangeDateValid(), formDto.getIdMerchant(), formDto.getDescription(),
//                    formDto.getIdSubMerchants() != null ? formDto.getIdSubMerchants() : "null",
//                    formDto.getImageUpload() != null && formDto.getImageUpload().length() > 100 ? "(Truncated) " + formDto.getImageUpload().substring(0, 100) + "..." : formDto.getImageUpload(),
//                    formDto.getCheckSubmit()
//            );
//            if (bindingResult.hasErrors()) {
//                return AppUtils.goBackWithErrorV2(request, redirectAttributes, model, null)
//                        .orElse("redirect:/voucher/form?id=" + formDto.getId());
//            }
//            voucherGroupService.save(formDto);
//            if (formDto.getId() == null) {
//                redirectAttributes.addFlashAttribute("success", formDto.getCheckSubmit() ? "Create request success" : "Create draft success");
//                return "redirect:/voucher/index";
//            } else {
//                VoucherGroup voucherGroup = voucherGroupRepository.findById(formDto.getId()).orElseThrow();
//                if (voucherGroup.getStatus() == 0 || voucherGroup.getStatus() == 1) {
//                    redirectAttributes.addFlashAttribute("success", formDto.getCheckSubmit() ? "Create request success" : "Create draft success");
//                    return "redirect:/voucher/index";
//                }
//                redirectAttributes.addFlashAttribute("success", "Update success!");
//            }
//            return AppUtils.goBack(request).orElse("redirect:/voucher/form?id=" + formDto.getId());
//        } catch (Exception e) {
//            log.error("Exception: {}", e.getMessage(), e);
//            return AppUtils.goBackWithErrorV2(request, redirectAttributes, model, "Error in server!")
//                    .orElse("redirect:/voucher/form?id=" + formDto.getId());
//        }
//    }
//
//    @PostMapping(value = {"/delete/{id}"})
//    @PreAuthorize("@permissionVoucher.isVoucherGroupOwner(#id)")
//    public String delete(@PathVariable(required = true) Long id,
//                         HttpServletRequest request,
//                         RedirectAttributes redirectAttributes) {
//        log.info("id: {}", id);
//        try {
//            voucherGroupService.deleteById(id);
//            redirectAttributes.addFlashAttribute("success", "Delete success!");
//        } catch (Exception e) {
//            log.error("Exception: {}", e.getMessage(), e);
//            redirectAttributes.addFlashAttribute("error", "Error in server!");
//        }
//        return AppUtils.goBack(request).orElse("redirect:/voucher/index");
//    }
//
//    @PostMapping("/seeNote")
//    @PreAuthorize("@permissionVoucher.checkAdminVoucher() || @permissionVoucher.isVoucherGroupOwner(#id)")
//    public ResponseEntity<?> seeNote(@RequestParam("id") Long id) {
//        log.info("id: {}", id);
//        try {
//            VoucherGroup voucherGroup = voucherGroupRepository.findById(id)
//                    .orElseThrow(() -> new NoSuchElementException("Not found by id: " + id));
//            return ResponseEntity.ok(voucherGroup.getNote());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
//        }
//    }
//
//    @GetMapping("/history-version")
//    @PreAuthorize("@permissionVoucher.checkAdminVoucher() || @permissionVoucher.isVoucherGroupOwner(#id)")
//    public String detail(@RequestParam("id") Long id, ModelMap model) throws Exception {
//        log.info("id: {}", id);
//        try {
//            List<HistoryVersionVoucher> historyVersionVouchers = new ArrayList<>();
//            VoucherGroup voucherGroup = voucherGroupRepository.findById(id).orElse(new VoucherGroup());
//            if (voucherGroup.getOfficialVG() != null) {
//                historyVersionVouchers = voucherGroupService.generateHistoryVV(voucherGroup.getOfficialVG(), voucherGroup, 1);
//                historyVersionVouchers.forEach(item -> item.setUser(voucherGroup.getUser()));
//            } else {
//                historyVersionVouchers = hvvRepo.findAllByIdVoucherGroup(id);
//            }
//            model.put("models", historyVersionVouchers);
//            return "voucher/history-version :: history-version";
//        } catch (Exception e) {
//            log.error("Exception: {}", e.getMessage(), e);
//            throw new Exception(e);
//        }
//    }
//
//    @PostMapping("/update-invoice-date")
//    public ResponseEntity<?> updateInvoiceDate(@RequestParam("idVoucher") Long idVoucher,
//                                               @RequestParam("value") String value) throws Exception {
//        log.info("idVoucher: {} | value: {}", idVoucher, value);
//        try {
//            voucherGroupService.updateInvoiceDate(idVoucher, value);
//            return ResponseEntity.ok("success");
//        } catch (Exception e) {
//            log.error("Exception: {}", e.getMessage(), e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in server");
//        }
//    }
//
//    @PostMapping("/update-invoice-number")
//    public ResponseEntity<?> updateInvoiceNumber(@RequestParam("idVoucher") Long idVoucher,
//                                                 @RequestParam("value") String value) throws Exception {
//        log.info("idVoucher: {} | value: {}", idVoucher, value);
//        try {
//            voucherGroupService.updateInvoiceNumber(idVoucher, value);
//            return ResponseEntity.ok("success");
//        } catch (Exception e) {
//            log.error("Exception: {}", e.getMessage(), e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in server");
//        }
//    }
//}
