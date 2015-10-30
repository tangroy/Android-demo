package com.oxygen.www.utils;

import java.util.concurrent.ThreadFactory;  
import java.util.concurrent.BlockingQueue;  
import java.util.concurrent.Executor;  
import java.util.concurrent.LinkedBlockingDeque;  
import java.util.concurrent.ThreadPoolExecutor;  
import java.util.concurrent.TimeUnit;  
  
// 线程池的实现方式  
public class MyThreadPool {  
    private final static int POOL_SIZE = 4;// 线程池的大小最好设置成为CUP核数的2N  
    private final static int MAX_POOL_SIZE = 6;// 设置线程池的最大线程数  
    private final static int KEEP_ALIVE_TIME = 4;// 设置线程的存活时间  
    private static Executor mExecutor;  
    private static ThreadFactory factory;
    private static BlockingQueue<Runnable> workQueue;
    public MyThreadPool() {  
        // 创建线程池工厂
    	if(factory == null){
    		factory = new PriorityThreadFactory("thread-pool", android.os.Process.THREAD_PRIORITY_BACKGROUND);  
    	}
        // 创建工作队列  
    	if(workQueue == null){
    		workQueue = new LinkedBlockingDeque<Runnable>();  
    	}
        if(mExecutor == null){
        	mExecutor = new ThreadPoolExecutor(POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue, factory);  
        }
    }  
    // 在线程池中执行线程  
    public void submit(Runnable command){  
    	mExecutor.execute(command);  
    }  
} 