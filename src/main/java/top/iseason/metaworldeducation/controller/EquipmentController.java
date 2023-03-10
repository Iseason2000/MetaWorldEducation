package top.iseason.metaworldeducation.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;
import top.iseason.metaworldeducation.entity.ActivityEquipment;
import top.iseason.metaworldeducation.entity.EquipmentInfo;
import top.iseason.metaworldeducation.entity.PlayerInfo;
import top.iseason.metaworldeducation.mapper.ActivityEquipmentMapper;
import top.iseason.metaworldeducation.mapper.EquipmentInfoMapper;
import top.iseason.metaworldeducation.mapper.PlayerMapper;
import top.iseason.metaworldeducation.util.FileUtil;
import top.iseason.metaworldeducation.util.Result;
import top.iseason.metaworldeducation.util.ResultCode;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Api(tags = "实验室API")
@RestController
@RequestMapping("/equipment")
public class EquipmentController {
    protected static final String EQUIPMENT_DIR = System.getProperty("user.dir") + File.separatorChar + "equipments";

    @Resource
    PlayerMapper playerMapper;

    @Resource
    ActivityEquipmentMapper activityEquipmentMapper;

    @Resource
    EquipmentInfoMapper equipmentInfoMapper;

    @ApiOperation(value = "上传并创建器材", notes = "'multipart/form-data' 协议")
    @Transactional
    @PostMapping(value = "/", produces = "application/json")
    public Result<EquipmentInfo> uploadEquipment(
            @ApiParam(value = "缩略图", required = false) @RequestPart(required = false) MultipartFile thumbnailFile,
            @ApiParam(value = "AssetBundle", required = false) @RequestPart(required = false) MultipartFile assetFile,
            @ApiParam(value = "源文件", required = false) @RequestPart(required = false) MultipartFile sourceFile,
            @ApiParam(value = "器材名", required = true) @RequestParam String name,
            @ApiParam(value = "器材预制件名称", required = true) @RequestParam String perfabName
    ) throws IOException {
        EquipmentInfo labEquipment = new EquipmentInfo();
        labEquipment.setName(name);
        labEquipment.setPerfabName(perfabName);
        labEquipment.setCreateTime(new Date());
        equipmentInfoMapper.insert(labEquipment);
        Integer eId = labEquipment.getEId();
        if (thumbnailFile != null) labEquipment.setThumbnailName(FileUtil.concatName(eId.toString(), thumbnailFile));
        if (assetFile != null) labEquipment.setAssetName(FileUtil.concatName(eId.toString(), assetFile));
        if (sourceFile != null) labEquipment.setSourceName(FileUtil.concatName(eId.toString(), sourceFile));
        equipmentInfoMapper.updateById(labEquipment);
        FileUtil.uploadFileTo(thumbnailFile, "thumbnail", eId);
        FileUtil.uploadFileTo(assetFile, "asset", eId);
        FileUtil.uploadFileTo(sourceFile, "source", eId);
        return Result.success(labEquipment);
    }

    @ApiOperation(value = "下载器材缩略图")
    @GetMapping(value = "/download/thumbnail/{id}", produces = "application/octet-stream")
    public Object downloadThumbnail(
            @ApiParam(value = "器材ID", required = true) @PathVariable Integer id
    ) throws Exception {
        EquipmentInfo labEquipment = equipmentInfoMapper.selectById(id);
        if (labEquipment == null) throw new IllegalArgumentException("器材不存在!");
        File thumbnail =
                new File(EQUIPMENT_DIR + File.separatorChar + "thumbnail" + File.separatorChar + labEquipment.getThumbnailName());
        if (!thumbnail.exists()) throw new IllegalArgumentException("器材不存在!");
        InputStreamResource isr = new InputStreamResource(Files.newInputStream(thumbnail.toPath()));
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-disposition", "attachment; filename=" + URLEncoder.encode(thumbnail.getName(), "UTF-8"))
                .body(isr);
    }

    @ApiOperation(value = "下载器材AssetBundle")
    @GetMapping(value = "/download/asset/{id}", produces = "application/octet-stream")
    public Object downloadAsset(
            @ApiParam(value = "器材ID", required = true) @PathVariable Integer id
    ) throws Exception {
        EquipmentInfo labEquipment = equipmentInfoMapper.selectById(id);
        if (labEquipment == null) throw new IllegalArgumentException("器材不存在!");
        File asset =
                new File(EQUIPMENT_DIR + File.separatorChar + "asset" + File.separatorChar + labEquipment.getAssetName());
        if (!asset.exists()) throw new IllegalArgumentException("器材不存在!");
        InputStreamResource isr = new InputStreamResource(Files.newInputStream(asset.toPath()));
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-disposition", "attachment; filename=" + URLEncoder.encode(asset.getName(), "UTF-8"))
                .body(isr);
    }

