package top.iseason.metaworldeducation.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import top.iseason.metaworldeducation.entity.ApplyRecord;
import top.iseason.metaworldeducation.entity.PlayerInfo;
import top.iseason.metaworldeducation.entity.UserFriendInfo;
import top.iseason.metaworldeducation.mapper.ApplyRecordMapper;
import top.iseason.metaworldeducation.mapper.UserFriendInfoMapper;
import top.iseason.metaworldeducation.service.PlayerService;
import top.iseason.metaworldeducation.util.Result;
import top.iseason.metaworldeducation.util.ResultCode;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "玩家API，需登录")
@RestController
@RequestMapping("/player")
public class PlayerController {

    @Resource
    PlayerService playerService;
    @Resource
    ApplyRecordMapper applyRecordMapper;
    @Resource
    UserFriendInfoMapper userFriendInfoMapper;

    @ApiOperation("更新玩家外观信息接口.")
    @PostMapping("/UpdateUserInfo")
    public Result updateInfo(@ApiIgnore Authentication authentication,
                             @ApiParam("0、1、2、3、4分别代表女学生、男学生、职业女性、职业男性") @RequestParam(required = false) Integer identityName,
                             @ApiParam("发型") @RequestParam(required = false) Integer hairType,
                             @ApiParam("脸型") @RequestParam(required = false) Integer faceType,
                             @ApiParam("头发颜色") @RequestParam(required = false) Integer hairColor,
                             @ApiParam("肤色") @RequestParam(required = false) Integer faceColor,
                             @ApiParam("爱好") @RequestParam(required = false) String hobby,
                             @ApiParam("性格") @RequestParam(required = false) String disposition,
                             @ApiParam("职业") @RequestParam(required = false) String occupation,
                             @ApiParam("故事") @RequestParam(required = false) String story,
                             @ApiParam("上衣") @RequestParam(required = false) Integer blouseIndex,
                             @ApiParam("裤子") @RequestParam(required = false) Integer trousersIndex,
                             @ApiParam("鞋子") @RequestParam(required = false) Integer shoeIndex
    ) {
        PlayerInfo playerInfo = playerService.getOne(new LambdaQueryWrapper<PlayerInfo>().eq(PlayerInfo::getUsrName, authentication.getName()));
        if (playerInfo == null) return Result.of(ResultCode.USER_NOT_LOGIN);
        if (identityName != null) playerInfo.setIdentityName(identityName);
        if (hairType != null) playerInfo.setHairType(hairType);
        if (faceType != null) playerInfo.setFaceType(faceType);
        if (hairColor != null) playerInfo.setHairColor(hairColor);
        if (faceColor != null) playerInfo.setFaceColor(faceColor);
        if (hobby != null) playerInfo.setHobby(hobby);
        if (disposition != null) playerInfo.setDisposition(disposition);
        if (occupation != null) playerInfo.setOccupation(occupation);
        if (story != null) playerInfo.setStory(story);
        if (blouseIndex != null) playerInfo.setBlouseIndex(blouseIndex);
        if (trousersIndex != null) playerInfo.setTrousersIndex(trousersIndex);
        if (shoeIndex != null) playerInfo.setShoeIndex(shoeIndex);
        try {
            playerService.updateById(playerInfo);
        } catch (Exception e) {
            return Result.failure();
        }
        return Result.success();
    }

