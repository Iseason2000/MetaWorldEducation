package top.iseason.metaworldeducation.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;
import top.iseason.metaworldeducation.entity.*;
import top.iseason.metaworldeducation.mapper.*;
import top.iseason.metaworldeducation.util.Result;
import top.iseason.metaworldeducation.util.ResultCode;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "实验室API，需登录")
@RestController
@RequestMapping("/lab")
public class LabController {
    private static final String USER_DIR = System.getProperty("user.dir");
    @Resource
    PlayerMapper playerMapper;
    @Resource
    LabMapper labMapper;
    @Resource
    LabSubjectMapper labSubjectMapper;
    @Resource
    LabEquipmentMapper labEquipmentMapper;
    @Resource
    LabPlayerMapper labPlayerMapper;

    @ApiOperation("获取所有学科")
    @GetMapping(value = "/subject", produces = "application/json")
    public Result<List<LabSubject>> getLabSubjects() {
        return Result.success(labSubjectMapper.selectList(null));
    }

    @ApiOperation("获取某个学科")
    @GetMapping(value = "/subject/{id}", produces = "application/json")
    public Result<LabSubject> getLabSubject(@ApiParam(value = "学科ID", required = true) @PathVariable String id) {
        LabSubject labSubject = labSubjectMapper.selectById(id);
        if (labSubject == null) return Result.of(999, "id不存在!");
        return Result.success(labSubject);
    }

    @ApiOperation(value = "添加一个学科", notes = "需要 ADMIN 权限")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/subject", produces = "application/json")
    public Result<LabSubject> addLabSubject(@ApiParam(value = "学科名称", required = true) @RequestParam String name) {
        LabSubject labSubject = new LabSubject();
        labSubject.setName(name);
        labSubjectMapper.insert(labSubject);
        return Result.success(labSubject);
    }

    @ApiOperation(value = "删除某个学科", notes = "需要 ADMIN 权限")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/subject/{id}", produces = "application/json")
    public Result<Object> delLabSubject(@ApiParam(value = "学科ID", required = true) @PathVariable String id) {
        int i = labSubjectMapper.deleteById(id);
        if (i == 0) return Result.failure();
        return Result.success();
    }

    @ApiOperation(value = "修改某个学科", notes = "需要 ADMIN 权限")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/subject/{id}", produces = "application/json")
    public Result<LabSubject> modifyLabSubject(@ApiParam(value = "学科ID", required = true) @PathVariable String id,
                                               @ApiParam(value = "学科名字", required = true) @RequestParam String name) {
        LabSubject labSubject = labSubjectMapper.selectById(id);
        labSubject.setName(name);
        labSubjectMapper.updateById(labSubject);
        return Result.success(labSubject);
    }

    @ApiOperation("获取实验室列表")
    @GetMapping(value = "/lab", produces = "application/json")
    public Result<List<Lab>> getLabs(
            @ApiParam("第几页,0开始") @RequestParam(required = false) Integer page,
            @ApiParam("每页的数量，默认10") @RequestParam(required = false) Integer count) {
        if (page == null) page = 0;
        if (count == null) count = 10;
        return Result.success(labMapper.selectList(
                new QueryWrapper<Lab>().last("limit " + page * count + "," + count)));
    }

    @ApiOperation("获取某个实验室")
    @GetMapping(value = "/lab/{id}", produces = "application/json")
    public Result<Lab> getLab(@ApiParam(value = "实验室ID", required = true) @PathVariable String id) {
        Lab lab = labMapper.selectById(id);
        if (lab == null) return Result.of(999, "id不存在!");
        return Result.success(lab);
    }

