package top.iseason.metaworldeducation.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import top.iseason.metaworldeducation.entity.BroadCast;
import top.iseason.metaworldeducation.entity.MsgRecord;
import top.iseason.metaworldeducation.entity.PlayerInfo;
import top.iseason.metaworldeducation.mapper.BroadCastMapper;
import top.iseason.metaworldeducation.mapper.MsgRecordMapper;
import top.iseason.metaworldeducation.mapper.PlayerMapper;
import top.iseason.metaworldeducation.util.DateUtil;
import top.iseason.metaworldeducation.util.Result;
import top.iseason.metaworldeducation.util.ResultCode;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Slf4j
@Api(tags = "消息API，需登录")
@RestController
@RequestMapping("/message")
public class MessageController {
    @Resource
    PlayerMapper playerMapper;

    @Resource
    MsgRecordMapper msgRecordMapper;

    @Resource
    BroadCastMapper broadCastMapper;

    @Transactional
    @ApiOperation("发送消息给玩家")
    @PostMapping("/send")
    public Result send(@ApiIgnore Authentication authentication,
                       @ApiParam("接受者ID") @RequestParam Integer receiveId,
                       @ApiParam("消息内容") @RequestParam String msg,
                       @ApiParam("房间ID,不传入则表示不在房间中") @RequestParam(required = false) Integer activityId,
                       @ApiParam("场景ID,不传入则表示不在房间中") @RequestParam(required = false) Integer sceneId
    ) {
        PlayerInfo playerInfo = playerMapper.selectOne(new LambdaQueryWrapper<PlayerInfo>().eq(PlayerInfo::getUsrName, authentication.getName()));
        if (playerInfo == null) return Result.of(ResultCode.USER_NOT_LOGIN);
        MsgRecord msgRecord = new MsgRecord();
        msgRecord.setSenderId(playerInfo.getPlayerId());
        msgRecord.setReceiveId(receiveId);
        msgRecord.setMsg(msg);
        msgRecord.setCreateTime(new Date());
        msgRecord.setActivityId(activityId);
        msgRecord.setSceneId(sceneId);
        msgRecordMapper.insert(msgRecord);
        return Result.success(msgRecord);
    }

    @Transactional
    @ApiOperation("获取某玩家发给自己的消息")
    @GetMapping("/receive")
    public Result getReceiveMessage(
            @ApiIgnore Authentication authentication,
            @ApiParam("玩家ID") @RequestParam Integer playerId,
            @ApiParam("第几页,0开始") @RequestParam(required = false) Integer page,
            @ApiParam("每页的数量，默认10") @RequestParam(required = false) Integer count,
            @ApiParam("房间ID,不传入则表示不在房间中") @RequestParam(required = false) Integer activityId

    ) {
        if (page == null) page = 0;
        if (count == null) count = 10;
        PlayerInfo playerInfo = playerMapper.selectOne(new LambdaQueryWrapper<PlayerInfo>().eq(PlayerInfo::getUsrName, authentication.getName()));
        if (playerInfo == null) return Result.of(ResultCode.USER_NOT_LOGIN);
        List<MsgRecord> msgRecords = msgRecordMapper.selectList(
                new LambdaQueryWrapper<MsgRecord>()
                        .eq(MsgRecord::getReceiveId, playerInfo.getPlayerId())
                        .eq(MsgRecord::getSenderId, playerId)
                        .eq(MsgRecord::getActivityId, activityId)
                        .last("limit " + page * count + "," + count));
        for (MsgRecord msgRecord : msgRecords) {
            msgRecord.setIsRead(1);
        }
        msgRecordMapper.update(null, new LambdaUpdateWrapper<MsgRecord>()
                .eq(MsgRecord::getReceiveId, playerInfo.getPlayerId())
                .eq(MsgRecord::getSenderId, playerId)
                .eq(MsgRecord::getActivityId, activityId)
                .set(MsgRecord::getIsRead, 1)
                .last("limit " + page * count + "," + count));
        return Result.success(msgRecords);
    }


