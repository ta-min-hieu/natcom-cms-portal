package com.ringme.service.sys;

import com.ringme.dto.AjaxSearchDto;
import com.ringme.model.sys.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void saveUser(User user);

    Page<User> pageUser(int pageNo, int pageSize, Long id, String email, String name, String phone);

    void deleteUser(Long id) throws Exception;

    Optional<User> findByIdUser(Long id);

    List<String> processAccountType(List<User> userList);

    String getUsername();

    User getUserInfo();

    boolean isAdmin();

    User getUser();

    Long getId();

    Integer getPartnerId();

    String getShowroomId();

    Long getBranchId();

    String checkId();

    List<AjaxSearchDto> ajaxSearchUser(String input);

    Optional<User> findUserByName(String username);
}
