package com.mythsart.freethru.serving.administrator.controller;

import com.mythsart.freethru.framework.common.CommonController;
import com.mythsart.freethru.framework.common.exception.custom.DataNotExistException;
import com.mythsart.freethru.framework.common.exception.custom.InvalidParameterException;
import com.mythsart.freethru.framework.common.exception.custom.NullParameterException;
import com.mythsart.freethru.framework.common.response.ResponseBuilder;
import com.mythsart.freethru.framework.common.response.ResponseResult;
import com.mythsart.freethru.framework.common.response.data.MerchandiseInfoData;
import com.mythsart.freethru.framework.common.response.data.MerchandiseOptionalGroupInfo;
import com.mythsart.freethru.framework.config.SwaggerConfig;
import com.mythsart.freethru.serving.administrator.repository.*;
import com.mythsart.freethru.serving.administrator.repository.entity.*;
import com.mythsart.freethru.serving.administrator.repository.permission.AdminPermission;
import com.mythsart.freethru.serving.administrator.service.MerchandiseService;
import io.swagger.annotations.Api;
import io.swagger.models.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(SwaggerConfig.PATH_ADMIN + "/merchandise")
@Api(tags = SwaggerConfig.TAG_NAME_ADMIN_MANAGE)
@Configuration
public class AdminManagementController extends CommonController {

    private MerchandiseAddonRepository merchandiseAddonRepository;

    @Autowired
    private void setMerchandiseAddonRepository(final MerchandiseAddonRepository merchandiseAddonRepository) {
        this.merchandiseAddonRepository = merchandiseAddonRepository;
    }

    private MerchandiseService merchandiseService;

    @PostMapping("/addon/add")
    @PreAuthorize("@authService.isPermission(" + AdminPermission.ADMIN + ")")
    public ResponseEntity<ResponseResult<MerchandiseAddonDo>> addAddon(
            @RequestParam final String merchandiseUuid,
            @RequestParam final String addonName,
            @RequestParam final Double addonPrice) throws DataNotExistException, NullParameterException, InvalidParameterException {
        final Optional<MerchandiseDo> merchandiseDoOptional = this.merchandiseRepository.findById(merchandiseUuid);
        if (merchandiseDoOptional.isEmpty()) {
            throw new DataNotExistException("Merchandise not exist.");
        }
        final MerchandiseAddonDo merchandiseAddonDo = new MerchandiseAddonDo();
        merchandiseAddonDo.setMerchandiseUuid(merchandiseUuid);
        if (addonName == null || addonName.replace(" ", "").equals("")) {
            throw new NullParameterException("Not allow null addon name.");
        }
        merchandiseAddonDo.setMerchandiseAddonName(addonName);
        if (addonPrice != null && addonPrice <= 0) {
            throw new InvalidParameterException("Price must more than 0.");
        }
        merchandiseAddonDo.setMerchandiseAddonPrice(addonPrice);
        this.merchandiseAddonRepository.save(merchandiseAddonDo);
        return ResponseBuilder.makeOkResponse(merchandiseAddonDo);
    }

    @PutMapping("/addon/alter")
    @PreAuthorize("@authService.isPermission(" + AdminPermission.ADMIN + ")")
    public ResponseEntity<ResponseResult<MerchandiseAddonDo>> updateAddon(
            @RequestParam final String merchandiseAddonUuid,
            final String addonName,
            final Double addonPrice) throws DataNotExistException {
        if (this.merchandiseAddonRepository.findById(merchandiseAddonUuid).isEmpty()) {
            throw new DataNotExistException("Addon not found.");
        }
        final MerchandiseAddonDo merchandiseAddonDo = this.merchandiseAddonRepository.findById(merchandiseAddonUuid).get();
        if (addonName != null || !addonName.replace(" ", "").equals("")) {
            merchandiseAddonDo.setMerchandiseAddonName(addonName);
        }
        if (addonPrice != null && addonPrice >= 0) {
            merchandiseAddonDo.setMerchandiseAddonPrice(addonPrice);
        }
        this.merchandiseAddonRepository.save(merchandiseAddonDo);
        return ResponseBuilder.makeOkResponse(merchandiseAddonDo);
    }

