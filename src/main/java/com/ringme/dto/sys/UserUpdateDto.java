package com.ringme.dto.sys;

import com.ringme.model.sys.User;
import com.ringme.validationfield.Name;
import lombok.*;

import jakarta.validation.constraints.Email;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class UserUpdateDto {
    private Long id;
    private String email;
    @Name
    private String name;

    private String type;

    private String username;
    private String phone;
    private Integer partnerId;
    private Integer channelId;
    private PartnerDto partner;
    public User convertToEntity() {
        User user = new User();
        user.setId(this.id);
        if (this.name != null) {
            user.setName(this.name.trim().replaceAll("\s+", " "));
        }
        if (this.phone != null) {
            user.setPhone(this.phone.trim().replaceAll("\s+", ""));
        }
        if (this.type != null) {
            user.setType(this.type.trim());
        }
        if (username != null){
            user.setUsername(username);
            user.setEmail(this.email.trim().replaceAll("\s+", ""));
        }
        user.setPartnerId(partnerId);
        return user;
    }
}
