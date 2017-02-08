package com.yy.sdk.upgrade.net;
/**
 * @(#)IRequestHandler.java	
 * <br>A interface that abstract the download actions, such as <code>stop()</code> ,
 * <br><code>start()</code> ,<code>handler()</code>,<code>addListener()</code>.
 * Copyright 2014 广州华多网络科技有限公司, Inc. All rights reserved.
 * @author 陈真   2014/04/28
 * @param <JSonObject>
 */
public interface IRequestHandler<E,N> {
	/**
	 * <p>
	 * <br>  first starting a request to the server and 
	 * <br>initialize some parameters before starting the actual request.
	 * <br>it will initialize parameters in none-ui-thread and then 
	 * <br>throw a requesting to the server after transfer to UI thread.
	 * <br>  当一个任务初次发起请求时，会调用此接口，这里会先在子线程中初始化一些参数和配置信息，
	 * <br>然后才跳转到UI线程中发起下载请求。
	 * </p>
	 * @param e
	 */
	public void handler(E ...e);
	/**
	 * <p>
	 * <br>  it's a way to stop the current executing task.
	 * <br>that will cancel the current running request but not remove.
	 * <br>  如果当前任务正在执行，可以通过此方法暂停取消下载任务，但不会把当前任务从队列里移除，
	 * <br>再次发起请求，是可以通过onResume唤醒的。
	 * </p>
	 */
	public void pause();
	/**
	 * <p>
	 * <br>  while the current request was paused, then we can call <code>resume()</code>
	 * <br>to wake up the request.
	 * <br>  如果当前下载任务已被暂停，可以通过此方法重新发起下载请求。
	 * <p>
	 */
	public boolean resume();
	/**
	 * <p>
	 * <br>  discard all requests, thread pool will cancel and remove
	 * <br>all requests,if you want to restart download file,you should
	 * <br>reconstruct your <code>IRequestHandler</code>
	 * </p>
	 */
	public void discard();
	/**
	 * it's a way to release resources.
	 */
	public void release();
}
