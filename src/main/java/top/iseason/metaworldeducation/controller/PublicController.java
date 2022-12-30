package top.iseason.metaworldeducation.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

    @ApiOperation("登录接口.")
    @PostMapping("/login")
    public Result login(
            @ApiParam("用户名") @RequestParam("usrName") String username,
            @ApiParam("密码") @RequestParam("usrPwd") String password,
            @ApiParam("是否记住用户") @RequestParam(value = "remember", required = false) String remember
    ) {
        return Result.failure();
    }

    @ApiOperation("注册接口.")
    @PostMapping("/register")
    public Result register(
            @ApiParam("用户名") @RequestParam("usrName") String username,
            @ApiParam("密码") @RequestParam("usrPwd") String password,
            @ApiParam("昵称") @RequestParam("playerName") String playerName
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
