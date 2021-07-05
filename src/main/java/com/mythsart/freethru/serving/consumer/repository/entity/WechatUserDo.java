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
@Table(name = "wechat_user",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"wechat_user_uuid", "wechat_user_open_id"})
        }
)
@EntityListeners(AuditingEntityListener.class)
public class WechatUserDo {

    @Id
    @GeneratedValue(generator = "uuid2")
    @Column(name = "wechat_user_uuid", length = 36, nullable = false)
    private String weChatUserUuid;

    @Column(name = "wechat_user_open_id", length = 28, nullable = false, unique = true)
    private String weChatUserOpenId;

    @CreatedDate
    @Column(name = "wechat_user_register_time", nullable = false, columnDefinition = "bigint default 0")
    private long weChatUserRegisterTime;

}
