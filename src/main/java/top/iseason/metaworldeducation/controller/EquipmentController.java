package top.iseason.metaworldeducation.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.iseason.metaworldeducation.entity.EquipmentInfo;
import top.iseason.metaworldeducation.mapper.ActivityEquipmentMapper;
import top.iseason.metaworldeducation.mapper.EquipmentInfoMapper;
import top.iseason.metaworldeducation.mapper.PlayerMapper;
import top.iseason.metaworldeducation.util.Result;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Api(tags = "实验室API，需登录")
@RestController
@RequestMapping("/equipment")
public class EquipmentController {

    private static final String EQUIPMENT_DIR = System.getProperty("user.dir") + File.separatorChar + "equipments";

    @Resource
    PlayerMapper playerMapper;
    @Resource
    ActivityEquipmentMapper activityEquipmentMapper;
    @Resource
    EquipmentInfoMapper labEquipmentMapper;

    @ApiOperation(value = "上传并创建器材", notes = "需要 ADMIN 权限 'multipart/form-data' 协议")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @PostMapping(value = "/", produces = "application/json")
    public Result<EquipmentInfo> uploadEquipment(
            @ApiParam(value = "缩略图", required = true) @RequestPart MultipartFile thumbnailFile,
            @ApiParam(value = "AssetBundle", required = true) @RequestPart MultipartFile assetFile,
            @ApiParam(value = "源文件", required = true) @RequestPart MultipartFile sourceFile,
            @ApiParam(value = "器材名", required = true) @RequestParam String name,
            @ApiParam(value = "器材预制件名称", required = true) @RequestParam String perfabName
    ) throws IOException {
        EquipmentInfo labEquipment = new EquipmentInfo();
        labEquipment.setName(name);
        labEquipment.setPerfabName(perfabName);
        labEquipment.setCreateTime(new Date());
        labEquipmentMapper.insert(labEquipment);
        Integer eId = labEquipment.getEId();
        uploadFileTo(thumbnailFile, "thumbnail", eId);
        uploadFileTo(assetFile, "asset", eId);
        uploadFileTo(sourceFile, "source", eId);
        return Result.success(labEquipment);
    }

    private void uploadFileTo(MultipartFile sourceFile, String folder, Integer id) throws IOException {
        String fileName = sourceFile.getOriginalFilename();
        if (fileName == null) throw new IOException("文件名为空");
        File storeFile = new File(EQUIPMENT_DIR + File.separatorChar +
                folder + File.separatorChar +
                id + fileName.substring(fileName.lastIndexOf(".")));
        storeFile.getParentFile().mkdirs();
        sourceFile.transferTo(storeFile);
    }

    @ApiOperation(value = "下载器材缩略图")
    @GetMapping(value = "/download/thumbnail/{id}", produces = "application/octet-stream")
    public Object downloadThumbnail(
            @ApiParam(value = "器材ID", required = true) @PathVariable Integer id
    ) throws Exception {
        EquipmentInfo labEquipment = labEquipmentMapper.selectById(id);
        if (labEquipment == null) throw new IllegalArgumentException("器材不存在!");
        File thumbnail = findFile("thumbnail", labEquipment.getEId());
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
        EquipmentInfo labEquipment = labEquipmentMapper.selectById(id);
        if (labEquipment == null) throw new IllegalArgumentException("器材不存在!");
        File thumbnail = findFile("asset", labEquipment.getEId());
        InputStreamResource isr = new InputStreamResource(Files.newInputStream(thumbnail.toPath()));
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-disposition", "attachment; filename=" + URLEncoder.encode(thumbnail.getName(), "UTF-8"))
                .body(isr);
    }

    @ApiOperation(value = "下载器材源文件")
    @GetMapping(value = "/download/source/{id}", produces = "application/octet-stream")
    public Object downloadEquipment(
            @ApiParam(value = "器材ID", required = true) @PathVariable Integer id
    ) throws Exception {
        EquipmentInfo labEquipment = labEquipmentMapper.selectById(id);
        if (labEquipment == null) throw new IllegalArgumentException("器材不存在!");
        File thumbnail = findFile("source", labEquipment.getEId());
        InputStreamResource isr = new InputStreamResource(Files.newInputStream(thumbnail.toPath()));
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-disposition", "attachment; filename=" + URLEncoder.encode(thumbnail.getName(), "UTF-8"))
                .body(isr);
    }

    @ApiOperation(value = "删除器材")
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public Result<Object> delEquipment(
            @ApiParam(value = "器材ID", required = true) @PathVariable Integer id
    ) throws Exception {
        if (labEquipmentMapper.deleteById(id) == 0) {
            return Result.of(999, "器材不存在");
        }
        findFile("thumbnail", id).deleteOnExit();
        findFile("asset", id).deleteOnExit();
        findFile("source", id).deleteOnExit();
        return Result.success();
    }

    private File findFile(String folder, Integer id) throws IOException {
        String path = EQUIPMENT_DIR + File.separatorChar + folder;
        AtomicReference<File> file = new AtomicReference<>(null);
        File[] files = new File(path).listFiles();
        if (files == null) throw new IOException("文件不存在");
        //并行查找
        Arrays.stream(files)
                .parallel()
                .forEach(it -> {
                            if (file.get() != null) return;
                            String name = it.getName();
                            int i = name.lastIndexOf('.');
                            if (i < 0) return;
                            name = name.substring(0, i);
                            if (name.equals(id.toString())) {
                                file.compareAndSet(null, it);
                            }
                        }
                );
        if (file.get() != null || files.length != 1) throw new IOException("文件不存在!");
        return file.get();
    }

    @ApiOperation("获取所有器材")
    @GetMapping(value = "/equipment", produces = "application/json")
    public Result<List<EquipmentInfo>> getEquipments() {
        return Result.success(labEquipmentMapper.selectList(null));
    }

}
