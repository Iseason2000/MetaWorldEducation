package top.iseason.metaworldeducation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("activity_player")
@ApiModel("参与房间/活动里的玩家")
public class ActivityPlayer implements Serializable {
    @ApiModelProperty("ID")
    @TableId(type = IdType.AUTO)
    private Integer apId;
    @ApiModelProperty("房间ID")
    private Integer activityId;
    @ApiModelProperty("玩家ID")
    private Integer playerId;
}
