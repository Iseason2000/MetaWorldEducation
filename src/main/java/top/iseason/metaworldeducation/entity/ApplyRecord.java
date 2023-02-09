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
@TableName("apply_record")
@ApiModel("好友请求记录")
public class ApplyRecord implements Serializable {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("ID")
    private Integer applyId;

    @ApiModelProperty(value = "邀请者", example = "0")
    private Integer applyer;

    @ApiModelProperty(value = "被邀请者", example = "5")
    private Integer receiver;

    @ApiModelProperty(value = "请求发送时间", example = "2023-02-07 16:45:26")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;

    @ApiModelProperty(value = "邀请结果", example = "0")
    private Integer result = 0;
}
