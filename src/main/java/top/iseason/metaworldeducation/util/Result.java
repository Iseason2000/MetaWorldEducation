package top.iseason.metaworldeducation.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * API结果集
 */
@ApiModel(description = "响应封装类")
@Data
@Accessors(chain = true)
public class Result {
    private final static ObjectMapper objectMapper;

    static {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper = mapper;
    }

    /**
     * 状态码
     */
    @ApiModelProperty("状态码")
    private Integer state;
    @ApiModelProperty("状态信息")
    private String message;
    /**
     * 数据 (如果有的话)
     */
    @ApiModelProperty("返回数据(可能不存在)")
    private Object data;

    public static Result success(Object... data) {
        return new Result()
                .setState(ResultCode.SUCCESS.getCode())
                .setMessage(ResultCode.SUCCESS.getMessage())
                .setData(data);
    }

    public static Result success(Object data) {
        return new Result()
                .setState(ResultCode.SUCCESS.getCode())
                .setMessage(ResultCode.SUCCESS.getMessage())
                .setData(data);
    }

    public static Result success() {
        return new Result().setState(ResultCode.SUCCESS.getCode())
                .setMessage(ResultCode.SUCCESS.getMessage());
    }

    public static Result failure(Object... data) {
        return new Result()
                .setState(ResultCode.COMMON_FAIL.getCode())
                .setMessage(ResultCode.COMMON_FAIL.getMessage())
                .setData(data);
    }

    public static Result failure(Object data) {
        return new Result()
                .setState(ResultCode.COMMON_FAIL.getCode())
                .setMessage(ResultCode.COMMON_FAIL.getMessage())
                .setData(data);
    }

    public static Result failure() {
        return new Result()
                .setState(ResultCode.COMMON_FAIL.getCode())
                .setMessage(ResultCode.COMMON_FAIL.getMessage());
    }

    public static Result of(ResultCode code, Object... data) {
        return new Result()
                .setState(code.getCode())
                .setMessage(code.getMessage())
                .setData(data);
    }

    public static Result of(ResultCode code) {
        return new Result()
                .setState(code.getCode())
                .setMessage(code.getMessage());
    }

    public static Result of(Integer code, String msg) {
        return new Result()
                .setState(code)
                .setMessage(msg);
    }

    @Override
    public String toString() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

}