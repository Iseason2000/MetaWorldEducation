package top.iseason.metaworldeducation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@TableName("equipment_preset")
@ApiModel("实验预设")
public class EquipmentPreset implements Serializable {

    @ApiModelProperty(value = "预设ID", example = "1")
    @TableId(type = IdType.AUTO)
    private Integer epId;

    @ApiModelProperty(value = "预设名称", example = "重力实验")
    private String name;

    @ApiModelProperty(value = "实验步骤")
    private String steps;

    @ApiModelProperty(value = "实验原理")
    private String theory;

    @ApiModelProperty(value = "预设上传时间", example = "2023-02-07 16:45:26")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonIgnore
    @TableField("equipments")
    @ApiModelProperty(hidden = true)
    private String equipmentsField;

    @ApiModelProperty(value = "预设文件名称", example = "1.xxx")
    private String fileName;

    @TableField(exist = false)
    @ApiModelProperty(value = "预设器材")
    private List<Integer> equipments;

    public List<Integer> getEquipments() {
        return Arrays.stream(equipmentsField.split(",")).map(Integer::parseInt).collect(Collectors.toList());
    }

}