package com.piesat.site.datasearch.service.constant;

/**
 * 通用常量信息
 */
public class Constants {

    /**
     * 订单待审核
     */
    public static final String ORDER_CREATE = "0";

    /**
     * 订单审核通过
     */
    public static final String ORDER_SUCCESS = "1";

    /**
     * 订单审核驳回
     */
    public static final String ORDER_FAIL = "2";

    public final static String UNIQUE = "0";

    public final static String NOT_UNIQUE = "1";

    /**
     * 文件待生成
     */
    public final static String FILE_CREATE = "0";

    /**
     * 文件生成成功
     */
    public final static String FILE_SUCCESS = "1";

    /**
     * 文件生成失败
     */
    public final static String FILE_FAIL = "2";

    /**
     * 标识符申请（待申请）
     */
    public final static String OID_CREATE = "0";

    /**
     * 标识符申请（申请中）
     */
    public final static String OID_CREATEING = "1";

    /**
     * 标识符申请（申请成功）
     */
    public final static String OID_SUCCESS = "2";

    /**
     * 标识符申请（申请失败）
     */
    public final static String OID_FAIL = "3";

    /**
     * 历史同步
     */
    public final static String SYNC_HISTORY = "history";

    /**
     * 实时同步
     */
    public final static String SYNC_ACTUAL = "actual";

    /**
     *
     * 同步时间类型（小时）
     */
    public final static String UNIT_MINUTE = "minute";

    /**
     *
     * 同步时间类型（小时）
     */
    public final static String UNIT_HOUR = "hour";

    /**
     * 同步时间类型（天）
     */
    public final static String UNIT_DAY = "day";

    /**
     * 检索定制
     */
    public final static String BUTTON_SEARCH = "searchCode";

    /**
     * TDS
     */
    public final static String BUTTON_TDS = "tdsCode";

    /**
     * 文件类型
     */
    public final static String RESOURCE_TYPE = "file";
}
