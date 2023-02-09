package top.iseason.metaworldeducation.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import top.iseason.metaworldeducation.entity.ActivityInfo;
import top.iseason.metaworldeducation.entity.ActivityPlayer;
import top.iseason.metaworldeducation.entity.PlayerInfo;
import top.iseason.metaworldeducation.mapper.ActivityInfoMapper;
import top.iseason.metaworldeducation.mapper.ActivityPlayerMapper;
import top.iseason.metaworldeducation.mapper.PlayerMapper;
import top.iseason.metaworldeducation.util.DateUtil;
import top.iseason.metaworldeducation.util.Result;
import top.iseason.metaworldeducation.util.ResultCode;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "房间API，需玩家登录")
@RestController
@RequestMapping("/room")
public class RoomController {

    @Resource
    PlayerMapper playerMapper;
    @Resource
    ActivityInfoMapper activityInfoMapper;
    @Resource
    ActivityPlayerMapper activityPlayerMapper;

    @ApiOperation("获取同一个房间的其他玩家")
    @GetMapping(value = "/getOtherPlayerInfos", produces = "application/json")
    public Result<List<PlayerInfo>> getOtherPlayerInfos(@ApiIgnore Authentication authentication) {
        PlayerInfo playerInfo = playerMapper.selectOne(new LambdaQueryWrapper<PlayerInfo>().eq(PlayerInfo::getUsrName, authentication.getName()));
        if (playerInfo == null) return Result.of(ResultCode.USER_NOT_LOGIN);
        Integer sceneId = playerInfo.getSceneId();
        if (sceneId == null) return Result.of(3001, "玩家不在场景中");
        Integer activityId = playerInfo.getActivityId();
        if (activityId == null) return Result.of(3002, "玩家不在活动中");
        List<ActivityPlayer> activityPlayers = activityPlayerMapper.selectList(new LambdaQueryWrapper<ActivityPlayer>().eq(ActivityPlayer::getActivityId, activityId));
        if (activityPlayers.isEmpty()) return Result.success(null);
        List<Integer> collect = activityPlayers.stream().map(ActivityPlayer::getPlayerId).collect(Collectors.toList());
        List<PlayerInfo> list = playerMapper.selectBatchIds(collect);
        list.remove(playerInfo);
        //脱敏
        for (PlayerInfo info : list) {
            info.setUsrPwd(null);
        }
        return Result.success(list);
    }

    @ApiOperation("创建房间/活动")
    @PostMapping(value = "/createRoom", produces = "application/json")
    public Result<ActivityInfo> createRoom(
            @ApiIgnore Authentication authentication,
            @ApiParam(value = "场景ID", required = true) @RequestParam Integer sceneId,
            @ApiParam(value = "房间名称,限长255", required = true) @RequestParam String activityName,
            @ApiParam(value = "开始时间,格式 yyyy-MM-dd HH:mm:ss", required = true) @RequestParam String startTime,
            @ApiParam(value = "结束时间,格式 yyyy-MM-dd HH:mm:ss", required = true) @RequestParam String endTime,
            @ApiParam(value = "房间类型, 0为课程、1为游览、2为实验", required = true) @RequestParam Integer activityType,
            @ApiParam(value = "可见性类型, 0为公开、1私有", required = true) @RequestParam Integer activityPermission,
            @ApiParam(value = "私有房间密码", required = true) @RequestParam(required = false) String activityPassword,
            @ApiParam("最大玩家数, 不设置则无限制") @RequestParam(required = false) Integer maxPlayer
    ) {
        PlayerInfo playerInfo = playerMapper.selectOne(new LambdaQueryWrapper<PlayerInfo>().eq(PlayerInfo::getUsrName, authentication.getName()));
        if (playerInfo == null) return Result.of(ResultCode.USER_NOT_LOGIN);
        if (activityName.length() > 255) return Result.of(999, "房间名称过长");
        Date start, end;
        try {
            start = DateUtil.toDate(startTime);
        } catch (Exception e) {
            return Result.of(999, "开始时间格式错误!");
        }
        try {
            end = DateUtil.toDate(endTime);
        } catch (Exception e) {
            return Result.of(999, "结束时间格式错误!");
        }
        ActivityInfo activityInfo = new ActivityInfo();
        activityInfo.setSceneId(sceneId);
        activityInfo.setActivityName(activityName);
        activityInfo.setCreateTime(new Date());
        activityInfo.setStartTime(start);
        activityInfo.setEndTime(end);
        activityInfo.setPlayerId(playerInfo.getPlayerId());
        activityInfo.setPlayerName(playerInfo.getPlayerName() == null ? playerInfo.getUsrName() : playerInfo.getPlayerName());
        activityInfo.setActivityType(activityType);
        activityInfo.setMaxPlayer(maxPlayer);
        activityInfo.setActivityPermission(activityPermission);
        activityInfo.setActivityPassword(activityPassword == null ? "" : activityPassword);
        activityInfoMapper.insert(activityInfo);
        return Result.success(activityInfo);
    }

