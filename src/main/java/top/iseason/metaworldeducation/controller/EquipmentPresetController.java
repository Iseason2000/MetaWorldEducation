package top.iseason.metaworldeducation.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.iseason.metaworldeducation.entity.EquipmentPreset;
import top.iseason.metaworldeducation.mapper.EquipmentPresetMapper;
import top.iseason.metaworldeducation.util.FileUtil;
import top.iseason.metaworldeducation.util.Result;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;

import static top.iseason.metaworldeducation.controller.EquipmentController.EQUIPMENT_DIR;

@Slf4j
@Api(tags = "实验室预设API")
@RestController
@RequestMapping("/equipment/preset")
public class EquipmentPresetController {

    @Resource
    EquipmentPresetMapper equipmentPresetMapper;

    @ApiOperation(value = "上传并创建实验预设", notes = "'multipart/form-data' 协议")
    @Transactional
    @PostMapping(value = "/", produces = "application/json")
    public Result<EquipmentPreset> uploadPreset(
            @ApiParam(value = "实验预设名", required = true) @RequestParam String name,
            @ApiParam(value = "实验步骤", required = true) @RequestParam String steps,
            @ApiParam(value = "实验原理", required = true) @RequestParam String theory,
            @ApiParam(value = "实验预设器材", required = true) @RequestParam List<String> equipments,
            @ApiParam(value = "实验预设文件") @RequestPart(required = false) MultipartFile file
    ) throws IOException {
        EquipmentPreset equipmentPreset = new EquipmentPreset();
        equipmentPreset.setName(name);
        equipmentPreset.setSteps(steps);
        equipmentPreset.setTheory(theory);
        equipmentPreset.setCreateTime(new Date());
        equipmentPreset.setEquipmentsField(String.join(",", equipments));
        equipmentPresetMapper.insert(equipmentPreset);
        if (file != null) {
            equipmentPreset.setFileName(FileUtil.concatName(equipmentPreset.getEpId().toString(), file));
            equipmentPresetMapper.updateById(equipmentPreset);
        }
        FileUtil.uploadFileTo(file, "preset", equipmentPreset.getEpId());
        return Result.success(equipmentPreset);
    }

    @ApiOperation(value = "获取实验预设")
    @Transactional
    @GetMapping(value = "/", produces = "application/json")
    public Result<List<EquipmentPreset>> getPreset(
            @ApiParam("第几页,0开始") @RequestParam(required = false) Integer page,
            @ApiParam("每页的数量，默认10") @RequestParam(required = false) Integer count
    ) {
        if (page == null) page = 0;
        if (count == null) count = 10;
        return Result.success(
                equipmentPresetMapper.selectList(
                        new QueryWrapper<EquipmentPreset>()
                                .last("limit " + page * count + "," + count)
                )
        );
    }

    @ApiOperation(value = "删除实验预设")
    @Transactional
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public Result<Object> delPreset(
            @ApiParam(value = "实验预设ID", required = true) @PathVariable Integer id
    ) {
        EquipmentPreset equipmentPreset = equipmentPresetMapper.selectById(id);
        if (equipmentPreset == null) return Result.of(999, "实验预设不存在");
        equipmentPresetMapper.deleteById(id);
        String fileName = equipmentPreset.getFileName();
        if (fileName != null) {
            new File(EQUIPMENT_DIR + File.separatorChar +
                    "preset" + File.separatorChar + fileName).deleteOnExit();
        }
        return Result.success();
    }

    @ApiOperation(value = "下载实验预设文件")
    @GetMapping(value = "/download/{id}", produces = "application/octet-stream")
    public Object downloadPreset(
            @ApiParam(value = "实验预设ID", required = true) @PathVariable Integer id
    ) throws Exception {
        EquipmentPreset equipmentPreset = equipmentPresetMapper.selectById(id);
        if (equipmentPreset == null) throw new IllegalArgumentException("实验预设不存在!");
        String fileName = equipmentPreset.getFileName();
        if (fileName == null) throw new IOException("文件预设不存在");
        File preset = new File(EQUIPMENT_DIR + File.separatorChar +
                "preset" + File.separatorChar + fileName);
        if (!preset.exists()) throw new IOException("文件预设不存在");
        InputStreamResource isr = new InputStreamResource(Files.newInputStream(preset.toPath()));
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-disposition", "attachment; filename=" + URLEncoder.encode(preset.getName(), "UTF-8"))
                .body(isr);
    }
}
