package com.mythsart.freethru.serving.administrator.repository.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@EntityListeners(AuditingEntityListener.class)
@Entity
@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
@Table(name = "merchandise_optional_group")
public class MerchandiseOptionalGroupDo {

    @Id
    @GeneratedValue(generator = "uuid2")
    @Column(name = "merchandise_optional_group_uuid", length = 36, nullable = false)
    private String merchandiseOptionalGroupUuid;

    @Column(name = "merchandise_uuid", length = 36, nullable = false)
    private String merchandiseUuid;

    @Column(name = "merchandise_optional_group_name", length = 64, nullable = false, unique = true)
    private String merchandiseOptionalGroupName;

}
