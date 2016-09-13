/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.vo.permission;

import com.serotonin.mango.vo.User;

public class PermissionException extends RuntimeException {
    private static final long serialVersionUID = -1;

    private final User user;

    public PermissionException(String message, User user) {
        super(message);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
