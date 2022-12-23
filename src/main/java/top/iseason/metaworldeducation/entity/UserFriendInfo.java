package top.iseason.metaworldeducation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@TableName("user_friend_info")
public class UserFriendInfo implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer ufId;
    private Integer userId;
    private Integer friendId;

}
