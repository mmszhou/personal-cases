package com.mms.cases.design.strategy.impl;

import com.mms.cases.design.strategy.CommonPairResponse;
import com.mms.cases.design.strategy.FormSubmitHandler;
import com.mms.cases.design.strategy.FormSubmitRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class FormHsfSubmitHandler implements FormSubmitHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public String getSubmitType() {
        return "hsf";
    }

    public CommonPairResponse handleSubmit(FormSubmitRequest request) {
        logger.info("HSF 模式提交：userId={}, formInput={}", request.getUserId(), request.getFormInput());
        return CommonPairResponse.success("hsf！", null);
    }
}
