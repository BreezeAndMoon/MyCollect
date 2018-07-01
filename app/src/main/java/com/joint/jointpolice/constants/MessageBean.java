package com.joint.jointpolice.constants;

import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

/**
 * 网络请求，模拟一个等待接收的对象。
 * 
 * @author 小F君
 * 
 */
public class MessageBean implements Serializable {

	private static final long serialVersionUID = -8575347226248681583L;

	@JsonProperty("CODE")
	private String code;

	@JsonProperty("MSG")
	private String message;

	public MessageBean() {
	}

	public MessageBean(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "Message [code=" + code + ", message=" + message + "]";
	}

}
