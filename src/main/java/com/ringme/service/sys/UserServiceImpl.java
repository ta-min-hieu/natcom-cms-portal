package com.ringme.service.sys;

import com.ringme.common.Helper;
import com.ringme.dto.AjaxSearchDto;
import com.ringme.model.sys.User;
import com.ringme.model.sys.UserRole;
import com.ringme.repository.sys.UserRepository;
import com.ringme.repository.sys.UserRoleRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import java.util.*;

@Service
@Log4j2
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserRoleRepository userRoleRepository;
    @Autowired
    HttpSession session;

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) throws Exception {
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            throw new Exception();
        }
    }

    @Override
    public Optional<User> findByIdUser(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<String> processAccountType(List<User> userList) {
        List<UserRole> userRoles = userRoleRepository.findUserRole();
        List<String> accountType = new ArrayList<>(Collections.nCopies(userList.size(), ""));
        for(int i=0; i<userList.size(); i++) {
            for(UserRole userRole : userRoles) {
                if(Objects.equals(userList.get(i).getId(), userRole.getUser().getId())) {
                    if(accountType.get(i).equals(""))
                        accountType.set(i,userRole.getRole().getRoleName().replace("ROLE_",""));
                    else
                        accountType.set(i,accountType.get(i) + ", " + userRole.getRole().getRoleName().replace("ROLE_",""));
                }
            }
        }
        return accountType;
    }

    @Override
    public String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((UserDetails) authentication.getPrincipal()).getUsername();
    }

    @Override
    public User getUserInfo() {
        if (session.getAttribute("user-cms") != null) {
            User user = (User) session.getAttribute("user-cms");
//            log.info("session user-cms is not null|{}", user);
            return user;
        }
        User user = userRepository.getUserInfoByUsername(getUsername());
//        log.info("session user-cms is null|{}", user);
        session.setAttribute("user-cms", user);
        return user;
    }

    private List<String> getRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
    }

    @Override
    public boolean isAdmin() {
        List<String> roles = getRoles();
        return roles.contains("ROLE_ADMIN");
    }

//    @Override
//    public boolean isMpsPackageReview() {
//        List<String> roles = getRoles();
//        return roles.contains("ROLE_ADMIN") || roles.contains("ROLE_MPS_PACKAGE_REVIEWER");
//    }

    @Override
    public Long getId() {
        return userRepository.getId(getUsername());
    }

    @Override
    public User getUser() {
        return userRepository.getUser(getUsername());
    }

    @Override
    public Integer getPartnerId() {
        return getUserInfo().getPartnerId();
    }

    @Override
    public String getShowroomId() {
        User user = getUserInfo();
        if(isAdmin()) {
            log.info("User: {} is admin", user.getUsername());
            return null;
        }
        log.info("User: {} is partner", user.getUsername());
        return Helper.processStringSearch(user.getShowroomId());
    }

    @Override
    public Long getBranchId() {
        User user = getUserInfo();
        if(isAdmin()) {
            log.info("User: {} is admin", user.getUsername());
            return null;
        }
        log.info("User: {} is partner", user.getUsername());
        return user.getBranchId();
    }

    @Override
    public Page<User> pageUser(int pageNo, int pageSize, Long id, String email, String name, String phone) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        if (id == 0) {
            id = null;
        }
        if (email != null) {
            if (email.trim().equals("")) {
                email = null;
            } else {
                email = email.trim().replaceAll("\s+", "");
            }
        }
        if (name != null) {
            if (name.trim().equals("")) {
                name = null;
            } else {
                name = name.trim().replaceAll("\s+", " ");
            }
        }
        if (phone != null) {
            if (phone.trim().equals("")) {
                phone = null;
            } else {
                phone = phone.trim().replaceAll("\s+", "");
                ;
            }
        }
        return userRepository.search(id, email, name, phone, pageable);
    }

    @Override
    public String checkId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String accountName = ((UserDetails) authentication.getPrincipal()).getUsername();
        log.info("accountName|" + accountName);
        return userRepository.checkId(accountName);
    }

    @Override
    public List<AjaxSearchDto> ajaxSearchUser(String input) {
        return Helper.listAjax(userRepository.ajaxSearchUser(Helper.processStringSearch(input)), 1);
    }

    @Override
    public Optional<User> findUserByName (String username) {
        return userRepository.findUserByName(username);
    }
}
