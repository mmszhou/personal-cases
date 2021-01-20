package com.mms.cases.retry;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * 如何正确的重试 伪代码
 * /需要有终止条件  “任何锲而不舍的坚持，都需要向着现实低头”
 */
public class retryDemo {

    //方法1 - 重试次数限制
    //存在问题，不带backoff的重试，对下游来说，失败时，请求压力加大，继而进一步恶化。
    public static <T> T retryNoDelay(final Callable<T> callable, final int maxAttempts) {
        //样本1
        for (int i = 0; i < maxAttempts; i++) {
            try {
                final T t = callable.call();
                if (isExpected(t)) {
                    return t;
                }
            } catch (Exception e) {
                //logging
            }
        }


        //样本2
  /*  for (int i = 0; i < configBean.getInsertMessageInboxRetry(); i++) {
        try {
            insertMessageInboxResult = messageInboxManager.insertManager(messageInbox);
            if (insertMessageInboxResult) {
                break;
            }
        } catch (Exception e) {
            //logging
        }
    }*/
        return null;
    }


    //伪代码 判断是否符合预期
    public static <T> Boolean isExpected(T t) {
        return true;
    }

    // 方法2-样本1 带delay的方式
    //按照方法本身是异步的，还是同步的，通过定时器或者简单的thread.sleep实现
    public static <T> T retryWithFixedDelay(final Callable<T> callable, final int maxAttempts, final int fixedBackOff) {

        //样本1
        for (int i = 0; i < maxAttempts; i++) {
            try {
                final T t = callable.call();
                if (isExpected(t)) {
                    return t;
                }
            } catch (Exception e) {
                try {
                    Thread.sleep(fixedBackOff);//延迟重试
                } catch (InterruptedException e1) {
                    //logging
                }
            }
        }

        return null;
    }

    // 方法2-样本2 带delay的方式
    private <T> T retry(Supplier<T> func) {
        int invokecnt = 0;//执行次数
        int maxRetrys = 10; //最大重试次数
        long sleepTime = 0l;//睡眠时间
        long min_sleep_time = 0l;
        long max_sleep_time = 0l;
        Exception ex = null;
        while (invokecnt < maxRetrys) {
            try {
                if (sleepTime > min_sleep_time && sleepTime < max_sleep_time) {
                    Thread.sleep(sleepTime);
                }

                return func.get();
            } catch (InterruptedException e) {
                ex = e;
                invokecnt++;
                if (invokecnt >= maxRetrys) break;
            }

        }

        throw new RuntimeException(ex);
    }
}