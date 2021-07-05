package com.mythsart.freethru.serving.administrator.repository;

import com.mythsart.freethru.serving.administrator.repository.entity.MerchandiseImageDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface MerchandiseImageRepository extends JpaRepository<MerchandiseImageDo, String> {

    List<MerchandiseImageDo> findByMerchandiseUuid(final String merchandiseUuid);

    @Modifying
    @Transactional
    int deleteByMerchandiseUuid(final String merchandiseUuid);

}
