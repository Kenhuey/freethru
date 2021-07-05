package com.mythsart.freethru.framework.common;

import com.mythsart.freethru.framework.config.CommonConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Data
@EqualsAndHashCode(callSuper = true)
@Configuration
public class CommonController extends CommonConfig {

    private final Logger logger = LoggerFactory.getLogger(CommonController.class);

}
