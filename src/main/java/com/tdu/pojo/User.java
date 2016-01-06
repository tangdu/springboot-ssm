package com.tdu.pojo;

import java.io.Serializable;

public class User implements Serializable {
	private static final long serialVersionUID = -8588814535074011726L;

	private String id;
	private String uname;
	private String upwd;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getUpwd() {
		return upwd;
	}

	public void setUpwd(String upwd) {
		this.upwd = upwd;
	}
}
