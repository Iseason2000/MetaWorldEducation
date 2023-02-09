package top.iseason.metaworldeducation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("lab_equipment")
@ApiModel("实验器材")
public class LabEquipment implements Serializable {

    @ApiModelProperty(value = "器材ID", example = "2")
    @TableId(type = IdType.AUTO)
    private Integer eqId;

    @ApiModelProperty(value = "器材名称", example = "酒精灯")
    private String name;

    @ApiModelProperty(value = "器材类型", example = "火具")
    private String type;

    @ApiModelProperty(value = "器材图片的MD5值", example = "4ede89328159a94a952783524fb8f945")
    private String md5;

}