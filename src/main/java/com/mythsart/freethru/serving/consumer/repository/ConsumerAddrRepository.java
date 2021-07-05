package com.mythsart.freethru.serving.consumer.repository;

import com.mythsart.freethru.serving.consumer.repository.entity.ConsumerAddrDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;
import java.util.List;

public interface ConsumerAddrRepository extends JpaRepository<ConsumerAddrDo, String> {

    List<ConsumerAddrDo> findByWeChatUserUuid(final String weChatUserUuid);

    List<ConsumerAddrDo> findByWeChatUserUuidAndConsumerAddrAndConsumerGenderIsMaleAndConsumerNameAndConsumerPhone(
            final String weChatUserUuid,
            final String consumerAddr,
            final boolean consumerGenderIsMale,
            final String consumerName,
            final String ConsumerPhone);

    @Modifying
    @Transactional
    void deleteByWeChatUserUuidAndConsumerAddrUuid(final String weChatUserUuid, final String consumerAddrUuid);

    ConsumerAddrDo findByWeChatUserUuidAndConsumerAddrUuid(final String weChatUserUuid, final String consumerAddrUuid);

}
