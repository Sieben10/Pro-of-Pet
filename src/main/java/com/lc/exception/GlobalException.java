package com.lc.exception;

import com.lc.result.CodeMsg;
//系统异常的方法
public class GlobalException extends RuntimeException{

	private static final long serialVersionUID = 1L;//版本控制 对象序列化
	
	private CodeMsg cm;
	
	public GlobalException(CodeMsg cm) {
		super(cm.toString());
		this.cm = cm;
	}

	public CodeMsg getCm() {
		return cm;
	}
}