    @ApiOperation(value = "下载器材源文件")
    @GetMapping(value = "/download/source/{id}", produces = "application/octet-stream")
    public Object downloadEquipment(
            @ApiParam(value = "器材ID", required = true) @PathVariable Integer id
    ) throws Exception {
        EquipmentInfo labEquipment = equipmentInfoMapper.selectById(id);
        if (labEquipment == null) throw new IllegalArgumentException("器材不存在!");
        File source =
                new File(EQUIPMENT_DIR + File.separatorChar + "source" + File.separatorChar + labEquipment.getSourceName());
        if (!source.exists()) throw new IllegalArgumentException("器材不存在!");
        InputStreamResource isr = new InputStreamResource(Files.newInputStream(source.toPath()));
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-disposition", "attachment; filename=" + URLEncoder.encode(source.getName(), "UTF-8"))
                .body(isr);
    }

    @ApiOperation(value = "删除器材")
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public Result<Object> delEquipment(
            @ApiParam(value = "器材ID", required = true) @PathVariable Integer id
    ) {
        EquipmentInfo equipmentInfo = equipmentInfoMapper.selectById(id);
        if (equipmentInfo == null) {
            return Result.of(999, "器材不存在");
        }
        equipmentInfoMapper.deleteById(id);
        String thumbnailName = equipmentInfo.getThumbnailName();
        if (thumbnailName != null) new File(
                EQUIPMENT_DIR + File.separatorChar +
                        "thumbnail" + File.separatorChar + thumbnailName).deleteOnExit();
        String assetName = equipmentInfo.getAssetName();
        if (assetName != null) new File(
                EQUIPMENT_DIR + File.separatorChar +
                        "asset" + File.separatorChar + assetName
        ).deleteOnExit();
        String sourceName = equipmentInfo.getSourceName();
        if (sourceName != null) new File(
                EQUIPMENT_DIR + File.separatorChar +
                        "source" + File.separatorChar + sourceName
        ).deleteOnExit();
        return Result.success();
    }


    @ApiOperation("获取所有器材")
    @GetMapping(value = "/all", produces = "application/json")
    public Result<List<EquipmentInfo>> getEquipments(
            @ApiParam("第几页,0开始") @RequestParam(required = false) Integer page,
            @ApiParam("每页的数量，默认10") @RequestParam(required = false) Integer count
    ) {
        if (page == null) page = 0;
        if (count == null) count = 10;
        return Result.success(
                equipmentInfoMapper.selectList(
                        new QueryWrapper<EquipmentInfo>()
                                .last("limit " + page * count + "," + count)
                )
        );

    }


    @ApiOperation("将某一种器材生成到场景中")
    @PostMapping(value = "/add", produces = "application/json")
    public Result<ActivityEquipment> addEquipmentTo(
            @ApiIgnore Authentication authentication,
            @ApiParam(value = "活动/房间ID", required = true) @RequestParam Integer activityId,
            @ApiParam(value = "桌子ID", required = true) @RequestParam Integer deskId,
            @ApiParam(value = "器材ID", required = true) @RequestParam Integer eId,
            @ApiParam(value = "器材子ID", required = true) @RequestParam Integer subId,
//            @ApiParam(value = "创建者ID", required = true) @RequestParam Integer creatorId,
            @ApiParam(value = "X坐标", required = true) @RequestParam Float posX,
            @ApiParam(value = "Y坐标", required = true) @RequestParam Float posY,
            @ApiParam(value = "Z坐标", required = true) @RequestParam Float posZ,
            @ApiParam(value = "X角", required = true) @RequestParam Float roaX,
            @ApiParam(value = "Y角", required = true) @RequestParam Float roaY,
            @ApiParam(value = "Z角", required = true) @RequestParam Float roaZ,
            @ApiParam(value = "X缩放", required = true) @RequestParam Float scaleX,
            @ApiParam(value = "Y缩放", required = true) @RequestParam Float scaleY,
            @ApiParam(value = "Z缩放", required = true) @RequestParam Float scaleZ,
            @ApiParam(value = "是否隐藏 0不隐藏 1隐藏") @RequestParam(required = false) Integer hide
    ) {
        PlayerInfo playerInfo = playerMapper.selectOne(new LambdaQueryWrapper<PlayerInfo>().eq(PlayerInfo::getUsrName, authentication.getName()));
        if (playerInfo == null) return Result.of(ResultCode.USER_NOT_LOGIN);

        ActivityEquipment activityEquipment = new ActivityEquipment()
                .setActivityId(activityId)
                .setDeskId(deskId)
                .setEId(eId)
                .setSubId(subId)
                .setCreatorId(playerInfo.getPlayerId())
                .setPosX(posX)
                .setPosY(posY)
                .setPosZ(posZ)
                .setRoaX(roaX)
                .setRoaY(roaY)
                .setRoaZ(roaZ)
                .setScaleX(scaleX)
                .setScaleY(scaleY)
                .setScaleZ(scaleZ);
        if (hide != null) activityEquipment.setHide(hide);
        activityEquipmentMapper.insert(activityEquipment);
        return Result.success(activityEquipment);
    }

