package com.mythsart.freethru.serving.consumer.service.impl;

import com.mythsart.freethru.framework.common.exception.custom.DataExistException;
import com.mythsart.freethru.framework.common.exception.custom.DataNotExistException;
import com.mythsart.freethru.framework.common.exception.custom.InvalidParameterException;
import com.mythsart.freethru.framework.common.exception.custom.NullParameterException;
import com.mythsart.freethru.framework.common.util.RegexUtil;
import com.mythsart.freethru.serving.consumer.repository.ConsumerAddrRepository;
import com.mythsart.freethru.serving.consumer.repository.entity.ConsumerAddrDo;
import com.mythsart.freethru.serving.consumer.service.ConsumerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConsumerServicesImpl implements ConsumerServices {

    private RegexUtil regexUtil;

    @Autowired
    private void setRegexUtil(final RegexUtil regexUtil) {
        this.regexUtil = regexUtil;
    }

    private ConsumerAddrRepository consumerAddrRepository;

    @Autowired
    private void setConsumerAddrRepository(final ConsumerAddrRepository consumerAddrRepository) {
        this.consumerAddrRepository = consumerAddrRepository;
    }

    @Override
    public ConsumerAddrDo addConsumerAddr(
            final String addr,
            final boolean gender,
            final String name,
            final String phone,
            final String weChatUserUuid) throws InvalidParameterException, NullParameterException, DataExistException {
        if (!this.regexUtil.isPhoneNumberValid(phone)) {
            throw new InvalidParameterException("Invalid phone number.");
        }
        if (this.regexUtil.removeBlankChar(addr) == null
                || this.regexUtil.removeBlankChar(name) == null
                || this.regexUtil.removeBlankChar(weChatUserUuid) == null) {
            throw new NullParameterException();
        }
        if (!this.consumerAddrRepository.findByWeChatUserUuidAndConsumerAddrAndConsumerGenderIsMaleAndConsumerNameAndConsumerPhone(
                weChatUserUuid, addr, gender, name, phone).isEmpty()) {
            throw new DataExistException();
        }
        final ConsumerAddrDo consumerAddrDo = new ConsumerAddrDo();
        consumerAddrDo.setConsumerAddr(this.regexUtil.removeBlankChar(addr));
        consumerAddrDo.setConsumerGenderIsMale(gender);
        consumerAddrDo.setConsumerName(this.regexUtil.removeBlankChar(name));
        consumerAddrDo.setWeChatUserUuid(weChatUserUuid);
        consumerAddrDo.setConsumerPhone(phone);
        this.consumerAddrRepository.save(consumerAddrDo);
        return consumerAddrDo;
    }

    @Override
    public List<ConsumerAddrDo> getConsumerAddr(final String weChatUserUuid) {
        return this.consumerAddrRepository.findByWeChatUserUuid(weChatUserUuid);
    }

    @Override
    public void removeConsumerAddr(final String consumerAddrUuid, final String weChatUserUuid) {
        this.consumerAddrRepository.deleteByWeChatUserUuidAndConsumerAddrUuid(weChatUserUuid, consumerAddrUuid);
    }

    @Override
    public ConsumerAddrDo updateConsumerAddr(
            final String consumerAddrUuid,
            final String weChatUserUuid,
            final String addr,
            final String genderIsMale,
            final String name,
            final String phone) throws DataNotExistException, InvalidParameterException {
        final ConsumerAddrDo consumerAddrDo = this.consumerAddrRepository.findByWeChatUserUuidAndConsumerAddrUuid(weChatUserUuid, consumerAddrUuid);
        if (consumerAddrDo == null) {
            throw new DataNotExistException();
        }
        if (this.regexUtil.removeBlankChar(addr) != null && !consumerAddrDo.getConsumerAddr().equals(this.regexUtil.removeBlankChar(addr))) {
            consumerAddrDo.setConsumerAddr(addr);
        }
        if (this.regexUtil.removeBlankChar(genderIsMale) != null) {
            if (this.regexUtil.removeBlankChar(genderIsMale).equals("true")) {
                consumerAddrDo.setConsumerGenderIsMale(true);
            } else if (this.regexUtil.removeBlankChar(genderIsMale).equals("false")) {
                consumerAddrDo.setConsumerGenderIsMale(false);
            } else {
                throw new InvalidParameterException();
            }
        }
        if (this.regexUtil.removeBlankChar(name) != null && !consumerAddrDo.getConsumerName().equals(this.regexUtil.removeBlankChar(name))) {
            consumerAddrDo.setConsumerName(name);
        }
        if (this.regexUtil.removeBlankChar(phone) != null && !consumerAddrDo.getConsumerPhone().equals(this.regexUtil.removeBlankChar(phone))) {
            if (this.regexUtil.isPhoneNumberValid(this.regexUtil.removeBlankChar(phone))) {
                consumerAddrDo.setConsumerPhone(phone);
            }
        }
        this.consumerAddrRepository.save(consumerAddrDo);
        return consumerAddrDo;
    }

    @Override
    public List<ConsumerAddrDo> getSingleConsumerAddr(final String weChatUserUuid, final String consumerAddrUuid) {
        final List<ConsumerAddrDo> consumerAddrDoList = new ArrayList<>();
        consumerAddrDoList.add((this.consumerAddrRepository.findByWeChatUserUuidAndConsumerAddrUuid(weChatUserUuid, consumerAddrUuid)));
        return consumerAddrDoList;
    }

}
