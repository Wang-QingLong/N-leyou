package com.leyou.advice;

import com.leyou.exception.LyException;
import com.leyou.result.ExceptionResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/15 22:34
 * @description:
 */
@ControllerAdvice  //controller中的通知器，只要controller有异常，这个业务就会自动生效，相当于在适配的controller中添加try
@Slf4j
public class BasicExceptionAdvice {

    @ExceptionHandler(LyException.class)  //异常处理器  等价于catch
    public ResponseEntity<ExceptionResult> handleException(LyException e) {
             //ResponseEntity中会返回2个值，一个是状态码，另外一个是消息主体
        return ResponseEntity.status(e.getStatus()).body(new ExceptionResult(e));
    }

}
