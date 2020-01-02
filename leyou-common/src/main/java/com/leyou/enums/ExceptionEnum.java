package com.leyou.enums;

import lombok.Getter;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/15 23:04
 * @description:
 */
@Getter
public enum ExceptionEnum {

    PRICE_CANNOT_BE_NULL(400, "价钱不能为空啊"),
    CATEGORY_FINDALL_BE_NULL(204, "查询Category表为null"),
    BRAND_FINDPAGE_BE_NULL(204, "查询Brand页面数据为null"),
    BRAND_ADD_FAIL(500, "品牌数据插入异常"),
    BRANDANDCATEGORY_ADD_FAIL(500, "品牌和商品的中间表插入数据异常"),
    IMAGE_TYPE_DISQUALIFIED(500, "前端提供的图片格式不符合要求"),
    IMAGE_CONTENT_BE_NULL(400, "前端提供的图片内容为空"),
    FILE_UPLOAD_ERROR(500,"图片上传失败"),
    FIND_SPUDETAIL_BY_SPUID_FAILED(204,"根据spuId查询spudetail失败"),
    UPDATE_OPERATION_FAIL(500,"更新获取Signature操作失败"),
    UPDATE_SALEABLE_FAIL(500,"更新上架状态失败"),
    UPDATE_BRAND_FAIL(500,"更新品牌数据失败"),
    CATEGORY_NOT_FOUND(204,"根据Ids查询商品类目没有结果"),
    INSERT_OPERATION_FAIL(500,"更新品牌和商品的中间表插入数据异常"),
    BRAND_NOT_FOUND(204,"商品规格组查询失败"),
    BRAND_SELECT_FAIL(500,"商品规格组查询异常，检查数据库或者对应表字段"),
    PARAM_SELECT_FAIL(500,"根据商品规格组Id查询异常,数据库关键字不可当作表字段使用"),
    PARAM_FIND_FILED(204,"根据规格组Id查询参数为空"),
    MYSQL_SERVER_ERROR(500,"数据库连接异常"),
    MYSQL_PWD_ERROR(500,"数据库密码错误"),
    MYSQL_ERROR(204,"数据库查询失败"),
    MYSQL_SYNTAX_ERROR(500,"数据库关键字不可当成字段名使用"),
    GOODS_NOT_FOUND(204,"商品没有找到"),
    BRAND_FIND_FAILED(204,"根据categoryId查询品牌失败"),
    FIND_SKUS_BY_SPUID_FAILED(204,"根据spuId查询skus数据为空"),
    INVALID_PARAM_ERROR(402,"请求参数有误"),
    INVALID_REQUEST_PARAM(402,"无效的请求参数"),
    DATA_NOT_FOUND(204,"没有查询到数据"),
    DIRECTORY_WRITER_ERROR(500,"【静态页服务】创建静态页目录失败"),
    FILE_WRITER_ERROR(500,"静态页服务】静态页生成失败"),
    DATA_NOT_FILED(500,"数据查询失败"),
    DATA_TRANSFER_ERROR(500, "数据转换异常");

    private int status;
    private String message;


    ExceptionEnum(int status, String message) {
        this.status = status;
        this.message = message;
    }

}
