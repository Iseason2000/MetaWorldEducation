package top.iseason.metaworldeducation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("lab_player")
@ApiModel("参与实验室里的玩家")
public class LabPlayer implements Serializable {

    @ApiModelProperty("ID")
    @TableId(type = IdType.AUTO)
    private Integer lpId;

    @ApiModelProperty("实验室ID")
    private Integer labId;

    @ApiModelProperty("玩家ID")
    private Integer playerId;

}
