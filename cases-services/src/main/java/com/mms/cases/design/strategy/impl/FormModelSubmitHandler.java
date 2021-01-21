package com.mms.cases.design.strategy.impl;

import com.mms.cases.design.strategy.CommonPairResponse;
import com.mms.cases.design.strategy.FormSubmitHandler;
import com.mms.cases.design.strategy.FormSubmitRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class FormModelSubmitHandler implements FormSubmitHandler<Long> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String getSubmitType() {
        return "model";
    }

    @Override
    public CommonPairResponse handleSubmit(FormSubmitRequest request) {
        logger.info("模型提交：userId={}, formInput={}", request.getUserId(), request.getFormInput());
        // 模型创建成功后获得模型的 id
        Long modelId = createModel(request);
        return CommonPairResponse.success("模型提交成功！", modelId);
    }

    public long createModel(FormSubmitRequest request){
        return 0l;
    }
}
