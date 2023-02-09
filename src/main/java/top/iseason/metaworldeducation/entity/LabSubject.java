package top.iseason.metaworldeducation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("lab_subject")
@ApiModel("实验学科")
public class LabSubject implements Serializable {

    @ApiModelProperty("学科ID")
    @TableId(type = IdType.AUTO)
    private Integer subjectId;

    @ApiModelProperty(value = "学科名称", example = "物理")
    private String name;
}
