package top.iseason.metaworldeducation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@TableName("activity_equipment")
@Accessors(chain = true)
@ApiModel("活动里的器材")
public class ActivityEquipment implements Serializable {

    @ApiModelProperty("活动器材ID")
    @TableId(type = IdType.ASSIGN_ID)
    private Long reId;

    @ApiModelProperty(value = "活动ID", example = "2")
    private Integer activityId;

    @ApiModelProperty(value = "实验桌ID", example = "1")
    private Integer deskId;

    @ApiModelProperty(value = "实验器材ID", example = "1")
    private Integer eId;

    @ApiModelProperty(value = "实验器材子ID", example = "0")
    private Integer subId;

    @ApiModelProperty(value = "玩家ID", example = "1")
    private Integer creatorId;

    @ApiModelProperty(value = "器材x坐标", example = "5.1")
    private Float posX;

    @ApiModelProperty(value = "器材y坐标", example = "10.1")
    private Float posY;

    @ApiModelProperty(value = "器材z坐标", example = "25.1")
    private Float posZ;

    @ApiModelProperty(value = "器材x旋转角", example = "0.4")
    private Float roaX;

    @ApiModelProperty(value = "器材y旋转角", example = "120.4")
    private Float roaY;

    @ApiModelProperty(value = "器材z旋转角", example = "35.51")
    private Float roaZ;

    @ApiModelProperty(value = "器材x缩放", example = "1.0")
    private Float scaleX;

    @ApiModelProperty(value = "器材y缩放", example = "1.0")
    private Float scaleY;

    @ApiModelProperty(value = "器材z缩放", example = "1.0")
    private Float scaleZ;

    @ApiModelProperty("操作的玩家, null表示无人操作")
    private Integer dealingPlayer;

}