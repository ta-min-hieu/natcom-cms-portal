package com.ringme.dto.sys;

import com.ringme.model.mynatcom.Shop;
import com.ringme.model.selfcare.FtthBranch;
import com.ringme.model.sys.User;
import com.ringme.validationfield.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private String username;
    @Name
    private String name;

    @Password
    private String password;

    @Password
    private String password2;

    private String type;

    private String phone;
    private Integer partnerId;
    private String showroomId;
    private Long branchId;
    private Shop shop;
    private FtthBranch ftthBranch;

    public User convertToEntity() {
        User user = new User();
        user.setId(this.id);
        if (this.name != null) {
            user.setName(this.name.trim().replaceAll("\s+", " "));
        }
        if (this.phone != null) {
            user.setPhone(this.phone.trim().replaceAll("\s+", " "));
        }
        if (this.type != null) {
            user.setType(this.type.trim());
        }
        if (this.password != null) {
            user.setPassword(this.password.trim());
        }
        if (this.username !=null  && this.id ==null){
            user.setUsername(this.username);
            user.setEmail(this.username);
        }
        user.setPartnerId(this.partnerId);
        user.setShowroomId(this.showroomId);
        user.setBranchId(this.branchId);
        return user;
    }
}
