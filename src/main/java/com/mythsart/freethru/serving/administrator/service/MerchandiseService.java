package com.mythsart.freethru.serving.administrator.service;

import com.mythsart.freethru.framework.common.exception.custom.DataNotExistException;
import com.mythsart.freethru.framework.common.exception.custom.InvalidParameterException;
import com.mythsart.freethru.framework.common.response.data.MerchandiseInfoData;
import com.mythsart.freethru.serving.administrator.repository.entity.MerchandiseDo;
import com.mythsart.freethru.serving.administrator.repository.entity.MerchandiseImageDo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MerchandiseService {

    List<MerchandiseInfoData> getAllMerchandiseInfoData() throws DataNotExistException;

    MerchandiseDo addMerchandise(final String merchandiseName, final String merchandiseDescription, final double merchandisePrice);

    MerchandiseInfoData getMerchandiseByUuid(final String merchandiseUuid) throws DataNotExistException;

    MerchandiseDo setMerchandiseBannerImage(final MultipartFile data, final String merchandiseUuid) throws IOException, InvalidParameterException, DataNotExistException;

    byte[] responseMerchandiseBannerImage(final String merchandiseUuid) throws DataNotExistException;

    MerchandiseImageDo addMerchandiseImage(final MultipartFile data, final String merchandiseUuid) throws DataNotExistException, IOException, InvalidParameterException;

    List<MerchandiseImageDo> getMerchandiseImageListByUuid(final String merchandiseUuid) throws DataNotExistException;

    byte[] responseMerchandiseImage(final String merchandiseImageUuid) throws DataNotExistException;

}
