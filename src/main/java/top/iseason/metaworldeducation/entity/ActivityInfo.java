package top.iseason.metaworldeducation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("activity_info")
@ApiModel("房间/活动信息")
public class ActivityInfo implements Serializable {

    @ApiModelProperty("房间ID")
    @TableId(type = IdType.AUTO)
    private Integer activityId;

    @ApiModelProperty("玩家ID")
    private Integer playerId;

    @ApiModelProperty(value = "玩家名称", example = "张三")
    private String playerName;

    @ApiModelProperty("场景ID")
    private Integer sceneId;

    @ApiModelProperty(value = "房间名称", example = "测试房间")
    private String activityName;

    @ApiModelProperty(value = "房间创建日期", example = "2023-01-02 14:26:38")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "活动开始日期", example = "2023-01-07 14:26:38")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "活动结束日期", example = "2023-01-14 14:26:38")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty("房间类型")
    private Integer activityType;

    @ApiModelProperty("房间可见性")
    private Integer activityPermission;

    @ApiModelProperty(value = "房间密码", example = "123456")
    private String activityPassword;

    @ApiModelProperty("房间人数")
    private Integer currentPlayer = 0;

    @ApiModelProperty(value = "房间人数限制", example = "40")
    private Integer maxPlayer;
}
