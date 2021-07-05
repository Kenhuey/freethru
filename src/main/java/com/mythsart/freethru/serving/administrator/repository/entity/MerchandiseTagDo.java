package com.mythsart.freethru.serving.administrator.repository.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
@Table(name = "merchandise_tag")
public class MerchandiseTagDo {

    @Id
    @GeneratedValue(generator = "uuid2")
    @Column(name = "merchandise_tag_uuid", length = 36, nullable = false)
    private String merchandiseTagUuid;

    @Column(name = "merchandise_tag_name", length = 32, nullable = false, unique = true)
    private String merchandiseTagName;

}
