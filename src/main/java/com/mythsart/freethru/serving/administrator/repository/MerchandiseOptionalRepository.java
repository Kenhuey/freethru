package com.mythsart.freethru.serving.administrator.repository;

import com.mythsart.freethru.serving.administrator.repository.entity.MerchandiseOptionalDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface MerchandiseOptionalRepository extends JpaRepository<MerchandiseOptionalDo, String> {

    List<MerchandiseOptionalDo> findByMerchandiseOptionalGroupUuid(final String merchandiseOptionalGroupUuid);

    @Modifying
    @Transactional
    int deleteByMerchandiseOptionalGroupUuid(final String merchandiseOptionalGroupUuid);

}
