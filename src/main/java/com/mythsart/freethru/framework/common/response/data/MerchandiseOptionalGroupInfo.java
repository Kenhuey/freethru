package com.mythsart.freethru.framework.common.response.data;

import com.mythsart.freethru.serving.administrator.repository.entity.MerchandiseAddonDo;
import com.mythsart.freethru.serving.administrator.repository.entity.MerchandiseOptionalDo;
import com.mythsart.freethru.serving.administrator.repository.entity.MerchandiseOptionalGroupDo;
import lombok.Data;

import java.util.List;

@Data
public class MerchandiseOptionalGroupInfo {

    private MerchandiseOptionalGroupDo merchandiseOptionalGroupDo;

    private List<MerchandiseOptionalDo> merchandiseOptionalDoList;

}
