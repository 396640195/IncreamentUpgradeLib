package com.yy.sdk.upgrade.net.josn;
/**
 * @(#)ResultHandler.java	
 * Copyright 2014 广州华多网络科技有限公司, Inc. All rights reserved.
 * @author 陈真   2014/05/12
 */
public class ResultHandler {
	/**
	 * <p>
	 * <br>it's request success that 
	 * <br>only returned result code with 200
	 * <br>当请求返回结果是200时，才算请求成功.
	 * </p>
	 */
	public int status;
	public String message;
	public ResultEntity resultEntity;
	
	public ResultHandler(int status, String message, ResultEntity resultEntity) {
		super();
		this.status = status;
		this.message = message;
		this.resultEntity = resultEntity;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public ResultEntity getResultEntity() {
		return resultEntity;
	}
	public void setResultEntity(ResultEntity resultEntity) {
		this.resultEntity = resultEntity;
	}
}
