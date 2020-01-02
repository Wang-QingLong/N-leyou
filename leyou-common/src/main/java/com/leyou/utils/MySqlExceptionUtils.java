package com.leyou.utils;

import com.leyou.enums.ExceptionEnum;
import com.leyou.exception.LyException;
import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.CannotGetJdbcConnectionException;

import java.sql.SQLException;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/21 19:35
 * @description: 自定义mysql通用几种异常处理机制
 */
@Slf4j
public class MySqlExceptionUtils {



    public static void  CheckMySqlException( Exception e){

        Throwable cause = e.getCause();
        if (cause instanceof CannotGetJdbcConnectionException) {
            log.error(ExceptionEnum.MYSQL_SERVER_ERROR.getMessage());
            throw new LyException(ExceptionEnum.MYSQL_SERVER_ERROR);
        } else if (cause instanceof MySQLSyntaxErrorException) {
            log.error(ExceptionEnum.MYSQL_SYNTAX_ERROR.getMessage());
            throw new LyException(ExceptionEnum.MYSQL_SYNTAX_ERROR);
        } else if (cause instanceof SQLException) {
            log.error(ExceptionEnum.MYSQL_PWD_ERROR.getMessage());
            throw new LyException(ExceptionEnum.MYSQL_PWD_ERROR);
        } else {
            log.error(ExceptionEnum.MYSQL_ERROR.getMessage());
            throw new LyException(ExceptionEnum.MYSQL_ERROR);
        }
    }
}
