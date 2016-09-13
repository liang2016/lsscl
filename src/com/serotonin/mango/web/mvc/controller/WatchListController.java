/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.mvc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import com.serotonin.db.IntValuePair;
import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.WatchListDao;
import com.serotonin.mango.db.dao.UserDao;
import com.serotonin.mango.view.ShareUser;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.mango.vo.User;
import com.serotonin.mango.vo.WatchList;
import com.serotonin.mango.vo.permission.Permissions;
import com.serotonin.web.i18n.I18NUtils;
import com.serotonin.mango.vo.scope.ScopeVO;


public class WatchListController extends ParameterizableViewController {
    public static final String KEY_WATCHLISTS = "watchLists";
    public static final String KEY_SELECTED_WATCHLIST = "selectedWatchList";
    //当前所在范围类型  工厂类型
    private static final int CURRENT_SCOPETYPE = ScopeVO.ScopeTypes.FACTORY;
    
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(getViewName(), createModel(request));
    }

    protected Map<String, Object> createModel(HttpServletRequest request) {
        Map<String, Object> model = new HashMap<String, Object>();
        User user = Common.getUser(request);
        int factoryId = Integer.parseInt(request.getParameter("factoryId"));
        UserDao.changeRole(CURRENT_SCOPETYPE,factoryId,user);
        // The user's permissions may have changed since the last session, so make sure the watch lists are correct.
        WatchListDao watchListDao = new WatchListDao();
        List<WatchList> watchLists = watchListDao.getWatchLists(user.getId(),user.getCurrentScope().getId());

        if (watchLists.size() == 0) {
            // Add a default watch list if none exist.
            WatchList watchList = new WatchList();
            watchList.setName(I18NUtils.getMessage(ControllerUtils.getResourceBundle(request), "common.newName"));
            watchLists.add(watchListDao.createNewWatchList(watchList, user));
        }

        int selected = user.getSelectedWatchList();
        boolean found = false;

        List<IntValuePair> watchListNames = new ArrayList<IntValuePair>(watchLists.size());
        for (WatchList watchList : watchLists) {
            if (watchList.getId() == selected)
                found = true;

            if (watchList.getUserAccess(user) == ShareUser.ACCESS_OWNER) {
                // If this is the owner, check that the user still has access to the points. If not, remove the
                // unauthorized points, resave, and continue.
                boolean changed = false;
                List<DataPointVO> list = watchList.getPointList();
                List<DataPointVO> copy = new ArrayList<DataPointVO>(list);
                for (DataPointVO point : copy) {
                    if (point == null || !Permissions.hasDataPointReadPermission(user, point)) {
                        list.remove(point);
                        changed = true;
                    } 
                }

                if (changed)
                    watchListDao.saveWatchList(watchList);
            }

            watchListNames.add(new IntValuePair(watchList.getId(), watchList.getName()));
        }

        if (!found) {
            // The user's default watch list was not found. It was either deleted or unshared from them. Find a new one.
            // The list will always contain at least one, so just use the id of the first in the list.
            selected = watchLists.get(0).getId();
            user.setSelectedWatchList(selected);
            new WatchListDao().saveSelectedWatchList(user.getId(), selected);
        }

        model.put(KEY_WATCHLISTS, watchListNames);
        model.put(KEY_SELECTED_WATCHLIST, selected);
        model.put("user", user);

        return model;
    }
}