    @ApiOperation("获取某自己发给某玩家的消息")
    @GetMapping("/send")
    public Result getSendMessage(
            @ApiIgnore Authentication authentication,
            @ApiParam("玩家ID") @RequestParam Integer playerId,
            @ApiParam("第几页,0开始") @RequestParam(required = false) Integer page,
            @ApiParam("每页的数量，默认10") @RequestParam(required = false) Integer count,
            @ApiParam("房间ID,不传入则表示不在房间中") @RequestParam(required = false) Integer activityId
    ) {
        if (page == null) page = 0;
        if (count == null) count = 10;
        PlayerInfo playerInfo = playerMapper.selectOne(new LambdaQueryWrapper<PlayerInfo>().eq(PlayerInfo::getUsrName, authentication.getName()));
        if (playerInfo == null) return Result.of(ResultCode.USER_NOT_LOGIN);
        List<MsgRecord> msgRecords = msgRecordMapper.selectList(
                new LambdaQueryWrapper<MsgRecord>()
                        .eq(MsgRecord::getReceiveId, playerId)
                        .eq(MsgRecord::getSenderId, playerInfo.getPlayerId())
                        .eq(MsgRecord::getActivityId, activityId)
                        .last("limit " + page * count + "," + count));

        return Result.success(msgRecords);
    }

    @Transactional
    @ApiOperation(value = "发布公告", notes = "需要ADMIN权限")
    @PostMapping("/broadcast")
    @PreAuthorize("hasRole('ADMIN')")
    public Result broadcast(
            @ApiIgnore Authentication authentication,
            @ApiParam("公告标题") @RequestParam String title,
            @ApiParam("公告内容") @RequestParam String content,
            @ApiParam("发布时间") @RequestParam String sendTime
    ) {
        PlayerInfo playerInfo = playerMapper.selectOne(new LambdaQueryWrapper<PlayerInfo>().eq(PlayerInfo::getUsrName, authentication.getName()));
        if (playerInfo == null) return Result.of(ResultCode.USER_NOT_LOGIN);
        Date time;
        try {
            time = DateUtil.toDate(sendTime);
        } catch (Exception e) {
            return Result.of(999, "时间格式错误!");
        }
        BroadCast broadCast = new BroadCast();
        broadCast.setPlayerId(playerInfo.getPlayerId());
        broadCast.setTitle(title);
        broadCast.setContent(content);
        broadCast.setSendTime(time);
        broadCastMapper.insert(broadCast);
        return Result.success(broadCast);
    }

    @ApiOperation("获取已发布的公告")
    @GetMapping("/broadcast")
    public Result getBroadcast(
            @ApiParam("第几页,0开始") @RequestParam(required = false) Integer page,
            @ApiParam("每页的数量，默认10") @RequestParam(required = false) Integer count) {
        if (page == null) page = 0;
        if (count == null) count = 10;
        LambdaQueryWrapper<BroadCast> wrapper = new LambdaQueryWrapper<>();
        String s = DateUtil.toString(new Date());
        wrapper.apply("UNIX_TIMESTAMP(send_time) <= UNIX_TIMESTAMP('" + s + "')");
        List<BroadCast> broadCasts = broadCastMapper.selectList(wrapper.last("limit " + page * count + "," + count));
        return Result.success(broadCasts);
    }

    @ApiOperation(value = "获取所有公告(包括未发布的)", notes = "需要ADMIN权限")
    @GetMapping("/allBroadcast")
    @PreAuthorize("hasRole('ADMIN')")
    public Result getAllBroadcast(
            @ApiParam("第几页,0开始") @RequestParam(required = false) Integer page,
            @ApiParam("每页的数量，默认10") @RequestParam(required = false) Integer count) {
        if (page == null) page = 0;
        if (count == null) count = 10;
        List<BroadCast> broadCasts = broadCastMapper.selectList(new LambdaQueryWrapper<BroadCast>().last("limit " + page * count + "," + count));
        return Result.success(broadCasts);
    }

    @Transactional
    @ApiOperation(value = "删除公告", notes = "需要ADMIN权限")
    @DeleteMapping("/broadcast")
    @PreAuthorize("hasRole('ADMIN')")
    public Result delBroadcast(@ApiParam("公告ID") @RequestParam String bcId) {
        broadCastMapper.deleteById(bcId);
        return Result.success();
    }
}