    @ApiOperation("更新玩家位置信息接口.")
    @PostMapping("/UpdatePosition")
    public Result updatePosition(@ApiIgnore Authentication authentication,
                                 @ApiParam("X坐标") @RequestParam(required = false) Float posX,
                                 @ApiParam("Y坐标") @RequestParam(required = false) Float posY,
                                 @ApiParam("Z坐标") @RequestParam(required = false) Float posZ,
                                 @ApiParam("X角") @RequestParam(required = false) Float roaX,
                                 @ApiParam("Y角") @RequestParam(required = false) Float roaY,
                                 @ApiParam("Z角") @RequestParam(required = false) Float roaZ,
                                 @ApiParam("移动速度") @RequestParam(required = false) Float moveSpeed,
                                 @ApiParam("旋转速度") @RequestParam(required = false) Float rotateSpeed,
                                 @ApiParam("玩家是否正在走路，0:不是，1：是") @RequestParam(required = false) Integer isRunning,
                                 @ApiParam("玩家是否正在讲话，0:不是，1：是") @RequestParam(required = false) Integer isTalking,
                                 @ApiParam("玩家讲话内容") @RequestParam(required = false) String talkMsg,
//                                 @ApiParam("登录账户") @RequestParam(required = false) String usrName,
//                                 @ApiParam("登录密码") @RequestParam(required = false) String usrPwd,
                                 @ApiParam("用户当前的场景") @RequestParam(required = false) Integer sceneID,
                                 @ApiParam("用户当前参加的活动") @RequestParam(required = false) Integer activityID
    ) {
        PlayerInfo playerInfo = playerService.getOne(new LambdaQueryWrapper<PlayerInfo>().eq(PlayerInfo::getUsrName, authentication.getName()));
        if (playerInfo == null) return Result.of(ResultCode.USER_NOT_LOGIN);
        if (posX != null) playerInfo.setPosX(posX);
        if (posY != null) playerInfo.setPosY(posY);
        if (posZ != null) playerInfo.setPosZ(posZ);
        if (roaX != null) playerInfo.setRoaX(roaX);
        if (roaY != null) playerInfo.setRoaY(roaY);
        if (roaZ != null) playerInfo.setRoaZ(roaZ);
        if (moveSpeed != null) playerInfo.setMoveSpeed(moveSpeed);
        if (rotateSpeed != null) playerInfo.setRotateSpeed(rotateSpeed);
        if (isRunning != null) playerInfo.setIsRunning(isRunning);
        if (isTalking != null) playerInfo.setIsTalking(isTalking);
        if (talkMsg != null) playerInfo.setTalkMsg(talkMsg);
//        if (usrName != null) playerInfo.setUsrName(usrName);
//        if (usrPwd != null) playerInfo.setUsrPwd(usrPwd);
        if (sceneID != null) playerInfo.setSceneId(sceneID);
        if (activityID != null) playerInfo.setActivityId(activityID);
        try {
            playerService.updateById(playerInfo);
        } catch (Exception e) {
            return Result.failure();
        }
        return Result.success();
    }

    @ApiOperation("由id获取玩家信息")
    @GetMapping("/GetPlayerInfoByPlayerID")
    public Result getPlayerInfo(@ApiParam("玩家id") @RequestParam Integer id) {
        PlayerInfo byId = playerService.getById(id);
        if (byId == null) return Result.of(ResultCode.USER_NOT_EXIST);
        byId.setUsrPwd(null);
        return Result.success(byId);
    }

    @ApiOperation("获取同一个场景同一个活动的其他玩家")
    @GetMapping("/GetOtherPlayerInfos")
    public Result getOtherPlayerInfos(@ApiIgnore Authentication authentication) {
        PlayerInfo playerInfo = playerService.getOne(new LambdaQueryWrapper<PlayerInfo>().eq(PlayerInfo::getUsrName, authentication.getName()));
        if (playerInfo == null) return Result.of(ResultCode.USER_NOT_LOGIN);
        Integer sceneId = playerInfo.getSceneId();
        if (sceneId == null) return Result.of(3001, "玩家不在场景中");
        Integer activityId = playerInfo.getActivityId();
        if (activityId == null) return Result.of(3002, "玩家不在活动中");
        List<PlayerInfo> list = playerService.list(new LambdaQueryWrapper<PlayerInfo>()
                .eq(PlayerInfo::getSceneId, sceneId)
                .eq(PlayerInfo::getActivityId, activityId));
        list.remove(playerInfo);
        //脱敏
        for (PlayerInfo info : list) {
            info.setUsrPwd(null);
        }
        return Result.success(list);
    }

    @ApiOperation("向某个玩家发起好友请求")
    @PostMapping("/AddApplyRecord")
    public Result AddApplyRecord(@ApiIgnore Authentication authentication, @ApiParam("目标玩家id") @RequestParam Integer receiver) {
        PlayerInfo playerInfo = playerService.getOne(new LambdaQueryWrapper<PlayerInfo>().eq(PlayerInfo::getUsrName, authentication.getName()));
        if (playerInfo == null) return Result.of(ResultCode.USER_NOT_LOGIN);
        ApplyRecord applyRecord = new ApplyRecord();
        applyRecord.setApplyTime(new Date());
        applyRecord.setApplyId(playerInfo.getPlayerId());
        applyRecord.setReceiver(receiver);
        try {
            applyRecordMapper.insert(applyRecord);
        } catch (Exception e) {
            return Result.of(3003, "请求已存在!");
        }
        return Result.success();
    }