    @Autowired
    private void setMerchandiseService(final MerchandiseService merchandiseService) {
        this.merchandiseService = merchandiseService;
    }

    @GetMapping("/public/merchandise/get")
    public ResponseEntity<ResponseResult<Object>> getMerchandiseInfo(final String merchandiseUuid) throws DataNotExistException {
        final List<MerchandiseOptionalGroupInfo> merchandiseOptionalGroupInfoList = new ArrayList<>();
        if (merchandiseUuid == null || merchandiseUuid.replace(" ", "").equals("")) {
            final List<MerchandiseInfoData> merchandiseInfoDataList = this.merchandiseService.getAllMerchandiseInfoData();
            if (merchandiseInfoDataList == null || merchandiseInfoDataList.isEmpty()) {
                return ResponseBuilder.makeNotFoundResponse();
            }
            for (final MerchandiseInfoData merchandiseInfoData : merchandiseInfoDataList) {
                final List<MerchandiseOptionalGroupInfo> merchandiseOptionalGroupInfoList2 = new ArrayList<>();
                for (final MerchandiseOptionalGroupDo merchandiseOptionalGroupDo : this.merchandiseOptionalGroupRepository.findByMerchandiseUuid(merchandiseInfoData.getMerchandiseDo().getMerchandiseUuid())) {
                    final MerchandiseOptionalGroupInfo merchandiseOptionalGroupInfo = new MerchandiseOptionalGroupInfo();
                    merchandiseOptionalGroupInfo.setMerchandiseOptionalGroupDo(merchandiseOptionalGroupDo);
                    merchandiseOptionalGroupInfo.setMerchandiseOptionalDoList(this.merchandiseOptionalRepository.findByMerchandiseOptionalGroupUuid(merchandiseOptionalGroupDo.getMerchandiseOptionalGroupUuid()));
                    merchandiseOptionalGroupInfoList2.add(merchandiseOptionalGroupInfo);
                }
                merchandiseInfoData.setMerchandiseOptionalGroupInfoList(merchandiseOptionalGroupInfoList2);
                merchandiseInfoData.setMerchandiseAddonDoList(
                        this.merchandiseAddonRepository.findByMerchandiseUuid(
                                merchandiseInfoData.getMerchandiseDo().getMerchandiseUuid())
                );
                if (this.merchandiseTagRepository.findById(merchandiseInfoData.getMerchandiseDo().getMerchandiseTagUuid()).isEmpty()) {
                    merchandiseInfoData.getMerchandiseDo().setMerchandiseTagUuid(null);
                }
            }
            return ResponseBuilder.makeOkResponse(merchandiseInfoDataList);
        }
        final MerchandiseInfoData merchandiseInfoData = this.merchandiseService.getMerchandiseByUuid(merchandiseUuid);
        if (merchandiseInfoData == null) {
            return ResponseBuilder.makeNotFoundResponse();
        }
        merchandiseInfoData.getMerchandiseDo().setMerchandiseImage(null);
        for (final MerchandiseOptionalGroupDo merchandiseOptionalGroupDo : this.merchandiseOptionalGroupRepository.findByMerchandiseUuid(merchandiseUuid)) {
            final MerchandiseOptionalGroupInfo merchandiseOptionalGroupInfo = new MerchandiseOptionalGroupInfo();
            merchandiseOptionalGroupInfo.setMerchandiseOptionalGroupDo(merchandiseOptionalGroupDo);
            merchandiseOptionalGroupInfo.setMerchandiseOptionalDoList(this.merchandiseOptionalRepository.findByMerchandiseOptionalGroupUuid(merchandiseOptionalGroupDo.getMerchandiseOptionalGroupUuid()));
            merchandiseOptionalGroupInfoList.add(merchandiseOptionalGroupInfo);
        }
        merchandiseInfoData.setMerchandiseOptionalGroupInfoList(merchandiseOptionalGroupInfoList);
        merchandiseInfoData.setMerchandiseAddonDoList(this.merchandiseAddonRepository.findByMerchandiseUuid(merchandiseUuid));
        if (this.merchandiseTagRepository.findById(merchandiseInfoData.getMerchandiseDo().getMerchandiseTagUuid()).isEmpty()) {
            merchandiseInfoData.getMerchandiseDo().setMerchandiseTagUuid(null);
        }
        return ResponseBuilder.makeOkResponse(merchandiseInfoData);
    }

