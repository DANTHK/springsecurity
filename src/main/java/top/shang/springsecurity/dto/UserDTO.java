package top.shang.springsecurity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

public interface UserDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    class UserResp {
        private int id;
        private String username;
        private String email;
        private String roles;
    }

}
