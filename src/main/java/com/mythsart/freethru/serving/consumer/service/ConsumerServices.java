package com.mythsart.freethru.serving.consumer.service;

import com.mythsart.freethru.framework.common.exception.custom.DataExistException;
import com.mythsart.freethru.framework.common.exception.custom.DataNotExistException;
import com.mythsart.freethru.framework.common.exception.custom.InvalidParameterException;
import com.mythsart.freethru.framework.common.exception.custom.NullParameterException;
import com.mythsart.freethru.serving.consumer.repository.entity.ConsumerAddrDo;

import java.util.List;

public interface ConsumerServices {

    ConsumerAddrDo addConsumerAddr(final String addr, final boolean gender, final String name, final String phone, final String weChatUserUuid) throws InvalidParameterException, NullParameterException, DataExistException;

    List<ConsumerAddrDo> getConsumerAddr(final String weChatUserUuid);

    void removeConsumerAddr(final String consumerAddrUuid, final String weChatUserUuid);

    ConsumerAddrDo updateConsumerAddr(final String consumerAddrUuid, final String weChatUserUuid, final String addr, final String genderIsMale, final String name, final String phone) throws DataNotExistException, InvalidParameterException, NullParameterException;

    List<ConsumerAddrDo> getSingleConsumerAddr(final String weChatUserUuid, final String consumerAddrUuid);

}
