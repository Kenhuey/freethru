package com.mythsart.freethru.serving.consumer.repository;

import com.mythsart.freethru.serving.consumer.repository.entity.WechatUserDo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeChatUserRepository extends JpaRepository<WechatUserDo, String> {

    List<WechatUserDo> findByWeChatUserOpenId(final String wechatUserOpenId);

    List<WechatUserDo> findByWeChatUserUuid(final String wechatUserUuid);

}