    @GetMapping("/public/merchandise/banner/get")
    public byte[] getMerchandiseBanner(@RequestParam final String merchandiseUuid) throws DataNotExistException {
        return this.merchandiseService.responseMerchandiseBannerImage(merchandiseUuid);
    }

    @PostMapping("/add")
    @PreAuthorize("@authService.isPermission(" + AdminPermission.ADMIN + ")")
    public ResponseEntity<ResponseResult<MerchandiseDo>> addMerchandise(
            @RequestParam final String merchandiseName,
            @RequestParam(required = false) final String merchandiseDescription,
            @RequestParam final Double merchandisePrice) throws NullParameterException, InvalidParameterException {
        if (merchandiseName == null
                || merchandiseName.replace(" ", "").equals("")) {
            throw new NullParameterException();
        }
        if (merchandisePrice < 0) {
            throw new InvalidParameterException("Merchandise price must more than 0.");
        }
        final MerchandiseDo merchandiseDo = this.merchandiseService.addMerchandise(merchandiseName, merchandiseDescription, merchandisePrice);
        merchandiseDo.setMerchandiseImage(null);
        return ResponseBuilder.makeOkResponse(merchandiseDo);
    }

    private MerchandiseRepository merchandiseRepository;

    @Autowired
    private void setMerchandiseRepository(final MerchandiseRepository merchandiseRepository) {
        this.merchandiseRepository = merchandiseRepository;
    }

