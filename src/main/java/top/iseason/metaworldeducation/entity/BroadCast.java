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
@TableName("broadcast")
@ApiModel("公告")
public class BroadCast implements Serializable {
    @ApiModelProperty("公告ID")
    @TableId(type = IdType.AUTO)
    private Integer bcId;

    @ApiModelProperty("发布者ID")
    private Integer playerId;

    @ApiModelProperty(value = "标题", example = "重要通知")
    private String title;

    @ApiModelProperty(value = "内容", example = "寒假要结束了!")
    private String content;

    @ApiModelProperty(value = "发布时间", example = "2023-02-17 17:12:26")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sendTime;
}