    @ApiOperation("获取所有好友请求")
    @GetMapping("/GetApplyRecords")
    public Result getApplyRecords(@ApiIgnore Authentication authentication,
                                  @ApiParam("第几页,0开始") @RequestParam(required = false) Integer page,
                                  @ApiParam("每页的数量，默认10") @RequestParam(required = false) Integer count
    ) {
        PlayerInfo playerInfo = playerService.getOne(new LambdaQueryWrapper<PlayerInfo>().eq(PlayerInfo::getUsrName, authentication.getName()));
        if (playerInfo == null) return Result.of(ResultCode.USER_NOT_LOGIN);
        if (page == null) page = 0;
        if (count == null) count = 10;
        List<ApplyRecord> applyRecords = applyRecordMapper.selectList(new LambdaQueryWrapper<ApplyRecord>().eq(ApplyRecord::getApplyId, playerInfo.getPlayerId()).last("limit " + page * count + "," + count));
        return Result.success(applyRecords);
    }

    @Transactional
    @ApiOperation("处理某个好友请求")
    @PostMapping("/SetApplyRecord")
    public Result setApplyRecord(@ApiIgnore Authentication authentication,
                                 @ApiParam("请求id") @RequestParam Integer requestId,
                                 @ApiParam("状态,0未处理,1接受,-1拒绝") @RequestParam Integer state
    ) {
        PlayerInfo playerInfo = playerService.getOne(new LambdaQueryWrapper<PlayerInfo>().eq(PlayerInfo::getUsrName, authentication.getName()));
        if (playerInfo == null) return Result.of(ResultCode.USER_NOT_LOGIN);
        ApplyRecord applyRecord = applyRecordMapper.selectById(requestId);
        if (applyRecord == null) return Result.of(3004, "请求不存在!");
        applyRecord.setResult(state);
        //同意请求
        if (state == 1) {
            try {
                userFriendInfoMapper.insert(new UserFriendInfo()
                        .setUserId(applyRecord.getReceiver())
                        .setFriendId(applyRecord.getApplyId()));
            } catch (Exception ignored) {
            }
            try {
                userFriendInfoMapper.insert(new UserFriendInfo()
                        .setUserId(applyRecord.getApplyId())
                        .setFriendId(applyRecord.getReceiver()));
            } catch (Exception ignored) {
            }
            log.info("玩家 " + applyRecord.getReceiver() + " 与玩家 " + applyRecord.getApplyId() + " 建立了好友关系");
        }
        try {
            applyRecordMapper.updateById(applyRecord);
        } catch (Exception e) {
            return Result.failure();
        }
        return Result.success();
    }

    @ApiOperation("获取好友列表")
    @GetMapping("/GetFriends")
    public Result getFriends(@ApiIgnore Authentication authentication,
                             @ApiParam("第几页,0开始") @RequestParam(required = false) Integer page,
                             @ApiParam("每页的数量，默认10") @RequestParam(required = false) Integer count
    ) {
        PlayerInfo playerInfo = playerService.getOne(new LambdaQueryWrapper<PlayerInfo>().eq(PlayerInfo::getUsrName, authentication.getName()));
        if (playerInfo == null) return Result.of(ResultCode.USER_NOT_LOGIN);
        if (page == null) page = 0;
        if (count == null) count = 10;
        List<UserFriendInfo> userFriendInfos = userFriendInfoMapper.selectList(new LambdaQueryWrapper<UserFriendInfo>().eq(UserFriendInfo::getUserId, playerInfo.getPlayerId()).last("limit " + page * count + "," + count));
        List<Integer> collect = userFriendInfos.stream().map(UserFriendInfo::getFriendId).collect(Collectors.toList());
        List<PlayerInfo> playerInfos = playerService.listByIds(collect);
        //脱敏
        for (PlayerInfo info : playerInfos) {
            info.setUsrPwd(null);
        }
        return Result.success(playerInfos);
    }

    @ApiOperation("删除好友")
    @GetMapping("/RemoveFriend")
    public Result removeFriends(@ApiIgnore Authentication authentication,
                                @ApiParam("朋友id") @RequestParam Integer friendId
    ) {
        PlayerInfo playerInfo = playerService.getOne(new LambdaQueryWrapper<PlayerInfo>().eq(PlayerInfo::getUsrName, authentication.getName()));
        if (playerInfo == null) return Result.of(ResultCode.USER_NOT_LOGIN);
        int delete = userFriendInfoMapper.delete(new LambdaQueryWrapper<UserFriendInfo>()
                .eq(UserFriendInfo::getUserId, playerInfo.getPlayerId())
                .eq(UserFriendInfo::getFriendId, friendId).last("limit 1")
        );
        if (delete == 1) {
            return Result.success();
        } else return Result.failure();
    }
}
