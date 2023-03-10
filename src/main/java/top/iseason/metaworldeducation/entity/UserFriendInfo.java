package top.iseason.metaworldeducation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@TableName("user_friend_info")
@ApiModel("好友记录")
public class UserFriendInfo implements Serializable {

    @ApiModelProperty("记录ID")
    @TableId(type = IdType.AUTO)
    private Integer ufId;

    @ApiModelProperty("玩家ID")
    private Integer userId;

    @ApiModelProperty("朋友ID")
    private Integer friendId;

}
