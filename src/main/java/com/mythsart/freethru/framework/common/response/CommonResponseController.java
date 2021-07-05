package com.mythsart.freethru.framework.common.response;

import com.mythsart.freethru.framework.config.SwaggerConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(SwaggerConfig.PATH_COMMON)
public class CommonResponseController {

    /**
     * 404
     **/
    @GetMapping("/status/404")
    public ResponseEntity<ResponseResult<Object>> status404() {
        return ResponseBuilder.makeNotFoundResponse();
    }

    /**
     * 403
     **/
    @GetMapping("/status/403")
    public ResponseEntity<ResponseResult<Object>> status403() {
        return ResponseBuilder.makeResponse(HttpStatus.FORBIDDEN, "Forbidden.");
    }

    /**
     * 401
     */
    @GetMapping("/status/401")
    public ResponseEntity<ResponseResult<Object>> status401() {
        return ResponseBuilder.makeUnAuthorizedResponse();
    }

}
