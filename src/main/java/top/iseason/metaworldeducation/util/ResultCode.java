package top.iseason.metaworldeducation.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ResultCode {
    /* 成功 */
    SUCCESS(200, "请求成功"),

    /* 默认失败 */
    COMMON_FAIL(999, "请求失败"),

    /* 参数错误：1000～1999 */
    PARAM_NOT_VALID(1001, "参数无效"),
    PARAM_IS_BLANK(1002, "参数为空"),
    PARAM_TYPE_ERROR(1003, "参数类型错误"),
    PARAM_NOT_COMPLETE(1004, "参数缺失"),

    /* 用户错误 */
    USER_LOGIN_SUCCESS(2100, "登录成功"),
    USER_LOGOUT_SUCCESS(2101, "退出成功"),
    USER_REGISTER_SUCCESS(2102, "注册成功"),
    USER_NOT_LOGIN(2001, "用户未登录"),
    USER_ACCOUNT_EXPIRED(2002, "账号已过期"),
    USER_CREDENTIALS_ERROR(2003, "用户名或密码错误"),
    USER_CREDENTIALS_EXPIRED(2004, "密码过期"),
    USER_ACCOUNT_DISABLE(2005, "账号不可用"),
    USER_ACCOUNT_LOCKED(2006, "账号被锁定"),
    USER_ACCOUNT_NOT_EXIST(2007, "账号不存在"),
    USER_ACCOUNT_ALREADY_EXIST(2008, "账号已存在"),
    USER_ACCOUNT_USE_BY_OTHERS(2009, "账号下线"),
    USER_NOT_EXIST(2201, "用户ID不存在"),

    ROOM_NOT_EXIST(2301, "房间不存在"),
    ROOM_ALREADY_JOINED(2302, "你已在房间中，无法加入另一个房间"),
    ROOM_NOT_JOINED(2303, "你不在房间中"),
    ROOM_IS_FULL(2304, "房间已满"),
    ROOM_PASSWORD_ERROR(2305, "房间密码错误"),

    /* 业务错误 */
    NO_PERMISSION(9001, "没有权限");

    /**
     * 状态码
     */
    @Getter
    @Setter
    private Integer code;

    /**
     * 状态描述
     */
    @Getter
    @Setter
    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 根据code获取message
     *
     * @param code
     * @return
     */
    public static String getMessageByCode(Integer code) {
        for (ResultCode ele : values()) {
            if (ele.getCode().equals(code)) {
                return ele.getMessage();
            }
        }
        return null;
    }

}