    @ApiOperation("获取房间/活动列表")
    @GetMapping(value = "/getRooms", produces = "application/json")
    public Result<List<ActivityInfo>> getRooms(
            @ApiParam("场景ID,没有则全部") @RequestParam(required = false) Integer sceneId,
            @ApiParam("创建者ID,没有则全部") @RequestParam(required = false) Integer playerId,
            @ApiParam("第几页,0开始") @RequestParam(required = false) Integer page,
            @ApiParam("每页的数量，默认10") @RequestParam(required = false) Integer count
    ) {
        if (page == null) page = 0;
        if (count == null) count = 10;
        LambdaQueryWrapper<ActivityInfo> wrapper = new LambdaQueryWrapper<>();
        if (sceneId != null) wrapper = wrapper.eq(ActivityInfo::getSceneId, sceneId);
        if (playerId != null) wrapper = wrapper.eq(ActivityInfo::getPlayerId, playerId);
        List<ActivityInfo> activities = activityInfoMapper.selectList(wrapper.last("limit " + page * count + "," + count));
        for (ActivityInfo activity : activities) {
            activity.setActivityPassword(null);
        }
        return Result.success(activities);
    }

    @ApiOperation("获取自己的房间/活动列表")
    @GetMapping(value = "/getMyRooms", produces = "application/json")
    public Result<List<ActivityInfo>> getMyRooms(
            @ApiIgnore Authentication authentication,
            @ApiParam("第几页,0开始") @RequestParam(required = false) Integer page,
            @ApiParam("每页的数量，默认10") @RequestParam(required = false) Integer count
    ) {
        PlayerInfo playerInfo = playerMapper.selectOne(new LambdaQueryWrapper<PlayerInfo>().eq(PlayerInfo::getUsrName, authentication.getName()));
        if (playerInfo == null) return Result.of(ResultCode.USER_NOT_LOGIN);
        if (page == null) page = 0;
        if (count == null) count = 10;
        List<ActivityInfo> activities = activityInfoMapper.selectList(new LambdaQueryWrapper<ActivityInfo>()
                .eq(ActivityInfo::getPlayerId, playerInfo.getPlayerId())
                .last("limit " + page * count + "," + count));
        return Result.success(activities);
    }

    @ApiOperation("按房间名称搜索房间")
    @GetMapping(value = "/searchRooms", produces = "application/json")
    public Result<List<ActivityInfo>> searchRooms(
            @ApiParam("房间名称") @RequestParam(required = false) String name,
            @ApiParam("第几页,0开始") @RequestParam(required = false) Integer page,
            @ApiParam("每页的数量，默认10") @RequestParam(required = false) Integer count
    ) {
        if (page == null) page = 0;
        if (count == null) count = 10;
        List<ActivityInfo> activities = activityInfoMapper.selectList(new LambdaQueryWrapper<ActivityInfo>()
                .like(ActivityInfo::getActivityName, name)
                .last("limit " + page * count + "," + count));
        for (ActivityInfo activity : activities) {
            activity.setActivityPassword(null);
        }
        return Result.success(activities);
    }