    @ApiOperation("添加实验室")
    @PostMapping(value = "/lab", produces = "application/json")
    public Result<Lab> addLab(
            @ApiIgnore Authentication authentication,
            @ApiParam(value = "学科ID", required = true) @RequestParam Integer subjectId,
            @ApiParam(value = "实验名称", required = true) @RequestParam String name,
            @ApiParam(value = "最大人数", required = true) @RequestParam Integer playerLimit,
            @ApiParam(value = "是否允许说话 0 不允许, 1允许", required = true) @RequestParam Integer enableVoice,
            @ApiParam(value = "是否允许操作 0 不允许, 1允许", required = true) @RequestParam Integer enableAction,
            @ApiParam(value = "器材列表，为器材id列表 以','为分隔符", required = true) @RequestParam String equipments
    ) {
        PlayerInfo playerInfo = playerMapper.selectOne(new LambdaQueryWrapper<PlayerInfo>().eq(PlayerInfo::getUsrName, authentication.getName()));
        if (playerInfo == null) return Result.of(ResultCode.USER_NOT_LOGIN);
        if (labSubjectMapper.selectById(subjectId) == null) {
            return Result.of(999, "学科不存在");
        }
        Lab lab = new Lab();
        lab.setSubjectId(subjectId);
        lab.setName(name);
        lab.setOwner(playerInfo.getPlayerId());
        lab.setPlayerLimit(playerLimit);
        lab.setCreateTime(new Date());
        lab.setEnableVoice(enableVoice);
        lab.setEnableAction(enableAction);
        lab.setEquipments(equipments);
        labMapper.insert(lab);
        return Result.success(lab);
    }

    @ApiOperation(value = "删除实验室", notes = "普通玩家只能删除自己创建的，ADMIN可以删除任意人的")
    @DeleteMapping(value = "/lab/{id}", produces = "application/json")
    public Result<Object> delLab(@ApiIgnore Authentication authentication,
                                 @ApiParam(value = "实验室ID", required = true) @PathVariable String id) {
        PlayerInfo playerInfo = playerMapper.selectOne(new LambdaQueryWrapper<PlayerInfo>().eq(PlayerInfo::getUsrName, authentication.getName()));
        if (playerInfo == null) return Result.of(ResultCode.USER_NOT_LOGIN);
        LambdaQueryWrapper<Lab> wrapper = new LambdaQueryWrapper<Lab>().eq(Lab::getLabId, id);
        if (Objects.equals(playerInfo.getRole(), "PLAYER")) {
            wrapper = wrapper.eq(Lab::getOwner, playerInfo.getPlayerId());
        }
        int delete = labMapper.delete(wrapper);
        if (delete == 0) return Result.failure();
        return Result.success();
    }

    @ApiOperation(value = "修改实验室", notes = "普通玩家只能修改自己创建的，ADMIN可以修改任意人的")
    @PostMapping(value = "/lab/{id}", produces = "application/json")
    public Result<Lab> modifyLab(
            @ApiIgnore Authentication authentication,
            @ApiParam(value = "实验室ID", required = true) @PathVariable Integer id,
            @ApiParam("学科ID") @RequestParam(required = false) Integer subjectId,
            @ApiParam("实验名称") @RequestParam(required = false) String name,
            @ApiParam("最大人数") @RequestParam(required = false) Integer playerLimit,
            @ApiParam("是否允许说话 0 不允许, 1允许") @RequestParam(required = false) Integer enableVoice,
            @ApiParam("是否允许操作 0 不允许, 1允许") @RequestParam(required = false) Integer enableAction,
            @ApiParam("器材列表，为器材id列表 以','为分隔符") @RequestParam(required = false) String equipments
    ) {
        PlayerInfo playerInfo = playerMapper.selectOne(new LambdaQueryWrapper<PlayerInfo>().eq(PlayerInfo::getUsrName, authentication.getName()));
        if (playerInfo == null) return Result.of(ResultCode.USER_NOT_LOGIN);
        LambdaQueryWrapper<Lab> wrapper = new LambdaQueryWrapper<Lab>()
                .eq(Lab::getLabId, id);
        if (Objects.equals(playerInfo.getRole(), "PLAYER"))
            wrapper = wrapper.eq(Lab::getOwner, playerInfo.getPlayerId());
        Lab lab = labMapper.selectOne(wrapper);
        if (lab == null) return Result.of(999, "实验室不存在");
        if (subjectId != null && labSubjectMapper.selectById(subjectId) == null) {
            return Result.of(999, "学科不存在");
        }
        if (subjectId != null) lab.setSubjectId(subjectId);
        if (name != null) lab.setName(name);
        if (playerLimit != null) lab.setPlayerLimit(playerLimit);
        if (enableVoice != null) lab.setEnableVoice(enableVoice);
        if (enableAction != null) lab.setEnableAction(enableAction);
        if (equipments != null) lab.setEquipments(equipments);
        labMapper.updateById(lab);
        return Result.success(lab);
    }

