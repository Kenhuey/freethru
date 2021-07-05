package com.mythsart.freethru.serving.administrator.repository;

import com.mythsart.freethru.serving.administrator.repository.entity.MerchandiseTagDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MerchandiseTagRepository extends JpaRepository<MerchandiseTagDo, String> {

    List<MerchandiseTagDo> findByMerchandiseTagUuid(final String MerchandiseTagUuid);

}