    @ApiOperation("删除自己创建的房间")
    @DeleteMapping(value = "/removeRoom", produces = "application/json")
    public Result<Object> searchRoom(
            @ApiIgnore Authentication authentication,
            @ApiParam(value = "房间ID", required = true) @RequestParam(required = false) Integer activityId
    ) {
        PlayerInfo playerInfo = playerMapper.selectOne(new LambdaQueryWrapper<PlayerInfo>().eq(PlayerInfo::getUsrName, authentication.getName()));
        if (playerInfo == null) return Result.of(ResultCode.USER_NOT_LOGIN);
        ActivityInfo activityInfo = activityInfoMapper.selectById(activityId);
        if (activityInfo == null) return Result.of(ResultCode.ROOM_NOT_EXIST);
        if (!Objects.equals(activityInfo.getPlayerId(), playerInfo.getPlayerId()))
            return Result.of(ResultCode.NO_PERMISSION);
        activityInfoMapper.deleteById(activityInfo);
        return Result.success();
    }

    @ApiOperation("修改自己的房间信息")
    @PostMapping(value = "/editRoom", produces = "application/json")
    public Result<ActivityInfo> editRoom(
            @ApiIgnore Authentication authentication,
            @ApiParam(value = "房间ID", required = true) @RequestParam Integer activityId,
            @ApiParam("场景ID") @RequestParam(required = false) Integer sceneId,
            @ApiParam("房间名称,限长255") @RequestParam(required = false) String activityName,
            @ApiParam("开始时间,格式 yyyy-MM-dd HH:mm:ss") @RequestParam(required = false) String startTime,
            @ApiParam("结束时间,格式 yyyy-MM-dd HH:mm:ss") @RequestParam(required = false) String endTime,
            @ApiParam("房间类型, 0为自由、1为公开、2为非公开") @RequestParam(required = false) Integer activityType,
            @ApiParam("可见性类型, 0为公开、1私有") @RequestParam(required = false) Integer activityPermission,
            @ApiParam("私有房间密码") @RequestParam(required = false) String activityPassword,
            @ApiParam("最大玩家数, 不设置则无限制") @RequestParam(required = false) Integer maxPlayer
    ) {
        PlayerInfo playerInfo = playerMapper.selectOne(new LambdaQueryWrapper<PlayerInfo>().eq(PlayerInfo::getUsrName, authentication.getName()));
        if (playerInfo == null) return Result.of(ResultCode.USER_NOT_LOGIN);
        ActivityInfo activityInfo = activityInfoMapper.selectById(activityId);
        if (activityInfo == null) return Result.of(ResultCode.ROOM_NOT_EXIST);
        if (!Objects.equals(activityInfo.getPlayerId(), playerInfo.getPlayerId()))
            return Result.of(ResultCode.NO_PERMISSION);
        if (activityName.length() > 255) return Result.of(999, "房间名称过长");
        Date start = null, end = null;
        try {
            if (startTime != null)
                start = DateUtil.toDate(startTime);
        } catch (Exception e) {
            return Result.of(999, "开始时间格式错误!");
        }
        try {
            if (endTime != null)
                end = DateUtil.toDate(endTime);
        } catch (Exception e) {
            return Result.of(999, "结束时间格式错误!");
        }
        if (activityName != null) activityInfo.setActivityName(activityName);
        if (start != null) activityInfo.setStartTime(start);
        if (end != null) activityInfo.setEndTime(end);
        if (activityType != null) activityInfo.setActivityType(activityType);
        if (sceneId != null) activityInfo.setSceneId(sceneId);
        if (maxPlayer != null) activityInfo.setMaxPlayer(maxPlayer);
        if (activityPermission != null) activityInfo.setActivityPermission(activityPermission);
        if (activityPassword != null) activityInfo.setActivityPassword(activityPassword);
        activityInfoMapper.updateById(activityInfo);
        return Result.success(activityInfo);
    }

