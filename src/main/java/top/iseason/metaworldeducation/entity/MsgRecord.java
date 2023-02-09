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
@TableName("msg_record")
@ApiModel("消息记录")
public class MsgRecord implements Serializable {

    @ApiModelProperty("消息ID")
    @TableId(type = IdType.AUTO)
    private Integer msgId;

    @ApiModelProperty("发送者ID")
    private Integer senderId;

    @ApiModelProperty("接收者ID")
    private Integer receiveId;

    @ApiModelProperty("场景ID")
    private Integer sceneId;

    @ApiModelProperty("房间/活动ID")
    private Integer activityId;

    @ApiModelProperty(value = "发送时间", example = "2023-02-17 12:22:51")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "消息内容", example = "在吗?")
    private String msg;

    @ApiModelProperty("是否已读")
    private Integer isRead = 0;

}
