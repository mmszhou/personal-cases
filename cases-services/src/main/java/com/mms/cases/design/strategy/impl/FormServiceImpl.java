package com.mms.cases.design.strategy.impl;

import com.mms.cases.design.strategy.CommonPairResponse;
import com.mms.cases.design.strategy.FormService;
import com.mms.cases.design.strategy.FormSubmitHandler;
import com.mms.cases.design.strategy.FormSubmitRequest;
import com.mms.cases.design.strategy.factory.FormSubmitHandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class FormServiceImpl implements FormService {

    @Autowired
    private FormSubmitHandlerFactory submitHandlerFactory;

    @Override
    public CommonPairResponse<String, Serializable> submitForm(FormSubmitRequest request) {
        String submitType = request.getSubmitType();
        // 根据 submitType 找到对应的提交处理器
        FormSubmitHandler<Serializable> submitHandler =
                submitHandlerFactory.getHandler(submitType);


        // 判断 submitType 对应的 handler 是否存在
        if (submitHandler == null) {
            return CommonPairResponse.failure("非法的提交类型: " + submitType);
        }

        return submitHandler.handleSubmit(request);
    }
}
