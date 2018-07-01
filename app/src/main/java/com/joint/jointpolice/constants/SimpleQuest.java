package com.joint.jointpolice.constants;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

/**
 * 网络请求模拟一个等待发送的对象。
 * 
 * @author 小F君
 * 
 */
public class SimpleQuest implements Serializable {
	private static final long serialVersionUID = 2777325261428250481L;

	@JsonProperty("NAME")
	private String name;

	@JsonProperty("PASSWORD")
	private String password;

	@JsonIgnore
	public String getName() {
		return name;
	}

	@JsonIgnore
	public void setName(String name) {
		this.name = name;
	}

	@JsonIgnore
	public String getpassword() {
		return password;
	}

	@JsonIgnore
	public void setpassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "SimpleQuest [name=" + name + ", password=" + password + "]";
	}
}
