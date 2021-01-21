package com.mms.cases.design.strategy.impl;

import com.mms.cases.design.strategy.CommonPairResponse;
import com.mms.cases.design.strategy.FormSubmitHandler;
import com.mms.cases.design.strategy.FormSubmitRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class FormPreviewSubmitHandler implements FormSubmitHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public String getSubmitType() {
        return "preview";
    }

    public CommonPairResponse handleSubmit(FormSubmitRequest request) {
        logger.info("预览模式提交：userId={}, formInput={}", request.getUserId(), request.getFormInput());
        return CommonPairResponse.success("预览模式提交数据成功！", null);
    }
}
