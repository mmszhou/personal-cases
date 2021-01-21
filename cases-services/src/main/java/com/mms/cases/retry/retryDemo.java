package com.mms.cases.retry;

import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
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

    //方法3 带随机delay方式
    // 具体delay的时间，在一个最小和最大值之间浮动
    public static <T> T retryWithRandomDelay(final Callable<T> callable,
                                             final int maxAttempts,
                                             final int minBackoff,
                                             final int maxBackoff,
                                             // 0.0-1.0
                                             final double randomFactor) {

        //样版1
        for (int i = 0; i < maxAttempts; i++) {
            try {
                final T t = callable.call();
                if (isExpected(t)) {
                    return t;
                }
            } catch (Exception e) {
                try {
                    final double rnd = 1.0 + ThreadLocalRandom.current().nextDouble() * randomFactor;
                    long backoffTime;
                    try {
                        backoffTime = (long) (Math.min(maxBackoff, minBackoff * Math.pow(2, i)) * rnd);
                    } catch (Exception e1) {
                        backoffTime = maxBackoff;
                    }
                    Thread.sleep(backoffTime);//延迟重试
                } catch (InterruptedException e1) {
                    //logging
                }
            }
        }

        return null;
    }

    //方法3 - 样本2 带随机delay方式
    public static <T> T retryWithBackoff(final Callable<T> callable,
                                         final int maxRetryTime,
                                         final int sleepInMills) {

        if (maxRetryTime >= 10) {
            return null;
        }

        if (sleepInMills <= 0) {
            return null;
        }

        try {
            return callable.call();
        } catch (Exception e) {
            try {
                Thread.sleep(sleepInMills);
                return retryWithBackoff(callable, maxRetryTime - 1,
                        (sleepInMills * (ThreadLocalRandom.current().nextInt(1, 3))));
            } catch (InterruptedException e1) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e1);
            }

        }

    }

    //方法三 解决了backoff(回退)时间集中的问题（对时间进行打散）但依然存在问题；
    // 1.依赖的底层持续失败，调用方依然会进行固定次数的尝试，并不能起到很好的保护作用；
    // 2.对结果是否符合预期，是否需要重试依赖于异常；
    // 3.无法针对异常进行精细化管理

    //细粒度控制的重试

    public static <T> T retryWithRandomDelayAndCondition(final Callable<T> callable,
                                                         final int maxRetryTime,
                                                         final int minBackoff,
                                                         final int maxBackoff,
                                                         final double randomFactor,
                                                         final Predicate<Throwable> retryPredicate) {
        for (int i = 0; i < maxRetryTime; i++) {
            try {
                final T t = callable.call();
                if (isExpected(t)) {
                    return t;
                }
            } catch (Exception e) {
                if (retryPredicate.test(e)) {
                    break;
                }

                try {
                    final double rnd = 1.0 + ThreadLocalRandom.current().nextDouble() * randomFactor;
                    long backoffTime;
                    try {
                        backoffTime = (long) (Math.min(maxBackoff, minBackoff * Math.pow(2, i)) * rnd);
                    } catch (Exception e1) {
                        backoffTime = maxBackoff;
                    }
                    Thread.sleep(backoffTime);//延迟重试
                } catch (InterruptedException e1) {
                    //logging
                }
            }
        }

        return null;
    }


    public static void main(String[] args) {
        Predicate<Throwable> predicate= (e)->{
            if(e instanceof  Exception){
                return true;
            }
            return false;
        };

        try {
            test();
        } catch (Exception e) {
            System.out.println(predicate.test(e));
        }

        new Predicate<String>(){
            @Override
            public boolean test(String s) {
                return false;
            }
        };
    }

    public static void test() throws Exception{
        throw new Exception("TEST");
    }

}