    @ApiOperation("更新活动器材信息")
    @PostMapping(value = "/update/{reId}", produces = "application/json")
    public Result<ActivityEquipment> updateEquipment(
            @ApiIgnore Authentication authentication,
            @ApiParam(value = "活动器材ID", required = true) @PathVariable Integer reId,
            @ApiParam(value = "是否正在操作器材", required = true) @RequestParam Boolean isDealing,
            @ApiParam("X坐标") @RequestParam(required = false) Float posX,
            @ApiParam("Y坐标") @RequestParam(required = false) Float posY,
            @ApiParam("Z坐标") @RequestParam(required = false) Float posZ,
            @ApiParam("X角") @RequestParam(required = false) Float roaX,
            @ApiParam("Y角") @RequestParam(required = false) Float roaY,
            @ApiParam("Z角") @RequestParam(required = false) Float roaZ,
            @ApiParam("X缩放") @RequestParam(required = false) Float scaleX,
            @ApiParam("Y缩放") @RequestParam(required = false) Float scaleY,
            @ApiParam("Z缩放") @RequestParam(required = false) Float scaleZ,
            @ApiParam("是否隐藏 0不隐藏 1隐藏") @RequestParam(required = false) Integer hide
    ) {
        PlayerInfo playerInfo = playerMapper.selectOne(new LambdaQueryWrapper<PlayerInfo>().eq(PlayerInfo::getUsrName, authentication.getName()));
        if (playerInfo == null) return Result.of(ResultCode.USER_NOT_LOGIN);
        ActivityEquipment activityEquipment = activityEquipmentMapper.selectById(reId);
        if (activityEquipment == null) return Result.of(999, "活动器材不存在!");
        if (isDealing) activityEquipment.setDealingPlayer(playerInfo.getPlayerId());
        if (posX != null) activityEquipment.setPosX(posX);
        if (posY != null) activityEquipment.setPosY(posY);
        if (posZ != null) activityEquipment.setPosZ(posZ);
        if (roaX != null) activityEquipment.setRoaX(roaX);
        if (roaY != null) activityEquipment.setRoaY(roaY);
        if (roaZ != null) activityEquipment.setRoaZ(roaZ);
        if (scaleX != null) activityEquipment.setScaleX(scaleX);
        if (scaleY != null) activityEquipment.setScaleY(scaleY);
        if (scaleZ != null) activityEquipment.setScaleZ(scaleZ);
        if (hide != null) activityEquipment.setHide(hide);
        activityEquipmentMapper.updateById(activityEquipment);
        return Result.success(activityEquipment);
    }

    @ApiOperation("删除活动器材")
    @DeleteMapping(value = "/{reId}", produces = "application/json")
    public Result<Object> updateEquipment(
            @ApiIgnore Authentication authentication,
            @ApiParam(value = "活动器材ID", required = true) @PathVariable Long reId) {
        PlayerInfo playerInfo = playerMapper.selectOne(new LambdaQueryWrapper<PlayerInfo>().eq(PlayerInfo::getUsrName, authentication.getName()));
        if (playerInfo == null) return Result.of(ResultCode.USER_NOT_LOGIN);
        LambdaQueryWrapper<ActivityEquipment> wrapper = new LambdaQueryWrapper<ActivityEquipment>()
                .eq(ActivityEquipment::getReId, reId);
        if (Objects.equals(playerInfo.getRole(), "PLAYER"))
            wrapper = wrapper.eq(ActivityEquipment::getCreatorId, playerInfo.getPlayerId());
        int delete = activityEquipmentMapper.delete(wrapper.last("limit 1"));
        if (delete != 1) return Result.of(999, "器材不存在或操作者非创造者");
        return Result.success();
    }

    @ApiOperation("由条件获取活动器材信息,多个条件取交集")
    @GetMapping(value = "", produces = "application/json")
    public Result<List<ActivityEquipment>> getActivityEquipments(
            @ApiParam("活动/房间ID") @RequestParam(required = false) Integer activityId,
            @ApiParam("桌子ID") @RequestParam(required = false) Integer deskId,
            @ApiParam("器材ID") @RequestParam(required = false) Integer eId,
            @ApiParam("器材子ID") @RequestParam(required = false) Integer subId,
            @ApiParam("创建者ID") @RequestParam(required = false) Integer creatorId
    ) {
        LambdaQueryWrapper<ActivityEquipment> wrapper = new LambdaQueryWrapper<>();
        if (activityId != null) wrapper = wrapper.eq(ActivityEquipment::getActivityId, activityId);
        if (deskId != null) wrapper = wrapper.eq(ActivityEquipment::getDeskId, deskId);
        if (eId != null) wrapper = wrapper.eq(ActivityEquipment::getEId, eId);
        if (subId != null) wrapper = wrapper.eq(ActivityEquipment::getSubId, subId);
        if (creatorId != null) wrapper = wrapper.eq(ActivityEquipment::getCreatorId, creatorId);
        return Result.success(activityEquipmentMapper.selectList(wrapper));
    }
}
