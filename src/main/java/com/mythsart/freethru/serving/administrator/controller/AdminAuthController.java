package com.mythsart.freethru.serving.administrator.controller;

import com.mythsart.freethru.framework.common.CommonController;
import com.mythsart.freethru.framework.common.exception.custom.NullParameterException;
import com.mythsart.freethru.framework.common.response.ResponseBuilder;
import com.mythsart.freethru.framework.common.response.ResponseResult;
import com.mythsart.freethru.framework.common.response.data.AdminUserData;
import com.mythsart.freethru.framework.config.SwaggerConfig;
import com.mythsart.freethru.serving.administrator.repository.entity.AdminUserDo;
import com.mythsart.freethru.serving.administrator.repository.permission.AdminPermission;
import com.mythsart.freethru.serving.administrator.service.AuthService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(SwaggerConfig.PATH_ADMIN + "/auth")
@Api(tags = SwaggerConfig.TAG_NAME_ADMIN_AUTH)
@Configuration
public class AdminAuthController extends CommonController {

    private AuthService authService;

    @Autowired
    private void setAuthService(final AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/info")
    @PreAuthorize("@authService.isPermission(" + AdminPermission.REGISTER + ")")
    public ResponseEntity<ResponseResult<AdminUserData>> info() {
        final String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        final AdminUserDo adminUserDo = (AdminUserDo) this.authService.getUserDetailsByUserName(userName);
        final AdminUserData adminUserData = new AdminUserData();
        adminUserData.setUserActive(adminUserDo.isUserActive());
        adminUserData.setUserName(userName);
        adminUserData.setUserRegisterTime(adminUserDo.getUserRegisterTime());
        adminUserData.setUserUuid(adminUserDo.getUserUuid());
        return ResponseBuilder.makeOkResponse(adminUserData);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseResult<AdminUserData>> register(
            @RequestParam final String userName,
            @RequestParam final String passWord,
            @RequestParam final String registerCode) throws NullParameterException {
        if (!registerCode.equals(this.getAdminDefaultRegisterCode())) {
            return ResponseBuilder.makeResponse(HttpStatus.BAD_REQUEST, "Register code not available.");
        }
        if (userName == null
                || passWord == null
                || userName.replace(" ", "").equals("")
                || passWord.replace(" ", "").equals("")) {
            throw new NullParameterException();
        }
        final AdminUserDo adminUserDo = this.authService.registerNewDefaultUser(userName.replace(" ", ""), passWord);
        final AdminUserData adminUserData = new AdminUserData();
        adminUserData.setUserActive(adminUserDo.isUserActive());
        adminUserData.setUserName(userName);
        adminUserData.setUserRegisterTime(adminUserDo.getUserRegisterTime());
        adminUserData.setUserUuid(adminUserDo.getUserUuid());
        return ResponseBuilder.makeOkResponse(adminUserData);
    }

}
