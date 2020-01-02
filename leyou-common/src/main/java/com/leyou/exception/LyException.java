package com.leyou.exception;

import com.leyou.enums.ExceptionEnum;
import lombok.Getter;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/15 23:16
 * @description:
 */
@Getter
public class LyException extends RuntimeException {

    private int status;

    public LyException(ExceptionEnum em) {
        super(em.getMessage());
        this.status = em.getStatus();
    }

    public LyException(ExceptionEnum em, Throwable cause) {
        super(em.getMessage(), cause);
        this.status = em.getStatus();
    }
}