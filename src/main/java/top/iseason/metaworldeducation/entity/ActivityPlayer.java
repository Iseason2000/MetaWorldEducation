package top.iseason.metaworldeducation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("activity_player")
public class ActivityPlayer implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer apId;
    private Integer activityId;
    private Integer playerId;
}
