/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.mvc.form;

public class LoginForm {
    private String username;
    private String password;
    private String yazhengma;
    public String getYazhengma() {
		return yazhengma;
	}

	public void setYazhengma(String yazhengma) {
		this.yazhengma = yazhengma;
	}

	public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
