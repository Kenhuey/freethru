package com.mythsart.freethru.framework.common.response.data;

import lombok.Data;

@Data
public class AdminUserData {

    private String userUuid;

    private String userName;

    private boolean userActive;

    private long userRegisterTime;

}
