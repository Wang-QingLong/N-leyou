package com.leyou.result;


import com.leyou.exception.LyException;
import lombok.Getter;
import org.joda.time.DateTime;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/15 23:29
 * @description: 异常返回结果类
 */
@Getter
public class ExceptionResult {
    private int status;       //状态码
    private String message;   //消息体
    private String timestamp;  //时间戳

    public ExceptionResult(LyException e) {
        this.status = e.getStatus();
        this.message = e.getMessage();
        this.timestamp = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
    }
}
