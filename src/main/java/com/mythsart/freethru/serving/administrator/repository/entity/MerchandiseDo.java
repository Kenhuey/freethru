package com.mythsart.freethru.serving.administrator.repository.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@EntityListeners(AuditingEntityListener.class)
@Entity
@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
@Table(name = "merchandise")
public class MerchandiseDo {

    @Id
    @GeneratedValue(generator = "uuid2")
    @Column(name = "merchandise_uuid", length = 36, nullable = false)
    private String merchandiseUuid;

    @Column(name = "merchandise_name", length = 32, nullable = false)
    private String merchandiseName;

    @Column(name = "merchandise_tag_name", length = 36)
    private String merchandiseTagUuid;

    @Column(name = "merchandise_description", length = 256)
    private String merchandiseDescription = "None description.";

    @Column(name = "merchandise_price", nullable = false)
    private double merchandisePrice;

    @Column(name = "merchandise_image", columnDefinition = "MediumBlob")
    private byte[] merchandiseImage;

}
