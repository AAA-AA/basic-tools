package com.github.tools.pub;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.LongAdder;

@Slf4j
public class ProgressListener {

    private final long DEFAULT_LOG_DELAY = 10000;

    private long logDelay;

    private volatile String progressName;

    private Timer timer;

    private Date startTime;

    private Date endTime;

    private Map<String, LongAdder> countStore;

    private LongAdder total;

    private Long goal;

    public String getProgressName(){
        return this.progressName;
    }

    public void setProgressName(String progressName){
        this.progressName = progressName;
    }

    public ProgressListener(long goal){
        init(Thread.currentThread().getName(),goal ,DEFAULT_LOG_DELAY);
    }

    public ProgressListener(String progressName, long goal){
        init(progressName, goal,DEFAULT_LOG_DELAY);
    }

    public ProgressListener(String progressName, long goal , long logDelay){
        init(progressName, goal,logDelay);
    }

    private void init(String progressName,long goal, long logDelay){
        this.progressName = progressName;
        this.logDelay = logDelay;
        this.countStore = new LinkedHashMap<>();
        this.total = new LongAdder();
        this.countStore.put("total", total);
        this.goal=goal;
//        this.startLog();
    }

    /**
     * 开始数据计数日志
     */
    public void startLog(){
        StringBuilder builder = new StringBuilder("[PROGRESS-LISTENER]");
        builder.append("|progressName:{}");
        countStore.forEach((k, v) -> builder.append("|").append(k).append(":{}"));
        builder.append("|lost:{}min");
        builder.append("|avg:{}/min");
        builder.append("|fps:{}/min");
        String logTemp = builder.toString();
        ArrayList<Object> logParams = new ArrayList<>();
        logParams.add(progressName);
        logParams.addAll(countStore.values());
        logParams.add(0f);
        logParams.add(0f);
        logParams.add(0f);

        this.startTime = new Date();
        this.timer = new Timer();
        timer.schedule(new TimerTask() {
            private volatile long lastTotal = 0;
            @Override
            public void run() {
                long currentTotal = total.longValue();
                long currentTime = System.currentTimeMillis();
                logParams.set(logParams.size() -3,String.format("%.2f", (currentTime - startTime.getTime()) * 1.00/60000));
                logParams.set(logParams.size() -2, (currentTotal * 60*1000) / (currentTime - startTime.getTime()));
                logParams.set(logParams.size() -1, (currentTotal - lastTotal) * 60*1000 / logDelay);
                log.info(logTemp, logParams.toArray());
                lastTotal = currentTotal;
            }
        }, logDelay, logDelay);
    }

    /**
     * 关闭计数日志
     */
    public void shutDown(){
        Map<String, Long> lastRead = new HashMap<>();
        do {
            this.countStore.forEach((k, v) -> lastRead.put(k, v.sum()));
            try {
                Thread.sleep(logDelay);
            } catch (InterruptedException e) {
                log.error("shutDown error", e);
            }
        } while (!isStopNow(lastRead));
        this.timer.cancel();
        this.timer = null;
        this.endTime = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.info("[PROGRESS-LISTENER]|startTime:{}|endTime:{}|wholeTime:{}min|:{}total",
            dateFormat.format(startTime), dateFormat.format(endTime),
            String.format("%.2f", (endTime.getTime() - startTime.getTime()) * 1.00/60000),
            total.sum());
    }

    /**
     * 判断监听的数据是否已经停止增长
     * @param lastRead
     * @return
     */
    private boolean isStopNow(Map<String, Long> lastRead){
        for (Map.Entry<String, LongAdder> e : this.countStore.entrySet()){
            if (e.getValue().sum() != lastRead.get(e.getKey())){
                return false;
            }
        }
        return true;
    }

    /**
     * 添加一个计数项
     * @param key 计数项名称
     */
    public void addCount(String key){
        if ("total".equalsIgnoreCase(key)) {
            throw new UnsupportedOperationException("total为工具已有属性,不允许用户设置");
        }
        this.countStore.put(key, new LongAdder());
    }

    /**
     * total计数项 +1
     * @return
     */
    public void totalIncrease(){
        this.total.increment();
    }

    /**
     * total计数项 +increase
     * @param increase 增加值
     * @return
     */
    public void totalIncrease(long increase){
        this.total.add(increase);
    }


    /**
     * key 计数项 +1
     * @param key
     * @return
     */
    public void increase(String key){
        this.countStore.get(key).increment();
        this.totalIncrease();
    }

    /**
     * key 计数项 +increase
     * @param key   计数项名称
     * @param increase  增加值
     * @return
     */
    public void increase(String key, long increase){
        this.countStore.get(key).add(increase);
        this.totalIncrease(increase);
    }

}
