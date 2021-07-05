package com.mythsart.freethru.serving.consumer.controller;

import com.mythsart.freethru.framework.config.CommonConfig;
import com.mythsart.freethru.framework.common.CommonController;
import com.mythsart.freethru.framework.common.exception.custom.*;
import com.mythsart.freethru.framework.common.response.ResponseBuilder;
import com.mythsart.freethru.framework.common.response.ResponseResult;
import com.mythsart.freethru.framework.common.util.jwt.JwtTokenUtil;
import com.mythsart.freethru.framework.jwt.annotation.WeChatUser;
import com.mythsart.freethru.framework.config.SwaggerConfig;
import com.mythsart.freethru.serving.consumer.repository.entity.ConsumerAddrDo;
import com.mythsart.freethru.serving.consumer.service.ConsumerServices;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(SwaggerConfig.PATH_CONSUMER + "/delivery")
@Api(tags = SwaggerConfig.TAG_NAME_CONSUMER_DELIVERY)
public class ConsumerDeliveryController extends CommonController {

    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private void setJwtTokenUtil(final JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    private ConsumerServices consumerServices;

    @Autowired
    private void setConsumerServices(final ConsumerServices consumerServices) {
        this.consumerServices = consumerServices;
    }

    /**
     * add address
     **/
    @PostMapping("/addr/add")
    @WeChatUser
    public ResponseEntity<ResponseResult<ConsumerAddrDo>> addAddr(
            @RequestHeader(name = CommonConfig.HEADER_TOKEN_NAME, required = false) final String token,
            @RequestParam(name = "addr") final String addr,
            @RequestParam(name = "genderIsMale") final boolean genderIsMale,
            @RequestParam(name = "name") final String name,
            @RequestParam(name = "phone") final String phone) throws NullParameterException, InvalidParameterException, DataExistException, NullTokenException {
        final String weChatUserUuid = this.jwtTokenUtil.getPayloadByFrontToken(token);
        return ResponseBuilder.makeOkResponse(this.consumerServices.addConsumerAddr(addr, genderIsMale, name, phone, weChatUserUuid));
    }

    /**
     * get address
     **/
    @GetMapping("/addr/get")
    @WeChatUser
    public ResponseEntity<ResponseResult<List<ConsumerAddrDo>>> getAllAddr(
            @RequestHeader(name = CommonConfig.HEADER_TOKEN_NAME, required = false) final String token,
            @RequestParam(name = "consumerAddrUuid", required = false) final String consumerAddrUuid) throws NullTokenException {
        final String weChatUserUuid = this.jwtTokenUtil.getPayloadByFrontToken(token);
        List<ConsumerAddrDo> consumerAddrDoList;
        if (consumerAddrUuid == null) {
            consumerAddrDoList = this.consumerServices.getConsumerAddr(weChatUserUuid);
        } else {
            consumerAddrDoList = this.consumerServices.getSingleConsumerAddr(weChatUserUuid, consumerAddrUuid);
        }
        if (consumerAddrDoList == null || consumerAddrDoList.isEmpty()) {
            return ResponseBuilder.makeResponse(null, HttpStatus.NOT_FOUND, "Empty addr list.");
        }
        return ResponseBuilder.makeOkResponse(consumerAddrDoList);
    }

    /**
     * remove address
     */
    @DeleteMapping("/addr/remove")
    @WeChatUser
    public ResponseEntity<ResponseResult<Object>> removeAddr(
            @RequestHeader(name = CommonConfig.HEADER_TOKEN_NAME, required = false) final String token,
            @RequestParam(name = "consumerAddrUuid") final String consumerAddrUuid) throws NullTokenException {
        final String weChatUserUuid = this.jwtTokenUtil.getPayloadByFrontToken(token);
        this.consumerServices.removeConsumerAddr(consumerAddrUuid, weChatUserUuid);
        return ResponseBuilder.makeOkResponse();
    }

    /**
     * update address
     **/
    @PutMapping("/addr/alter")
    @WeChatUser
    public ResponseEntity<ResponseResult<ConsumerAddrDo>> updateAddr(
            @RequestHeader(name = CommonConfig.HEADER_TOKEN_NAME, required = false) final String token,
            @RequestParam(name = "consumerAddrUuid") final String consumerAddrUuid,
            @RequestParam(name = "addr", required = false) final String addr,
            @RequestParam(name = "genderIsMale", required = false) final String genderIsMale,
            @RequestParam(name = "name", required = false) final String name,
            @RequestParam(name = "phone", required = false) final String phone)
            throws NullTokenException, DataNotExistException, InvalidParameterException, NullParameterException {
        final String weChatUserUuid = this.jwtTokenUtil.getPayloadByFrontToken(token);
        return ResponseBuilder.makeOkResponse(this.consumerServices.updateConsumerAddr(consumerAddrUuid, weChatUserUuid, addr, genderIsMale, name, phone));
    }

}
