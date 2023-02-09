package top.iseason.metaworldeducation.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import top.iseason.metaworldeducation.util.Result;

@Slf4j
@ControllerAdvice(basePackages = "")
public class ExceptionAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    Result<String> handleControllerException(Throwable ex) {
//        log.info("异常", ex);
        return Result.failure(ex.getMessage());
    }

}