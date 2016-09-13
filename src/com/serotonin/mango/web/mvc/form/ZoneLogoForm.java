/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.mvc.form;

import org.springframework.web.multipart.MultipartFile;

import com.serotonin.mango.vo.scope.ScopeVO;

public class ZoneLogoForm {
	private ScopeVO scope;
	private MultipartFile backgroundImageMP;

	public MultipartFile getBackgroundImageMP() {
		return backgroundImageMP;
	}

	public void setBackgroundImageMP(MultipartFile backgroundImageMP) {
		this.backgroundImageMP = backgroundImageMP;
	}

	public ScopeVO getScope() {
		if(scope==null||scope.getId()==null){
			return scope=new ScopeVO();
		}
		else 
			return scope;
	}

	public void setScope(ScopeVO scope) {
		this.scope = scope;
	}

}
