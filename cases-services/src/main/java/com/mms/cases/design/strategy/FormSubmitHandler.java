package com.mms.cases.design.strategy;

import java.io.Serializable;

/**
 * 策略模式
 *
 * 表单提交处理器
 * @param <R>
 */
public interface FormSubmitHandler<R extends Serializable> {
    /**
     * 表单提交类型
     * @return
     */
    String getSubmitType();

    /**
     * 处理表单提交请求
     *
     * @param request 请求
     * @return 响应，left 为返回给前端的提示信息，right 为业务值
     */
    CommonPairResponse<String,R> handleSubmit(FormSubmitRequest request);


}
