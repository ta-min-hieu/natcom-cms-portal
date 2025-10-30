//package com.ringme.controller.voucher;
//
//import com.ringme.common.AppUtils;
//import com.ringme.dto.voucher.MerchantFormDto;
//import com.ringme.dto.voucher.StaffFormDto;
//import com.ringme.dto.voucher.SubMerchantFormDto;
//import com.ringme.model.voucher.Merchant;
//import com.ringme.model.voucher.SubMerchant;
//import com.ringme.repository.voucher.HistoryVersionMerchantRepository;
//import com.ringme.repository.voucher.MerchantRepository;
//import com.ringme.repository.voucher.StaffRepository;
//import com.ringme.repository.voucher.SubMerchantRepository;
//import com.ringme.service.voucher.MerchantService;
//import com.ringme.service.voucher.PermissionVoucher;
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
//import java.util.*;
//
//@Controller
//@Log4j2
//@RequestMapping("/merchant")
//public class MerchantController {
//    @Autowired
//    MerchantService merchantService;
//    @Autowired
//    MerchantRepository merchantRepository;
//    @Autowired
//    HistoryVersionMerchantRepository hvmRepo;
//    @Autowired
//    PermissionVoucher permissionVoucher;
//    @Autowired
//    ModelMapper modelMapper;
//    @Autowired
//    private SubMerchantRepository subMerchantRepository;
//    @Autowired
//    private StaffRepository staffRepository;
//
//    @GetMapping(value = {"/index"})
//    public String index(@RequestParam(name = "pageNo", required = false, defaultValue = "1") Integer pageNo,
//                        @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
//                        @RequestParam(name = "ownerPhoneNumber", required = false) String ownerPhoneNumber,
//                        @RequestParam(name = "status", required = false) Integer status,
//                        @RequestParam(name = "name", required = false) String name,
//                        @RequestParam(name = "rangerDate", required = false) String rangerDate,
//                        ModelMap model) {
//        log.info("pageNo: {} | pageSize: {} | ownerPhoneNumber: {} | status: {} | name: {} | rangerDate: {}",
//                pageNo, pageSize, ownerPhoneNumber, status, name, rangerDate);
//        if (pageNo == null || pageNo <= 0) pageNo = 1;
//        if (pageSize == null || pageSize <= 0) pageSize = 10;
//
//        Page<Merchant> models = merchantService.getPage(ownerPhoneNumber, status, name, rangerDate, pageNo, pageSize);
//        model.put("title", "Merchant Management");
//        model.put("pageNo", pageNo);
//        model.put("pageSize", pageSize);
//        model.put("totalPage", models.getTotalPages());
//        model.put("models", models.toList());
//        model.put("ownerPhoneNumber", ownerPhoneNumber);
//        model.put("status", status);
//        model.put("name", name);
//        model.put("rangerDate", rangerDate);
//        return "merchant/index";
//    }
//
//    @PostMapping("/disapproveAll")
//    @PreAuthorize("@permissionVoucher.checkAdminVoucher()")
//    public ResponseEntity<?> disapproveAll(@RequestParam("ids") List<Long> ids,
//                                           @RequestParam("reason") String reason) {
//        log.info("Ids change status: {}", ids);
//        try {
//            merchantService.disapproveAll(ids, reason);
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
//            merchantService.approveAll(ids);
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
//            merchantService.lockAll(ids);
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
//            merchantService.unlockAll(ids);
//            return ResponseEntity.ok("Success");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
//        }
//    }
//
//    @PostMapping(value = {"/delete/{id}"})
//    @PreAuthorize("@permissionVoucher.isMerchantOwner(#id)")
//    public String delete(@PathVariable(required = true) Long id,
//                         HttpServletRequest request,
//                         RedirectAttributes redirectAttributes) {
//        log.info("id: {}", id);
//        try {
//            merchantService.deleteById(id);
//            redirectAttributes.addFlashAttribute("success", "Delete success!");
//        } catch (Exception e) {
//            log.error("Exception: {}", e.getMessage(), e);
//            redirectAttributes.addFlashAttribute("error", "Error in server!");
//        }
//        return AppUtils.goBack(request).orElse("redirect:/voucher/index");
//    }
//
//    @PostMapping("/seeNote")
//    @PreAuthorize("@permissionVoucher.checkAdminVoucher() || @permissionVoucher.isMerchantOwner(#id)")
//    public ResponseEntity<?> seeNote(@RequestParam("id") Long id) {
//        log.info("id: {}", id);
//        try {
//            Merchant merchant = merchantRepository.findById(id)
//                    .orElseThrow(() -> new NoSuchElementException("Not found by id: " + id));
//            return ResponseEntity.ok(merchant.getNote());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
//        }
//    }
//
//    @GetMapping("/history-version")
//    @PreAuthorize("@permissionVoucher.checkAdminVoucher() || @permissionVoucher.isMerchantOwner(#id)")
//    public String detail(@RequestParam("id") Long id, ModelMap model) throws Exception {
//        log.info("id: {}", id);
//        try {
//            List<HistoryVersionMerchant> historyVersionMerchants = new ArrayList<>();
//            Merchant merchant = merchantRepository.findById(id).orElse(new Merchant());
//            if (merchant.getOfficialMerchant() != null) {
//                historyVersionMerchants = merchantService.generateHistoryVM(merchant.getOfficialMerchant(), merchant, 1);
//                historyVersionMerchants.forEach(item -> item.setUser(merchant.getUser()));
//            } else {
//                historyVersionMerchants = hvmRepo.findAllByIdMerchant(id);
//            }
//
//            model.put("models", historyVersionMerchants);
//            return "voucher/history-version :: history-version";
//        } catch (Exception e) {
//            log.error("Exception: {}", e.getMessage(), e);
//            throw new Exception(e);
//        }
//    }
//
//    @GetMapping("/form")
//    @PreAuthorize("@permissionVoucher.checkAdminVoucher() || @permissionVoucher.isMerchantOwner(#id)")
//    public String form(@RequestParam(value = "id", required = false) Long id, ModelMap model) {
//        log.info("id: {}", id);
//        try {
//            MerchantFormDto formDto = (MerchantFormDto) model.getOrDefault("form", new MerchantFormDto());
//            Merchant merchant = new Merchant();
//            if (id != null) {
//                merchant = merchantRepository.findById(id).orElseThrow();
//                if (!model.containsAttribute("form")) {
//                    formDto = modelMapper.map(merchant, MerchantFormDto.class);
//                }
//                model.put("status", merchant.getStatus());
//            }
//            model.put("merchant", merchant);
//            model.putIfAbsent("form", formDto);
//
//            if (permissionVoucher.checkAdminVoucher() || !(merchant.getStatus() == 2 || merchant.getStatus() == 4)) {
//                model.put("title", "Detail merchant");
//            } else {
//                model.put("title", id != null ? "Edit merchant" : "Create merchant");
//            }
//
//            return "merchant/form";
//        } catch (Exception e) {
//            log.error("Exception: {}", e.getMessage(), e);
//            return "404";
//        }
//    }
//
//    @PostMapping("/save")
//    @PreAuthorize("@permissionVoucher.isMerchantOwner(#formDto.id)")
//    public String save(@Valid @ModelAttribute("form") MerchantFormDto formDto,
//                       BindingResult bindingResult,
//                       RedirectAttributes redirectAttributes,
//                       HttpServletRequest request,
//                       ModelMap model) {
//        try {
//            log.info("Merchant Form DTO Information: {}", formDto);
//            if (bindingResult.hasErrors()) {
//                return AppUtils.goBackWithErrorV2(request, redirectAttributes, model, null)
//                        .orElse("redirect:/merchant/form?id=" + formDto.getId());
//            }
//            Merchant merchantSaved = merchantService.save(formDto);
//            if (formDto.getId() == null) {
//                redirectAttributes.addFlashAttribute("success", "Create draft success");
//                return "redirect:/merchant/form?id=" + merchantSaved.getId();
//            } else {
//                if (formDto.getCheckSubmit()) {
//                    redirectAttributes.addFlashAttribute("success", "Create request success");
//                } else {
//                    redirectAttributes.addFlashAttribute("success", "Update success!");
//                }
//            }
//            return AppUtils.goBack(request).orElse("redirect:/merchant/form?id=" + merchantSaved.getId());
//        } catch (Exception e) {
//            log.error("Exception: {}", e.getMessage(), e);
//            return AppUtils.goBackWithErrorV2(request, redirectAttributes, model, "Error in server!")
//                    .orElse("redirect:/merchant/form?id=" + formDto.getId());
//        }
//    }
//
//    @PostMapping("/create-draft-v2")
//    @PreAuthorize("@permissionVoucher.isMerchantOwner(#id)")
//    public String createDraftV2(@RequestParam(value = "id") Long id,
//                                HttpServletRequest request,
//                                RedirectAttributes redirectAttributes) {
//        try {
//            log.info("Create draft: {}", id);
//
//            Merchant merchant = merchantService.createDraft(id);
//
//            if (merchant == null || merchant.getId() == null) {
//                redirectAttributes.addFlashAttribute("error", "Failed!");
//                return AppUtils.goBack(request).orElse("redirect:/merchant/index");
//            } else {
//                redirectAttributes.addFlashAttribute("success", "Success!");
//                return "redirect:/merchant/form?id=" + merchant.getId();
//            }
//        } catch (Exception e) {
//            log.error("Exception: {}", e.getMessage(), e);
//            redirectAttributes.addFlashAttribute("error", "Error in server!");
//            return AppUtils.goBack(request).orElse("redirect:/merchant/index");
//        }
//    }
//
//    @GetMapping(value = {"/render-sub-merchant"})
//    public String renderSubMerchant(@RequestParam(name = "pageNo", required = false, defaultValue = "1") Integer pageNo,
//                                    @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
//                                    @RequestParam(name = "idMerchant", required = false) Long idMerchant,
//                                    @RequestParam(name = "smName", required = false) String smName,
//                                    ModelMap model) throws Exception {
//        log.info("pageNo: {} | pageSize: {} | idMerchant: {} | smName: {}", pageNo, pageSize, idMerchant, smName);
//        if (pageNo == null || pageNo <= 0) pageNo = 1;
//        if (pageSize == null || pageSize <= 0) pageSize = 10;
//
//        try {
//            Page<SubMerchant> models = merchantService.renderSubMerchant(idMerchant, smName, pageNo, pageSize);
//            Merchant merchant = merchantRepository.findById(Optional.ofNullable(idMerchant).orElse(-1L)).orElse(new Merchant());
//            model.put("pageNo", pageNo);
//            model.put("pageSize", pageSize);
//            model.put("totalPage", models.getTotalPages());
//            model.put("models", models.toList());
//            model.put("idMerchant", idMerchant);
//            model.put("merchant", merchant);
//            return "merchant/render-sub-merchant :: render-sub-merchant";
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @GetMapping(value = {"/render-staff"})
//    public String renderStaff(@RequestParam(name = "pageNo", required = false, defaultValue = "1") Integer pageNo,
//                              @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
//                              @RequestParam(name = "idSubMerchant", required = false) Long idSubMerchant,
//                              @RequestParam(name = "sName", required = false) String sName,
//                              ModelMap model) throws Exception {
//        log.info("pageNo: {} | pageSize: {} | idSubMerchant: {} | sName: {}", pageNo, pageSize, idSubMerchant, sName);
//        if (pageNo == null || pageNo <= 0) pageNo = 1;
//        if (pageSize == null || pageSize <= 0) pageSize = 10;
//
//        try {
//            SubMerchant subMerchant = subMerchantRepository.findById(Optional.ofNullable(idSubMerchant).orElse(-1L))
//                    .orElse(new SubMerchant());
//            Page<Staff> models = merchantService.renderStaff(idSubMerchant, sName, pageNo, pageSize);
//            model.put("pageNo", pageNo);
//            model.put("pageSize", pageSize);
//            model.put("totalPage", models.getTotalPages());
//            model.put("models", models.toList());
//            model.put("idSubMerchant", idSubMerchant);
//            model.put("status", Optional.ofNullable(subMerchant.getMerchant()).orElse(new Merchant()).getStatus());
//            model.put("nameSubMerchant", subMerchant.getName());
//            return "merchant/render-staff :: render-staff";
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @GetMapping(value = {"/find-sub-merchant"})
//    public ResponseEntity<?> findSubMerchant(@RequestParam(name = "id", required = false, defaultValue = "-1") Long id) throws Exception {
//        log.info("id: {}", id);
//        try {
//            Map<String, Object> result = new HashMap<>();
//            SubMerchant subMerchant = subMerchantRepository.findById(id)
//                    .orElse(new SubMerchant());
//            result.put("id", subMerchant.getId());
//            result.put("idMerchant", subMerchant.getIdMerchant());
//            result.put("code", subMerchant.getCode());
//            result.put("name", subMerchant.getName());
//            result.put("address", subMerchant.getAddress());
//            result.put("ownerName", subMerchant.getOwnerName());
//            result.put("ownerPhoneNumber", subMerchant.getOwnerPhoneNumber());
//            result.put("ownerIdPaperType", subMerchant.getOwnerIdPaperType());
//            result.put("ownerIdPaperNumber", subMerchant.getOwnerIdPaperNumber());
//            result.put("totalStaff", subMerchant.getTotalStaff());
//            return ResponseEntity.ok(result);
//        } catch (Exception e) {
//            log.error("Exception: {}", e.getMessage(), e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in server");
//        }
//    }
//
//    @GetMapping(value = {"/find-staff"})
//    public ResponseEntity<?> findStaff(@RequestParam(name = "id", required = false, defaultValue = "-1") Long id) throws Exception {
//        log.info("id: {}", id);
//        try {
//            Map<String, Object> result = new HashMap<>();
//            Staff staff = staffRepository.findById(id)
//                    .orElse(new Staff());
//            result.put("id", staff.getId());
//            result.put("idSubMerchant", staff.getIdSubMerchant());
//            result.put("code", staff.getCode());
//            result.put("name", staff.getName());
//            result.put("phoneNumber", staff.getPhoneNumber());
//            return ResponseEntity.ok(result);
//        } catch (Exception e) {
//            log.error("Exception: {}", e.getMessage(), e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in server");
//        }
//    }
//
//    @PostMapping(value = {"/save-sub-merchant"})
//    public ResponseEntity<?> saveSubMerchant(@Valid @RequestBody SubMerchantFormDto formDto) throws Exception {
//        log.info("formDto: {}", formDto);
//        try {
//            merchantService.saveSubMerchant(formDto);
//            return ResponseEntity.ok("Success");
//        } catch (Exception e) {
//            log.error("Exception: {}", e.getMessage(), e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in server");
//        }
//    }
//
//    @PostMapping(value = {"/save-staff"})
//    public ResponseEntity<?> saveStaff(@Valid @RequestBody StaffFormDto formDto) throws Exception {
//        log.info("formDto: {}", formDto);
//        try {
//            merchantService.saveStaff(formDto);
//            return ResponseEntity.ok("Success");
//        } catch (Exception e) {
//            log.error("Exception: {}", e.getMessage(), e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in server");
//        }
//    }
//
//    @PostMapping(value = {"/delete-sub-merchant"})
//    public ResponseEntity<?> deleteSubMerchant(@RequestParam("id") Long id) throws Exception {
//        log.info("id: {}", id);
//        try {
//            merchantService.deleteSubMerchantById(id);
//            return ResponseEntity.ok("Success");
//        } catch (Exception e) {
//            log.error("Exception: {}", e.getMessage(), e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in server");
//        }
//    }
//
//    @PostMapping(value = {"/delete-staff"})
//    public ResponseEntity<?> deleteStaff(@RequestParam("id") Long id) throws Exception {
//        log.info("id: {}", id);
//        try {
//            merchantService.deleteStaffById(id);
//            return ResponseEntity.ok("Success");
//        } catch (Exception e) {
//            log.error("Exception: {}", e.getMessage(), e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in server");
//        }
//    }
//
//    @GetMapping(value = {"/find-merchant"})
//    public ResponseEntity<?> findMerchant(@RequestParam(name = "id", required = false, defaultValue = "-1") Long id) throws Exception {
//        log.info("id: {}", id);
//        try {
//            Map<String, Object> result = new HashMap<>();
//            Merchant merchant = merchantRepository.findById(id)
//                    .orElse(new Merchant());
//            result.put("id", merchant.getId());
//            result.put("code", merchant.getCode());
//            result.put("name", merchant.getName());
//            result.put("ownerName", merchant.getOwnerName());
//            result.put("totalSubMerchant", merchant.getTotalSubMerchant());
//            result.put("ownerPhoneNumber", merchant.getOwnerPhoneNumber());
//            result.put("totalStaff", merchant.getTotalStaff());
//            result.put("status", merchant.getStatus());
//            result.put("ownerIdPaperType", merchant.getOwnerIdPaperType());
//            result.put("ownerIdPaperNumber", merchant.getOwnerIdPaperNumber());
//            result.put("type", merchant.getType());
//            result.put("note", merchant.getNote());
//            result.put("draftFor", merchant.getDraftFor());
//            return ResponseEntity.ok(result);
//        } catch (Exception e) {
//            log.error("Exception: {}", e.getMessage(), e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in server");
//        }
//    }
//
//    @GetMapping("/create-draft")
//    public String createDraft(RedirectAttributes redirectAttributes, HttpServletRequest request) {
//        try {
//            Merchant merchantSaved = merchantService.createDraft();
//            redirectAttributes.addFlashAttribute("success", "Create draft success");
//            return "redirect:/merchant/form?id=" + merchantSaved.getId();
//        } catch (Exception e) {
//            log.error("Exception: {}", e.getMessage(), e);
//            redirectAttributes.addFlashAttribute("error", "Create draft failed");
//            return AppUtils.goBack(request).orElse("redirect:/merchant/index");
//        }
//    }
//}
