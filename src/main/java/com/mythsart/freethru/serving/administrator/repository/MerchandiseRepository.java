package com.mythsart.freethru.serving.administrator.repository;


import com.mythsart.freethru.serving.administrator.repository.entity.MerchandiseDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MerchandiseRepository extends JpaRepository<MerchandiseDo, String> {

    List<MerchandiseDo> findByMerchandiseName(final String merchandiseName);

}
