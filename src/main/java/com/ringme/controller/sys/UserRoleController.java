package com.ringme.controller.sys;

import com.ringme.model.sys.Role;
import com.ringme.model.sys.User;
import com.ringme.model.sys.UserRole;
import com.ringme.repository.sys.RoleRepository;
import com.ringme.service.sys.RoleService;
import com.ringme.service.sys.UserRoleService;
import com.ringme.service.sys.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/sys")
public class UserRoleController {

    @Autowired
    RoleService roleService;

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    UserService userService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserController userController;

    @GetMapping("user/view/{id}")
    public String getUserRoleById(@PathVariable Long id, Model model, RedirectAttributes attributes) {
        Optional<User> user = userService.findByIdUser(id);
        if (user.isPresent()) {
            List<UserRole> roles = userRoleService.findAllUserRoleByIdUser(id);
            if (roles.isEmpty()) {
                List<Role> roleList = roleService.findAllRole();
                model.addAttribute("roles", roleList);
            } else {
                List<Long> idRole = roles.stream().map(e -> e.getRole().getId()).collect(Collectors.toList());
                List<Role> roleList = roleService.findAllRoleNotInListIdRole(idRole);
                model.addAttribute("roles", roleList);
            }
            model.addAttribute("user", user.get());
            model.addAttribute("user_roles", roles);
            return "sys/user-role";
        }
        attributes.addFlashAttribute("error", "Người dùng không còn tồn tại!");
        return "redirect:/sys/user/index";
    }

    @PostMapping("/user-role/create")
    public String updateUserRole(@RequestParam("id") Long id, @RequestParam(name = "role1", required = false) List<Long> roles, Model model, RedirectAttributes redirectAttributes) {
        if (roles == null) {
            model.addAttribute("error", "Vui lòng chọn một quyền!");
            return getUserRoleById(id, model, redirectAttributes);
        }
        if (roles.isEmpty()) {
            model.addAttribute("error", "Vui lòng chọn một quyền!");
            return getUserRoleById(id, model, redirectAttributes);
        }
        Optional<User> user = userService.findByIdUser(id);
        if (user.isPresent()) {
            List<Role> rolePresent = roleRepository.findAllById(roles);
            List<UserRole> userRoles = rolePresent.stream().map((e) -> {
                return new UserRole(user.get(), e);
            }).collect(Collectors.toList());
            try {
                userRoleService.saveAllUserRole(userRoles);
            } catch (Exception e) {
                model.addAttribute("error", "Cập nhật quyền lỗi!");
                return getUserRoleById(id, model, redirectAttributes);
            }
            model.addAttribute("success", "Cập nhật quyền thành công!");
            return getUserRoleById(id, model, redirectAttributes);
        }
        model.addAttribute("error", "Người dùng không còn tồn tại!");
        return getUserRoleById(id, model, redirectAttributes);

    }

    @PostMapping("/user-role/delete")
    public String deleteUserRole(@RequestParam("id") Long id, @RequestParam(name = "role2", required = false) List<Long> userroles, Model model, RedirectAttributes redirectAttributes) {
        if (userroles == null) {
            model.addAttribute("error", "Vui lòng chọn một quyền muốn bỏ!");
            return getUserRoleById(id, model, redirectAttributes);
        }
        if (userroles.isEmpty()) {
            model.addAttribute("error", "Vui lòng chọn một quyền muốn bỏ!");
            return getUserRoleById(id, model, redirectAttributes);
        }
        Optional<User> user = userService.findByIdUser(id);
        if (user.isPresent()) {
            try {
                userRoleService.deleteUserRoleById(userroles);
                model.addAttribute("success", "Xóa quyền thành công!");
                return getUserRoleById(id, model, redirectAttributes);
            } catch (Exception e) {
                model.addAttribute("error", "Xóa thất bại!");
                return getUserRoleById(id, model, redirectAttributes);
            }
        } else {
            model.addAttribute("error", "Người dùng không còn tồn tại!");
            return getUserRoleById(id, model, redirectAttributes);
        }
    }

}
