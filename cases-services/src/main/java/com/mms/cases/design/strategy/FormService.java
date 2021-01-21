package com.mms.cases.design.strategy;

import org.springframework.lang.NonNull;

import java.io.Serializable;

public interface FormService {

    CommonPairResponse<String, Serializable> submitForm(@NonNull FormSubmitRequest request);

}
