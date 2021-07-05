package com.mythsart.freethru.serving.administrator.repository;

import com.mythsart.freethru.serving.administrator.repository.entity.MerchandiseOptionalGroupDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MerchandiseOptionalGroupRepository extends JpaRepository<MerchandiseOptionalGroupDo, String> {

    List<MerchandiseOptionalGroupDo> findByMerchandiseUuid(final String merchandiseUuid);

}
