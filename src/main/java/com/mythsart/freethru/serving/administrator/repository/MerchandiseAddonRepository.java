package com.mythsart.freethru.serving.administrator.repository;

import com.mythsart.freethru.serving.administrator.repository.entity.MerchandiseAddonDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface MerchandiseAddonRepository extends JpaRepository<MerchandiseAddonDo, String> {

    @Modifying
    @Transactional
    int deleteByMerchandiseUuid(final String MerchandiseUuid);

    List<MerchandiseAddonDo> findByMerchandiseUuid(final String MerchandiseUuid);

}