    @Transactional
    @ApiOperation("加入一个房间")
    @PostMapping(value = "/joinRoom", produces = "application/json")
    public Result<Object> joinRoom(
            @ApiIgnore Authentication authentication,
            @ApiParam(value = "房间ID", required = true) @RequestParam Integer activityId,
            @ApiParam("房间密码，如果房间为私密则必填") @RequestParam(required = false) String activityPassword
    ) {
        PlayerInfo playerInfo = playerMapper.selectOne(new LambdaQueryWrapper<PlayerInfo>().eq(PlayerInfo::getUsrName, authentication.getName()));
        if (playerInfo == null) return Result.of(ResultCode.USER_NOT_LOGIN);
        if (playerInfo.getActivityId() != null) return Result.of(ResultCode.ROOM_ALREADY_JOINED);
        ActivityInfo activityInfo = activityInfoMapper.selectById(activityId);
        if (activityInfo == null) return Result.of(ResultCode.ROOM_NOT_EXIST);
        if (activityInfo.getMaxPlayer() != null && activityInfo.getCurrentPlayer() >= activityInfo.getMaxPlayer())
            return Result.of(ResultCode.ROOM_IS_FULL);
        //私有房间
        if (activityInfo.getActivityPermission() == 1 && !Objects.equals(activityInfo.getActivityPassword(), activityPassword))
            return Result.of(ResultCode.ROOM_PASSWORD_ERROR);
        activityInfo.setCurrentPlayer(activityInfo.getCurrentPlayer() + 1);
        activityInfoMapper.updateById(activityInfo);
        playerInfo.setActivityId(activityId);
        playerInfo.setSceneId(activityInfo.getSceneId());
        playerMapper.updateById(playerInfo);
        ActivityPlayer activityPlayer = new ActivityPlayer();
        activityPlayer.setActivityId(activityId);
        activityPlayer.setPlayerId(playerInfo.getPlayerId());
        activityPlayerMapper.insert(activityPlayer);
        return Result.success();
    }

    @Transactional
    @ApiOperation("退出当前的房间,会同步更新玩家信息")
    @PostMapping(value = "/quitRoom", produces = "application/json")
    public Result<Object> quitRoom(@ApiIgnore Authentication authentication) {
        PlayerInfo playerInfo = playerMapper.selectOne(new LambdaQueryWrapper<PlayerInfo>().eq(PlayerInfo::getUsrName, authentication.getName()));
        if (playerInfo == null) return Result.of(ResultCode.USER_NOT_LOGIN);
        if (playerInfo.getActivityId() == null) return Result.of(ResultCode.ROOM_NOT_JOINED);
        ActivityInfo activityInfo = activityInfoMapper.selectById(playerInfo.getActivityId());
        LambdaUpdateWrapper<PlayerInfo> wrapper = new LambdaUpdateWrapper<PlayerInfo>()
                .set(PlayerInfo::getActivityId, null)
                .set(PlayerInfo::getSceneId, null)
                .set(PlayerInfo::getPosX, null)
                .set(PlayerInfo::getPosY, null)
                .set(PlayerInfo::getPosZ, null)
                .set(PlayerInfo::getRoaX, null)
                .set(PlayerInfo::getRoaY, null)
                .set(PlayerInfo::getRoaZ, null)
                .set(PlayerInfo::getMoveSpeed, null)
                .set(PlayerInfo::getRotateSpeed, null)
                .eq(PlayerInfo::getPlayerId, playerInfo.getPlayerId());
        playerMapper.update(null, wrapper);
        activityInfo.setCurrentPlayer(activityInfo.getCurrentPlayer() - 1);
        activityPlayerMapper.delete(new LambdaQueryWrapper<ActivityPlayer>().eq(ActivityPlayer::getPlayerId, playerInfo.getPlayerId()));
        activityInfoMapper.updateById(activityInfo);
        return Result.success();
    }
}