    @PutMapping("/alter")
    @PreAuthorize("@authService.isPermission(" + AdminPermission.ADMIN + ")")
    public ResponseEntity<ResponseResult<MerchandiseDo>> updateMerchandise(
            @RequestParam final String merchandiseUuid,
            @RequestParam(required = false) final String merchandiseName,
            @RequestParam(required = false) final String merchandiseDescription,
            @RequestParam(required = false) final Double merchandisePrice,
            @RequestParam(required = false) final String tagUuid) throws DataNotExistException, InvalidParameterException {
        final Optional<MerchandiseDo> merchandiseDoOptional = this.merchandiseRepository.findById(merchandiseUuid);
        if (merchandiseDoOptional.isEmpty()) {
            throw new DataNotExistException("Merchandise not exist.");
        }
        final MerchandiseDo merchandiseDo = merchandiseDoOptional.get();
        if (merchandiseName != null && !merchandiseName.replace(" ", "").equals("")) {
            merchandiseDo.setMerchandiseName(merchandiseName);
        }
        if (merchandiseDescription != null && !merchandiseDescription.replace(" ", "").equals("")) {
            merchandiseDo.setMerchandiseDescription(merchandiseDescription);
        }
        if (merchandisePrice != null && merchandisePrice >= 0) {
            merchandiseDo.setMerchandisePrice(merchandisePrice);
        }
        if (tagUuid != null || !tagUuid.replace(" ", "").equals("")) {
            if (!this.merchandiseTagRepository.findById(tagUuid).isEmpty()) {
                merchandiseDo.setMerchandiseTagUuid(tagUuid);
            }
        }
        this.merchandiseRepository.save(merchandiseDo);
        merchandiseDo.setMerchandiseImage(null);
        return ResponseBuilder.makeOkResponse(merchandiseDo);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("@authService.isPermission(" + AdminPermission.ADMIN + ")")
    public ResponseEntity<ResponseResult<Object>> deleteMerchandise(@RequestParam final String merchandiseUuid) throws DataNotExistException {
        final Optional<MerchandiseDo> merchandiseDoOptional = this.merchandiseRepository.findById(merchandiseUuid);
        if (merchandiseDoOptional.isEmpty()) {
            throw new DataNotExistException("Merchandise not exist.");
        }
        this.merchandiseRepository.delete(merchandiseDoOptional.get());
        final List<MerchandiseOptionalGroupDo> merchandiseOptionalGroupDos =
                this.merchandiseOptionalGroupRepository.findByMerchandiseUuid(merchandiseUuid);
        this.merchandiseImageRepository.deleteByMerchandiseUuid(merchandiseUuid);
        if (!merchandiseOptionalGroupDos.isEmpty()) {
            for (final MerchandiseOptionalGroupDo merchandiseOptionalGroupDo : merchandiseOptionalGroupDos) {
                this.merchandiseOptionalRepository.deleteByMerchandiseOptionalGroupUuid(merchandiseOptionalGroupDo.getMerchandiseOptionalGroupUuid());
                this.merchandiseOptionalGroupRepository.delete(merchandiseOptionalGroupDo);
            }
        }
        return ResponseBuilder.makeOkResponse();
    }

    @PutMapping("/banner/alter")
    @PreAuthorize("@authService.isPermission(" + AdminPermission.ADMIN + ")")
    public ResponseEntity<ResponseResult<MerchandiseDo>> updateMerchandiseBanner(
            @RequestParam(value = "file") final MultipartFile data,
            @RequestParam final String merchandiseUuid) throws InvalidParameterException, DataNotExistException, IOException {
        final MerchandiseDo merchandiseDo = this.merchandiseService.setMerchandiseBannerImage(data, merchandiseUuid);
        if (merchandiseDo == null) {
            return ResponseBuilder.makeNotFoundResponse();
        }
        merchandiseDo.setMerchandiseImage(null);
        return ResponseBuilder.makeOkResponse(merchandiseDo);
    }

    @PostMapping("/image/add")
    @PreAuthorize("@authService.isPermission(" + AdminPermission.ADMIN + ")")
    public ResponseEntity<ResponseResult<MerchandiseImageDo>> addMerchandiseImage(
            @RequestParam(value = "file") final MultipartFile data,
            @RequestParam final String merchandiseUuid) throws InvalidParameterException, DataNotExistException, IOException {
        final MerchandiseImageDo merchandiseImageDo = this.merchandiseService.addMerchandiseImage(data, merchandiseUuid);
        merchandiseImageDo.setMerchandiseImage(null);
        return ResponseBuilder.makeOkResponse(merchandiseImageDo);
    }

    private MerchandiseImageRepository merchandiseImageRepository;

    @Autowired
    private void setMerchandiseImageRepository(final MerchandiseImageRepository merchandiseImageRepository) {
        this.merchandiseImageRepository = merchandiseImageRepository;
    }

    @DeleteMapping("/image/delete")
    @PreAuthorize("@authService.isPermission(" + AdminPermission.ADMIN + ")")
    public ResponseEntity<ResponseResult<Object>> deleteMerchandiseImage(
            @RequestParam final String merchandiseImageUuid) throws DataNotExistException {
        final Optional<MerchandiseOptionalDo> merchandiseOptionalDoOptional = this.merchandiseOptionalRepository.findById(merchandiseImageUuid);
        if (merchandiseOptionalDoOptional.isEmpty()) {
            throw new DataNotExistException("Image not exist");
        }
        this.merchandiseOptionalRepository.delete(merchandiseOptionalDoOptional.get());
        return ResponseBuilder.makeOkResponse();
    }

    @GetMapping("/public/merchandise/image/list")
    public ResponseEntity<ResponseResult<List<MerchandiseImageDo>>> getMerchandiseImageList(
            @RequestParam final String merchandiseUuid) throws DataNotExistException {
        return ResponseBuilder.makeOkResponse(this.merchandiseService.getMerchandiseImageListByUuid(merchandiseUuid));
    }

    @GetMapping("/public/merchandise/image/get")
    public byte[] getMerchandiseImage(@RequestParam final String merchandiseImageUuid) throws DataNotExistException {
        return this.merchandiseService.responseMerchandiseImage(merchandiseImageUuid);
    }

    private MerchandiseOptionalGroupRepository merchandiseOptionalGroupRepository;

    @Autowired
    private void setMerchandiseOptionalGroupRepository(final MerchandiseOptionalGroupRepository merchandiseOptionalGroupRepository) {
        this.merchandiseOptionalGroupRepository = merchandiseOptionalGroupRepository;
    }

    private MerchandiseOptionalRepository merchandiseOptionalRepository;

    @Autowired
    private void setMerchandiseOptionalRepository(final MerchandiseOptionalRepository merchandiseOptionalRepository) {
        this.merchandiseOptionalRepository = merchandiseOptionalRepository;
    }

    @PostMapping("/optional/group/add")
    @PreAuthorize("@authService.isPermission(" + AdminPermission.ADMIN + ")")
    public ResponseEntity<ResponseResult<MerchandiseOptionalGroupDo>> addOptionalGroup(
            @RequestParam final String merchandiseUuid,
            @RequestParam final String groupName) throws DataNotExistException, InvalidParameterException {
        if (groupName == null || groupName.replace(" ", "").equals("") ||
                merchandiseUuid == null || merchandiseUuid.replace(" ", "").equals("")) {
            throw new InvalidParameterException("Null group name.");
        }
        final MerchandiseInfoData merchandiseInfoData = this.merchandiseService.getMerchandiseByUuid(merchandiseUuid);
        final MerchandiseOptionalGroupDo merchandiseOptionalGroupDo = new MerchandiseOptionalGroupDo();
        if (merchandiseInfoData == null) {
            throw new DataNotExistException("Merchandise not exist");
        }
        merchandiseOptionalGroupDo.setMerchandiseUuid(merchandiseInfoData.getMerchandiseDo().getMerchandiseUuid());
        merchandiseOptionalGroupDo.setMerchandiseOptionalGroupName(groupName);
        this.merchandiseOptionalGroupRepository.save(merchandiseOptionalGroupDo);
        return ResponseBuilder.makeOkResponse(merchandiseOptionalGroupDo);
    }

    @DeleteMapping("/optional/group/delete")
    @PreAuthorize("@authService.isPermission(" + AdminPermission.ADMIN + ")")
    public ResponseEntity<ResponseResult<Object>> deleteOptionalGroup(@RequestParam final String groupUuid) throws DataNotExistException {
        final Optional<MerchandiseOptionalGroupDo> merchandiseOptionalGroupDoOptional = this.merchandiseOptionalGroupRepository.findById(groupUuid);
        if (merchandiseOptionalGroupDoOptional.isEmpty()) {
            throw new DataNotExistException("Merchandise group not exist");
        }
        this.merchandiseOptionalGroupRepository.delete(merchandiseOptionalGroupDoOptional.get());
        this.merchandiseOptionalRepository.deleteByMerchandiseOptionalGroupUuid(groupUuid);
        return ResponseBuilder.makeOkResponse();
    }

    @PutMapping("/optional/group/alter")
    @PreAuthorize("@authService.isPermission(" + AdminPermission.ADMIN + ")")
    public ResponseEntity<ResponseResult<MerchandiseOptionalGroupDo>> updateOptionalGroup(
            @RequestParam final String groupUuid,
            @RequestParam final String groupName) throws NullParameterException, DataNotExistException {
        if (groupName == null || groupName.replace(" ", "").equals("")) {
            throw new NullParameterException("Null group name.");
        }
        final Optional<MerchandiseOptionalGroupDo> merchandiseOptionalGroupDoOptional = this.merchandiseOptionalGroupRepository.findById(groupUuid);
        if (merchandiseOptionalGroupDoOptional.isEmpty()) {
            throw new DataNotExistException("Merchandise group not exist");
        }
        final MerchandiseOptionalGroupDo merchandiseOptionalGroupDo = merchandiseOptionalGroupDoOptional.get();
        merchandiseOptionalGroupDo.setMerchandiseOptionalGroupName(groupName);
        this.merchandiseOptionalGroupRepository.save(merchandiseOptionalGroupDo);
        return ResponseBuilder.makeOkResponse(merchandiseOptionalGroupDo);
    }

    @PostMapping("/optional/add")
    @PreAuthorize("@authService.isPermission(" + AdminPermission.ADMIN + ")")
    public ResponseEntity<ResponseResult<MerchandiseOptionalDo>> addOptional(
            @RequestParam final String groupUuid,
            @RequestParam final String optionalName) throws InvalidParameterException, DataNotExistException {
        if (optionalName == null || optionalName.replace(" ", "").equals("")) {
            throw new InvalidParameterException("Null group name.");
        }
        final Optional<MerchandiseOptionalGroupDo> merchandiseOptionalGroupDo = this.merchandiseOptionalGroupRepository.findById(groupUuid);
        if (merchandiseOptionalGroupDo.isEmpty()) {
            throw new DataNotExistException("Group not exist");
        }
        final MerchandiseOptionalDo merchandiseOptionalDo = new MerchandiseOptionalDo();
        merchandiseOptionalDo.setMerchandiseOptionalGroupUuid(merchandiseOptionalGroupDo.get().getMerchandiseOptionalGroupUuid());
        merchandiseOptionalDo.setMerchandiseOptionalName(optionalName);
        this.merchandiseOptionalRepository.save(merchandiseOptionalDo);
        return ResponseBuilder.makeOkResponse(merchandiseOptionalDo);
    }

    @DeleteMapping("/optional/delete")
    @PreAuthorize("@authService.isPermission(" + AdminPermission.ADMIN + ")")
    public ResponseEntity<ResponseResult<Object>> deleteOptional(@RequestParam final String optionalUuid) throws DataNotExistException {
        final Optional<MerchandiseOptionalDo> merchandiseOptionalDoOptional = this.merchandiseOptionalRepository.findById(optionalUuid);
        if (merchandiseOptionalDoOptional.isEmpty()) {
            throw new DataNotExistException();
        }
        this.merchandiseOptionalRepository.delete(merchandiseOptionalDoOptional.get());
        return ResponseBuilder.makeOkResponse();
    }

    @PutMapping("/optional/alter")
    @PreAuthorize("@authService.isPermission(" + AdminPermission.ADMIN + ")")
    public ResponseEntity<ResponseResult<MerchandiseOptionalDo>> updateOptional(
            @RequestParam final String optionalUuid,
            @RequestParam final String optionalName) throws DataNotExistException, NullParameterException {
        final Optional<MerchandiseOptionalDo> merchandiseOptionalDoOptional = this.merchandiseOptionalRepository.findById(optionalUuid);
        if (merchandiseOptionalDoOptional.isEmpty()) {
            throw new DataNotExistException();
        }
        final MerchandiseOptionalDo merchandiseOptionalDo = merchandiseOptionalDoOptional.get();
        if (optionalName == null || optionalName.replace(" ", "").equals("")) {
            throw new NullParameterException("Null optional name");
        }
        merchandiseOptionalDo.setMerchandiseOptionalName(optionalName);
        this.merchandiseOptionalRepository.save(merchandiseOptionalDo);
        return ResponseBuilder.makeOkResponse(merchandiseOptionalDo);
    }

    private MerchandiseOptionalGroupInfo getOptionalGroupInfo(final String groupUuid) {
        final MerchandiseOptionalGroupInfo merchandiseOptionalGroupInfo = new MerchandiseOptionalGroupInfo();
        merchandiseOptionalGroupInfo.setMerchandiseOptionalGroupDo(this.merchandiseOptionalGroupRepository.findById(groupUuid).get());
        merchandiseOptionalGroupInfo.setMerchandiseOptionalDoList(this.merchandiseOptionalRepository.findByMerchandiseOptionalGroupUuid(groupUuid));
        return merchandiseOptionalGroupInfo;
    }

    @GetMapping("/public/merchandise/optional/group/get")
    public ResponseEntity<ResponseResult<MerchandiseOptionalGroupInfo>> getOptionalGroup(
            @RequestParam final String groupUuid) throws DataNotExistException {
        final MerchandiseOptionalGroupInfo merchandiseOptionalGroupInfo = new MerchandiseOptionalGroupInfo();
        if (this.merchandiseOptionalGroupRepository.findById(groupUuid).isEmpty()) {
            throw new DataNotExistException("Group not exist");
        }
        merchandiseOptionalGroupInfo.setMerchandiseOptionalGroupDo(this.merchandiseOptionalGroupRepository.findById(groupUuid).get());
        merchandiseOptionalGroupInfo.setMerchandiseOptionalDoList(this.merchandiseOptionalRepository.findByMerchandiseOptionalGroupUuid(groupUuid));
        return ResponseBuilder.makeOkResponse(merchandiseOptionalGroupInfo);
    }

    private MerchandiseTagRepository merchandiseTagRepository;

    @Autowired
    private void setMerchandiseTagRepository(final MerchandiseTagRepository merchandiseTagRepository) {
        this.merchandiseTagRepository = merchandiseTagRepository;
    }

    @PostMapping("/tag/add")
    @PreAuthorize("@authService.isPermission(" + AdminPermission.ADMIN + ")")
    public ResponseEntity<ResponseResult<MerchandiseTagDo>> addTag(@RequestParam final String tagName) throws NullParameterException {
        if (tagName == null || tagName.replace(" ", "").equals("")) {
            throw new NullParameterException("Not allow null tag name.");
        }
        final MerchandiseTagDo merchandiseTagDo = new MerchandiseTagDo();
        merchandiseTagDo.setMerchandiseTagName(tagName);
        this.merchandiseTagRepository.save(merchandiseTagDo);
        return ResponseBuilder.makeOkResponse(merchandiseTagDo);
    }

    @PutMapping("/tag/alter")
    @PreAuthorize("@authService.isPermission(" + AdminPermission.ADMIN + ")")
    public ResponseEntity<ResponseResult<MerchandiseTagDo>> addTag(
            @RequestParam final String tagUuid,
            @RequestParam final String tagName) throws NullParameterException {
        if (tagName == null || tagName.replace(" ", "").equals("")) {
            throw new NullParameterException("Not allow null tag name.");
        }
        if (this.merchandiseTagRepository.findById(tagUuid).isEmpty()) {
            return ResponseBuilder.makeNotFoundResponse();
        }
        final MerchandiseTagDo merchandiseTagDo = this.merchandiseTagRepository.findById(tagUuid).get();
        merchandiseTagDo.setMerchandiseTagName(tagName);
        this.merchandiseTagRepository.save(merchandiseTagDo);
        return ResponseBuilder.makeOkResponse(merchandiseTagDo);
    }

    @DeleteMapping("/tag/delete")
    @PreAuthorize("@authService.isPermission(" + AdminPermission.ADMIN + ")")
    public ResponseEntity<ResponseResult<Object>> deleteTag(@RequestParam final String tagUuid) {
        this.merchandiseTagRepository.deleteById(tagUuid);
        return ResponseBuilder.makeOkResponse();
    }

    @GetMapping("/public/merchandise/optional/tag/get")
    public ResponseEntity<ResponseResult<Object>> getMerchandiseTag(final String merchandiseTagUuid) {
        if (merchandiseTagUuid == null || merchandiseTagUuid.replace(" ", "").equals("")) {
            return ResponseBuilder.makeOkResponse(this.merchandiseTagRepository.findAll());
        }
        return ResponseBuilder.makeOkResponse(this.merchandiseTagRepository.findById(merchandiseTagUuid).get());
    }

    // todo: 添加商家（后台）发货系统
    // todo: 添加consumer端的购物车，订单系统，下单系统

}
