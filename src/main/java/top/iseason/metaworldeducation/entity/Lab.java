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
@TableName("lab")
@ApiModel("实验室信息")
public class Lab implements Serializable {
    @ApiModelProperty("实验室ID")
    @TableId(type = IdType.AUTO)
    private Integer labId;

    @ApiModelProperty(value = "学科ID", example = "2")
    private Integer subjectId;

    @ApiModelProperty(value = "创建者ID", example = "1")
    private Integer owner;

    @ApiModelProperty(value = "实验名称", example = "摩擦力实验")
    private String name;

    @ApiModelProperty(value = "玩家数量", example = "12")
    private Integer playerCount = 0;

    @ApiModelProperty(value = "玩家限制", example = "45")
    private Integer playerLimit;

    @ApiModelProperty(value = "创建时间", example = "2023-02-17 12:22:51")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty("是否允许说话")
    private Integer enableVoice;

    @ApiModelProperty("是否允许操作")
    private Integer enableAction;

    @ApiModelProperty(value = "器材列表", example = "1,2,5,15,22,26,24,58,102,156")
    private String equipments;
}