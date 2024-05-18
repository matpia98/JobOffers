package com.joboffers.domain.loginandregister;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
class User {
    private Long id;
    private String username;
    private String password;
}
