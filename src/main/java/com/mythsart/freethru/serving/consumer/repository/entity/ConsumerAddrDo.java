package com.mythsart.freethru.serving.consumer.repository.entity;

import com.mythsart.freethru.framework.common.exception.custom.InvalidParameterException;
import com.mythsart.freethru.framework.common.exception.custom.NullParameterException;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@Entity
@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
@Table(name = "consumer_addr",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"wechat_user_uuid", "consumer_gender", "consumer_phone", "consumer_addr", "consumer_name"})
        }
)
@EntityListeners(AuditingEntityListener.class)
public class ConsumerAddrDo {

    @Column(name = "wechat_user_uuid", length = 36, nullable = false)
    private String weChatUserUuid;

    @Id
    @GeneratedValue(generator = "uuid2")
    @Column(name = "consumer_addr_uuid", length = 36, nullable = false)
    private String consumerAddrUuid;

    @CreatedDate
    @Column(name = "consumer_addr_added_time", nullable = false, columnDefinition = "bigint default 0")
    private long consumerAddrAddedTime;

    @Column(name = "consumer_gender", nullable = false)
    private boolean consumerGenderIsMale; // true: male, false: female

    @Column(name = "consumer_phone", length = 11, nullable = false)
    private String consumerPhone;

    public void setConsumerPhone(final String consumerPhone) throws InvalidParameterException {
        if (consumerPhone.length() > 11) {
            throw new InvalidParameterException("Phone number too long.");
        }
        this.consumerPhone = consumerPhone;
    }

    @Column(name = "consumer_addr", length = 128, nullable = false)
    private String consumerAddr;

    public void setConsumerAddr(final String consumerAddr) throws InvalidParameterException {
        if (consumerAddr.length() > 128) {
            throw new InvalidParameterException("Addr too long.");
        }
        this.consumerAddr = consumerAddr;
    }

    @Column(name = "consumer_name", length = 12, nullable = false)
    private String consumerName;

    public void setConsumerName(final String consumerName) throws InvalidParameterException {
        if (consumerName.length() > 12) {
            throw new InvalidParameterException("Name too long.");
        }
        this.consumerName = consumerName;
    }

}
