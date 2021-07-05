package com.mythsart.freethru.serving.administrator.repository.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@Entity
@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
@Table(name = "merchandise_addon")
public class MerchandiseAddonDo {

    @Id
    @GeneratedValue(generator = "uuid2")
    @Column(name = "merchandise_addon_uuid", length = 36, nullable = false)
    private String merchandiseAddonUuid;

    @Column(name = "merchandise_uuid", length = 36, nullable = false)
    private String merchandiseUuid;

    @Column(name = "merchandise_addon_name", length = 32, nullable = false)
    private String merchandiseAddonName;

    @Column(name = "merchandise_addon_price", nullable = false)
    private double merchandiseAddonPrice;

}
