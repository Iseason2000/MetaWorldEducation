package top.iseason.metaworldeducation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

@Data
@Accessors(chain = true)
@TableName("player_info")
public class PlayerInfo implements Serializable {

    @ApiModelProperty("用户id")
    @TableId(type = IdType.AUTO)
    private Integer playerId;

    @ApiModelProperty("用户名称")
    private String playerName;

    @ApiModelProperty("玩家最近一次发送心跳包的时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty("玩家位置X坐标")
    private Float posX;

    @ApiModelProperty("玩家位置Y坐标")
    private Float posY;

    @ApiModelProperty("玩家位置Z坐标")
    private Float posZ;

    @ApiModelProperty("玩家角度X角")
    private Float roaX;

    @ApiModelProperty("玩家角度Y角")
    private Float roaY;

    @ApiModelProperty("玩家角度Z角")
    private Float roaZ;

    @ApiModelProperty("玩家移动速度")
    private Float moveSpeed;

    @ApiModelProperty("玩家移动速度")
    private Float rotateSpeed;

    @ApiModelProperty("玩家是否正在走路，0:不是，1：是")
    private Integer isRunning;

    @ApiModelProperty("玩家是否正在讲话，0:不是，1：是")
    private Integer isTalking;

    @ApiModelProperty("玩家讲话内容")
    private String talkMsg;

    @ApiModelProperty("登录账户")
    private String usrName;

    @ApiModelProperty("登录密码")
    private String usrPwd;

    @ApiModelProperty("用户当前在哪个场景，广工沙河校区、广工东风路校区、广工大学城校区等等")
    private Integer sceneId;

    @ApiModelProperty("用户当前在参加哪个活动，只有进入相同场景相同活动的用户才能互相看到对方的虚拟人形象")
    private Integer activityId;

    @ApiModelProperty("0、1、2、3、4分别代表女学生、男学生、职业女性、职业男性4个。")
    private Integer identityName;

    @ApiModelProperty("发型")
    private Integer hairType;

    @ApiModelProperty("脸型")
    private Integer faceType;

    @ApiModelProperty("头发颜色")
    private Integer hairColor;

    @ApiModelProperty("头发颜色")
    private Integer faceColor;

    @ApiModelProperty("爱好")
    private String hobby;

    @ApiModelProperty("性格")
    private String disposition;

    @ApiModelProperty("爱好")
    private String occupation;

    @ApiModelProperty("故事")
    private String story;

    @ApiModelProperty("上衣选择哪一件")
    private Integer blouseIndex;

    @ApiModelProperty("裤子选择哪一件")
    private Integer trousersIndex;

    @ApiModelProperty("鞋子选择哪一件")
    private Integer shoeIndex;

    @ApiModelProperty("权限")
    private String role = "PLAYER";

    public User toUser() {
        return new User(getUsrName(), getUsrPwd(), Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())));
    }

    @Override
    public int hashCode() {
        return playerId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof PlayerInfo)) return false;
        return Objects.equals(this.getPlayerId(), ((PlayerInfo) obj).getPlayerId());
    }

}
