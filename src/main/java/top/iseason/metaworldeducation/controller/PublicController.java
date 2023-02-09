package top.iseason.metaworldeducation.controller;

import io.swagger.annotations.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.iseason.metaworldeducation.entity.PlayerInfo;
import top.iseason.metaworldeducation.mapper.PlayerMapper;
import top.iseason.metaworldeducation.util.Result;
import top.iseason.metaworldeducation.util.ResultCode;

import javax.annotation.Resource;
import java.util.Date;

@Api(tags = "公开API，无需登录")
@RestController
@RequestMapping("/public")
public class PublicController {

    @Resource
    PlayerMapper playerMapper;
    @Resource
    PasswordEncoder passwordEncoder;


    @ApiOperation(value = "登录接口",
            extensions = @Extension(properties = @ExtensionProperty(name = "data", value = "[{\"playerId\":2,\"updateTime\":\"2023-02-09 14:22:10\",\"usrName\":\"admin\",\"role\":\"ADMIN\"}]"))
    )
    @PostMapping(value = "/login", produces = "application/json")
    public Result<PlayerInfo> login(
            @ApiParam(value = "用户名", required = true) @RequestParam("usrName") String username,
            @ApiParam(value = "密码", required = true) @RequestParam("usrPwd") String password,
            @ApiParam("是否记住用户") @RequestParam(value = "remember", required = false) String remember
    ) {
        return Result.failure();
    }

    @ApiOperation(value = "注销接口.")
    @PostMapping(value = "/logout", produces = "application/json")
    public Result<Object> logout() {
        return Result.failure();
    }

    @ApiOperation("注册接口.")
    @PostMapping(value = "/register", produces = "application/json")
    public Result<PlayerInfo> register(
            @ApiParam(value = "用户名", required = true) @RequestParam("usrName") String username,
            @ApiParam(value = "密码", required = true) @RequestParam("usrPwd") String password,
            @ApiParam(value = "昵称", required = true) @RequestParam("playerName") String playerName
    ) {
        PlayerInfo playerInfo = new PlayerInfo()
                .setUsrName(username)
                .setUsrPwd(passwordEncoder.encode(password))
                .setPlayerName(playerName);
        try {
            playerMapper.insert(playerInfo);
        } catch (Exception e) {
            return Result.of(ResultCode.USER_ACCOUNT_ALREADY_EXIST);
        }
        playerInfo.setUpdateTime(new Date());
        return Result.of(ResultCode.USER_REGISTER_SUCCESS, playerInfo);
    }
}