    @ApiOperation(value = "上传器材图片", notes = "需要 ADMIN 权限 'multipart/form-data' 协议")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @PostMapping(value = "/equipment/upload", produces = "application/json")
    public Result<Object> uploadEquipment(
            @ApiParam(value = "上传的文件", required = true) @RequestPart MultipartFile file,
            @ApiParam(value = "器材名", required = true) @RequestParam String name,
            @ApiParam(value = "器材分类", required = true) @RequestParam String type
    ) throws IOException {

        File folder = new File(USER_DIR, "equipments");
        String fileName = file.getOriginalFilename();
        InputStream inputStream = file.getInputStream();
        String md5 = DigestUtils.md5DigestAsHex(inputStream);
        LabEquipment labEquipment = new LabEquipment();
        labEquipment.setName(name);
        labEquipment.setType(type);
        labEquipment.setMd5(md5);
        labEquipmentMapper.insert(labEquipment);
        File storeFile = new File(folder, type + File.separatorChar + labEquipment.getEqId() + fileName.substring(fileName.lastIndexOf(".")));
//        System.out.println(storeFile);
        storeFile.getParentFile().mkdirs();
//        storeFile.createNewFile();
        file.transferTo(storeFile);
        return Result.success(labEquipment);
    }

    @ApiOperation(value = "下载器材图片")
    @GetMapping(value = "/equipment/download/{id}", produces = "application/octet-stream")
    public Object downloadEquipment(
            @ApiParam(value = "下载的器材ID", required = true) @PathVariable Integer id
    ) throws Exception {
        LabEquipment labEquipment = labEquipmentMapper.selectById(id);
        if (labEquipment == null) throw new IllegalArgumentException("器材不存在!");
        File[] files = new File(USER_DIR, "equipments" + File.separatorChar + labEquipment.getType())
                .listFiles(it -> {
                    String name = it.getName();
                    name = name.substring(0, name.lastIndexOf('.'));
                    return name.equals(id.toString());
                });
        if (files.length != 1) throw new IllegalArgumentException("器材图片不存在!");
        File file = files[0];
        InputStreamResource isr = new InputStreamResource(Files.newInputStream(file.toPath()));
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-disposition", "attachment; filename=" + URLEncoder.encode(file.getName(), "UTF-8"))
                .body(isr);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "删除器材", notes = "需要 ADMIN 权限")
    @DeleteMapping(value = "/equipment/{id}", produces = "application/json")
    public Result<Object> delEquipment(
            @ApiParam(value = "器材ID", required = true) @PathVariable Integer id
    ) {
        LabEquipment labEquipment = labEquipmentMapper.selectById(id);
        if (labEquipment == null) throw new IllegalArgumentException("器材不存在!");
        File[] files = new File(USER_DIR, "equipments" + File.separatorChar + labEquipment.getType())
                .listFiles(it -> {
                    String name = it.getName();
                    name = name.substring(0, name.lastIndexOf('.'));
                    return name.equals(id.toString());
                });
        if (files != null && files.length == 1) {
            try {
                files[0].delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        labEquipmentMapper.deleteById(id);
        return Result.success();
    }

    @ApiOperation("获取所有器材")
    @GetMapping(value = "/equipment", produces = "application/json")
    public Result<List<LabEquipment>> getEquipments() {
        return Result.success(labEquipmentMapper.selectList(null));
    }

    @Transactional
    @ApiOperation("加入某个实验室")
    @PostMapping(value = "/join/{id}", produces = "application/json")
    public Result<LabPlayer> joinLab(
            @ApiIgnore Authentication authentication,
            @ApiParam(value = "实验室ID", required = true) @PathVariable Integer id
    ) {
        PlayerInfo playerInfo = playerMapper.selectOne(new LambdaQueryWrapper<PlayerInfo>().eq(PlayerInfo::getUsrName, authentication.getName()));
        if (playerInfo == null) return Result.of(ResultCode.USER_NOT_LOGIN);
        if (labPlayerMapper.exists(
                new LambdaQueryWrapper<LabPlayer>()
                        .eq(LabPlayer::getPlayerId, playerInfo.getPlayerId())
                        .last("limit 1")
        )) {
            return Result.of(999, "你已在实验室中");
        }
        Lab lab = labMapper.selectById(id);
        if (lab == null) return Result.of(998, "实验室不存在");
        if (lab.getPlayerCount() >= lab.getPlayerLimit()) {
            return Result.of(997, "实验室已满员");
        }
        lab.setPlayerCount(lab.getPlayerCount() + 1);
        labMapper.updateById(lab);
        LabPlayer labPlayer = new LabPlayer();
        labPlayer.setLabId(id);
        labPlayer.setPlayerId(playerInfo.getPlayerId());
        labPlayerMapper.insert(labPlayer);
        return Result.success(labPlayer);
    }

    @Transactional
    @ApiOperation("退出当前实验室")
    @PostMapping(value = "/quit", produces = "application/json")
    public Result<Object> quitLab(@ApiIgnore Authentication authentication) {
        PlayerInfo playerInfo = playerMapper.selectOne(new LambdaQueryWrapper<PlayerInfo>().eq(PlayerInfo::getUsrName, authentication.getName()));
        if (playerInfo == null) return Result.of(ResultCode.USER_NOT_LOGIN);
        LambdaQueryWrapper<LabPlayer> eq = new LambdaQueryWrapper<LabPlayer>().eq(LabPlayer::getPlayerId, playerInfo.getPlayerId());
        LabPlayer labPlayer = labPlayerMapper.selectOne(eq);
        if (labPlayer == null) return Result.of(999, "你不在实验室中");
        labMapper.update(null, new LambdaUpdateWrapper<Lab>()
                .setSql("'player_count' = 'player_count' + 1")
                .eq(Lab::getLabId, labPlayer.getLpId())
        );
        labPlayerMapper.delete(eq);
        return Result.success();
    }

    @ApiOperation("获取当前实验室的所有玩家")
    @PostMapping(value = "/players", produces = "application/json")
    public Result<List<PlayerInfo>> getLabPlayers(@ApiIgnore Authentication authentication) {
        PlayerInfo playerInfo = playerMapper.selectOne(new LambdaQueryWrapper<PlayerInfo>().eq(PlayerInfo::getUsrName, authentication.getName()));
        if (playerInfo == null) return Result.of(ResultCode.USER_NOT_LOGIN);
        LambdaQueryWrapper<LabPlayer> eq = new LambdaQueryWrapper<LabPlayer>()
                .eq(LabPlayer::getPlayerId, playerInfo.getPlayerId());
        LabPlayer labPlayer = labPlayerMapper.selectOne(eq);
        if (labPlayer == null) return Result.of(999, "你不在实验室中");
        List<Integer> labs =
                labPlayerMapper.selectList(new LambdaQueryWrapper<LabPlayer>()
                        .eq(LabPlayer::getLpId, labPlayer.getLabId())).stream().map(LabPlayer::getPlayerId).collect(Collectors.toList());
        return Result.success(playerMapper.selectBatchIds(labs));
    }
}
