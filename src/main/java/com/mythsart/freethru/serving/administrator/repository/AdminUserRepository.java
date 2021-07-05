package com.mythsart.freethru.serving.administrator.repository;

import com.mythsart.freethru.serving.administrator.repository.entity.AdminUserDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminUserRepository extends JpaRepository<AdminUserDo, String> {

    List<AdminUserDo> findByUserName(final String userName);

    List<AdminUserDo> findByUserUuid(final String userUuid);

}
