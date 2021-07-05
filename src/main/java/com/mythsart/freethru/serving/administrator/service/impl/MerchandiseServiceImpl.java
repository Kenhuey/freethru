package com.mythsart.freethru.serving.administrator.service.impl;

import com.mythsart.freethru.framework.common.exception.custom.DataNotExistException;
import com.mythsart.freethru.framework.common.exception.custom.InvalidParameterException;
import com.mythsart.freethru.framework.common.response.data.MerchandiseInfoData;
import com.mythsart.freethru.serving.administrator.repository.MerchandiseImageRepository;
import com.mythsart.freethru.serving.administrator.repository.MerchandiseRepository;
import com.mythsart.freethru.serving.administrator.repository.entity.MerchandiseDo;
import com.mythsart.freethru.serving.administrator.repository.entity.MerchandiseImageDo;
import com.mythsart.freethru.serving.administrator.service.MerchandiseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MerchandiseServiceImpl implements MerchandiseService {

    private MerchandiseImageRepository merchandiseImageRepository;

    @Autowired
    private void setMerchandiseImageRepository(final MerchandiseImageRepository merchandiseImageRepository) {
        this.merchandiseImageRepository = merchandiseImageRepository;
    }

    private MerchandiseRepository merchandiseRepository;

    @Autowired
    private void setMerchandiseRepository(final MerchandiseRepository merchandiseRepository) {
        this.merchandiseRepository = merchandiseRepository;
    }

    @Override
    public List<MerchandiseInfoData> getAllMerchandiseInfoData() throws DataNotExistException {
        final List<MerchandiseInfoData> merchandiseInfoDataList = new ArrayList<>();
        final List<MerchandiseDo> merchandiseDoList = this.merchandiseRepository.findAll();
        if (merchandiseDoList.isEmpty()) {
            return null;
        }
        for (final MerchandiseDo merchandiseDoListChild : merchandiseDoList) {
            final MerchandiseInfoData merchandiseInfoData = new MerchandiseInfoData();
            merchandiseDoListChild.setMerchandiseImage(null);
            merchandiseInfoData.setImageUuidList(this.getMerchandiseImageListByUuid(merchandiseDoListChild.getMerchandiseUuid()));
            merchandiseInfoData.setMerchandiseDo(merchandiseDoListChild);
            merchandiseInfoDataList.add(merchandiseInfoData);
        }
        return merchandiseInfoDataList;
    }

    @Override
    public MerchandiseDo addMerchandise(final String merchandiseName, final String merchandiseDescription, final double merchandisePrice) {
        final MerchandiseDo merchandiseDo = new MerchandiseDo();
        merchandiseDo.setMerchandiseName(merchandiseName);
        merchandiseDo.setMerchandiseDescription(merchandiseDescription);
        merchandiseDo.setMerchandisePrice(merchandisePrice);
        this.merchandiseRepository.save(merchandiseDo);
        return merchandiseDo;
    }

    @Override
    public MerchandiseInfoData getMerchandiseByUuid(final String merchandiseUuid) throws DataNotExistException {
        final MerchandiseInfoData merchandiseInfoData = new MerchandiseInfoData();
        final Optional<MerchandiseDo> merchandiseDoOptional = this.merchandiseRepository.findById(merchandiseUuid);
        if (merchandiseDoOptional.isEmpty()) {
            return null;
        }
        merchandiseInfoData.setImageUuidList(this.getMerchandiseImageListByUuid(merchandiseUuid));
        merchandiseInfoData.setMerchandiseDo(merchandiseDoOptional.get());
        return merchandiseInfoData;
    }

    @Override
    public MerchandiseDo setMerchandiseBannerImage(final MultipartFile data, final String merchandiseUuid) throws IOException, InvalidParameterException, DataNotExistException {
        MerchandiseDo merchandiseDo;
        if (!this.merchandiseRepository.findById(merchandiseUuid).isEmpty()) {
            merchandiseDo = this.merchandiseRepository.findById(merchandiseUuid).get();
        } else {
            throw new DataNotExistException("Not exist uuid");
        }
        BufferedImage bufferedImage;
        bufferedImage = ImageIO.read(data.getInputStream());
        if (bufferedImage == null || bufferedImage.getWidth() <= 0 || bufferedImage.getHeight() <= 0) {
            throw new InvalidParameterException("Not a image file.");
        }
        merchandiseDo.setMerchandiseImage(data.getBytes());
        this.merchandiseRepository.save(merchandiseDo);
        return merchandiseDo;
    }

    @Override
    public byte[] responseMerchandiseBannerImage(final String merchandiseUuid) throws DataNotExistException {
        MerchandiseDo merchandiseDo;
        if (!this.merchandiseRepository.findById(merchandiseUuid).isEmpty()) {
            merchandiseDo = this.merchandiseRepository.findById(merchandiseUuid).get();
        } else {
            throw new DataNotExistException("Not exist uuid");
        }
        final byte[] data = merchandiseDo.getMerchandiseImage();
        final String fileName = merchandiseDo.getMerchandiseUuid() + ".png";
        final ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        final HttpServletResponse httpServletResponse = servletRequestAttributes.getResponse();
        httpServletResponse.setHeader("content-type", "application/octet-stream");
        httpServletResponse.setContentType("application/octet-stream");
        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        return data;
    }

    @Override
    public byte[] responseMerchandiseImage(final String merchandiseImageUuid) throws DataNotExistException {
        MerchandiseImageDo merchandiseImageDo;
        if (!this.merchandiseImageRepository.findById(merchandiseImageUuid).isEmpty()) {
            merchandiseImageDo = this.merchandiseImageRepository.findById(merchandiseImageUuid).get();
        } else {
            throw new DataNotExistException("Not exist uuid");
        }
        final byte[] data = merchandiseImageDo.getMerchandiseImage();
        final String fileName = merchandiseImageDo.getMerchandiseUuid() + ".png";
        final ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        final HttpServletResponse httpServletResponse = servletRequestAttributes.getResponse();
        httpServletResponse.setHeader("content-type", "application/octet-stream");
        httpServletResponse.setContentType("application/octet-stream");
        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        return data;
    }

    @Override
    public MerchandiseImageDo addMerchandiseImage(final MultipartFile data, final String merchandiseUuid)
            throws DataNotExistException, IOException, InvalidParameterException {
        if (this.merchandiseRepository.findById(merchandiseUuid).isEmpty()) {
            throw new DataNotExistException("Not exist uuid");
        }
        BufferedImage bufferedImage;
        bufferedImage = ImageIO.read(data.getInputStream());
        if (bufferedImage == null || bufferedImage.getWidth() <= 0 || bufferedImage.getHeight() <= 0) {
            throw new InvalidParameterException("Not a image file.");
        }
        final MerchandiseImageDo merchandiseImageDo = new MerchandiseImageDo();
        merchandiseImageDo.setMerchandiseUuid(merchandiseUuid);
        merchandiseImageDo.setMerchandiseImage(data.getBytes());
        this.merchandiseImageRepository.save(merchandiseImageDo);
        return merchandiseImageDo;
    }

    @Override
    public List<MerchandiseImageDo> getMerchandiseImageListByUuid(final String merchandiseUuid) throws DataNotExistException {
        if (this.merchandiseRepository.findById(merchandiseUuid).isEmpty()) {
            throw new DataNotExistException("Not exist uuid");
        }
        final List<MerchandiseImageDo> merchandiseImageDoList = this.merchandiseImageRepository.findByMerchandiseUuid(merchandiseUuid);
        for (final MerchandiseImageDo merchandiseImageDo : merchandiseImageDoList) {
            merchandiseImageDo.setMerchandiseImage(null);
        }
        return merchandiseImageDoList;
    }

}
