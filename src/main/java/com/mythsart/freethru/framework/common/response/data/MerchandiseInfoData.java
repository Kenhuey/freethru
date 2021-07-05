package com.mythsart.freethru.framework.common.response.data;

import com.mythsart.freethru.serving.administrator.repository.entity.MerchandiseAddonDo;
import com.mythsart.freethru.serving.administrator.repository.entity.MerchandiseDo;
import com.mythsart.freethru.serving.administrator.repository.entity.MerchandiseImageDo;
import lombok.Data;

import java.util.List;

@Data
public class MerchandiseInfoData {

    private List<MerchandiseImageDo> imageUuidList;

    private MerchandiseDo merchandiseDo;

    private List<MerchandiseOptionalGroupInfo> merchandiseOptionalGroupInfoList;

    private List<MerchandiseAddonDo> merchandiseAddonDoList;

}
