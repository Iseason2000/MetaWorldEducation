package top.iseason.metaworldeducation.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.web.cors.CorsConfiguration;
import top.iseason.metaworldeducation.entity.PlayerInfo;
import top.iseason.metaworldeducation.mapper.PlayerMapper;
import top.iseason.metaworldeducation.util.Result;
import top.iseason.metaworldeducation.util.ResultCode;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;

@EnableWebSecurity
public class SecurityConfig {

    @Resource
    private PlayerMapper playerMapper;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .formLogin()
                .usernameParameter("usrName")
                .passwordParameter("usrPwd")
                .loginProcessingUrl("/public/login")
                .permitAll()
                .successHandler((request, response, authentication) -> {
                    response.setContentType("text/json;charset=utf-8");
                    PlayerInfo playerInfo = playerMapper.selectOne(new LambdaQueryWrapper<PlayerInfo>().eq(PlayerInfo::getUsrName, authentication.getName()));
                    playerInfo.setUpdateTime(new Date());
                    playerInfo.setUsrPwd(null);
                    response.getWriter().write(Result.of(ResultCode.USER_LOGIN_SUCCESS, playerInfo).toString());
                })
                .failureHandler((request, response, exception) -> {
                    response.setContentType("text/json;charset=utf-8");
                    Result result;
                    if (exception instanceof AccountExpiredException) {
                        //账号过期
                        result = Result.of(ResultCode.USER_ACCOUNT_EXPIRED);
                    } else if (exception instanceof BadCredentialsException) {
                        //密码错误
                        result = Result.of(ResultCode.USER_CREDENTIALS_ERROR);
                    } else if (exception instanceof CredentialsExpiredException) {
                        //密码过期
                        result = Result.of(ResultCode.USER_CREDENTIALS_EXPIRED);
                    } else if (exception instanceof DisabledException) {
                        //账号不可用
                        result = Result.of(ResultCode.USER_ACCOUNT_DISABLE);
                    } else if (exception instanceof LockedException) {
                        //账号锁定
                        result = Result.of(ResultCode.USER_ACCOUNT_LOCKED);
                    } else if (exception instanceof InternalAuthenticationServiceException) {
                        //用户不存在
                        result = Result.of(ResultCode.USER_ACCOUNT_NOT_EXIST);
                    } else {
                        //其他错误
                        result = Result.of(ResultCode.COMMON_FAIL);
                    }
                    response.getWriter().write(result.toString());
                })
                .and()
                .rememberMe()
                .key("remember")
                .and()
                .logout()
                .logoutUrl("/public/logout")
                .permitAll()
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setContentType("text/json;charset=utf-8");
                    response.getWriter().write(Result.of(ResultCode.USER_LOGOUT_SUCCESS).toString());
                })
                .deleteCookies("JSESSIONID")
                .and()
                .userDetailsService(userDetailsService())
                .authorizeRequests()
                .antMatchers("/player/**").hasAnyRole("PLAYER")
                .antMatchers("/room/**").hasAnyRole("PLAYER")
                .antMatchers("/public/**").permitAll()

//                .antMatchers("/doc.html").permitAll()
//                .antMatchers("/webjars/**").permitAll()
//                .anyRequest()
//                .authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new AccessDeniedHandlerImpl())
                .authenticationEntryPoint((request, response, exception) -> {
//                exception.printStackTrace();
                    response.setContentType("text/json;charset=utf-8");
                    response.getWriter().write(Result.of(ResultCode.USER_NOT_LOGIN).toString());
                }).and()
                .csrf().disable()
                .cors(c -> c.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Collections.singletonList("*"));
                    config.setAllowedMethods(Collections.singletonList("*"));
                    return config;
                }));

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            PlayerInfo player = playerMapper.selectOne(new LambdaQueryWrapper<PlayerInfo>().eq(PlayerInfo::getUsrName, username));
            if (player == null) throw new UsernameNotFoundException(username);
            return player.toUser();
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}