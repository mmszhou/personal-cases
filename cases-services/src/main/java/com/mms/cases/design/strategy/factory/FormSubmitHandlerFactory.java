package com.mms.cases.design.strategy.factory;

import com.mms.cases.design.strategy.FormSubmitHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 策略的简单工厂
 */
@Component
public class FormSubmitHandlerFactory implements InitializingBean, ApplicationContextAware {

    private static final Map<String, FormSubmitHandler<Serializable>> FORM_SUBMIT_HANDLER_MAP
            = new HashMap<>(8);

    private ApplicationContext applicationContext;

    /**
     * 根据提交类型获取对应的处理器
     *
     * @param submitType 提交类型
     * @return 提交类型对应的处理器
     */
    public FormSubmitHandler<Serializable> getHandler(String submitType) {
        return FORM_SUBMIT_HANDLER_MAP.get(submitType);
    }

    //实现InitializingBean 接口
    //基于 Spring 容器将所有 FormSubmitHandler 自动注册到 FORM_SUBMIT_HANDLER_MAP
    @Override
    public void afterPropertiesSet() throws Exception {
        // 将 Spring 容器中所有的 FormSubmitHandler 注册到 FORM_SUBMIT_HANDLER_MAP
        applicationContext.getBeansOfType(FormSubmitHandler.class)
                .values()
                .forEach(handler -> FORM_SUBMIT_HANDLER_MAP.put(handler.getSubmitType(), handler));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        applicationContext = applicationContext;
    }
}
