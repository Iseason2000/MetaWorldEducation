package top.iseason.metaworldeducation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("goods_types")
public class GoodsType implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer typeId;
    private String name;
}
