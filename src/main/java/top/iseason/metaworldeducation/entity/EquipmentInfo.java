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
@TableName("equipment_info")
@ApiModel("实验器材")
public class EquipmentInfo implements Serializable {

    @ApiModelProperty(value = "器材ID", example = "2")
    @TableId(type = IdType.AUTO)
    private Integer eId;

    @ApiModelProperty(value = "器材名称", example = "酒精灯")
    private String name;

    @ApiModelProperty(value = "器材预设名称")
    private String perfabName;

    @ApiModelProperty(value = "缩略图文件名称")
    private String thumbnailName;

    @ApiModelProperty(value = "AssetBundle文件名称")
    private String assetName;

    @ApiModelProperty(value = "源文件文件名称")
    private String sourceName;

    @ApiModelProperty(value = "器材上传时间", example = "2023-02-07 16:45:26")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}