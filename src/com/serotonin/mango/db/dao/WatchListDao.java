/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.db.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.serotonin.db.spring.ExtendedJdbcTemplate;
import com.serotonin.db.spring.GenericRowMapper;
import com.serotonin.mango.Common;
import com.serotonin.mango.view.ShareUser;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.mango.vo.WatchList;
import com.serotonin.mango.vo.User;

/**
 *  
 */

  //观察列表DAO

public class WatchListDao extends BaseDao {
    public String generateUniqueXid() {
        return generateUniqueXid(WatchList.XID_PREFIX, "watchLists");
    }

    public boolean isXidUnique(String xid, int watchListId) {
        return isXidUnique(xid, watchListId, "watchLists");
    }

    /**
     * 
     * Note: this method only returns basic watchlist information. No data points or share users.
     */
    /**
     * 注释:通过用户的编号得到 列表的信息,没有数据点或用户共享
     */
    public List<WatchList> getWatchLists(final int userId) {
        return query("select id, xid, userId, name from watchLists " //
                + "where userId=? or id in (select watchListId from watchListUsers where userId=?) " //
                + "order by name", new Object[] { userId, userId }, new WatchListRowMapper());
    }
    
    /**
     * 根据 用户+用户所在范围 查询观察列表
     * @param userId 用户编号
     * @param factoryId 范围编号
     * @return 观察列表
     */
    public List<WatchList> getWatchLists(final int userId,final int factoryId) {
        return query("select id, xid, userId, name from watchLists " //
                + "where (userId=? or id in (select watchListId from watchListUsers where userId=?)) and factoryId = ? " //
                + "order by name", new Object[] { userId, userId,factoryId }, new WatchListRowMapper());
    }

    /**
     * Note: this method only returns basic watchlist information. No data points or share users.
     */
    public List<WatchList> getWatchLists() {
        return query("select id, xid, userId, name from watchLists", new WatchListRowMapper());
    }
//通过观察列表的编号得到对象信息
    public WatchList getWatchList(int watchListId) {
        // Get the watch lists.
        WatchList watchList = queryForObject("select id, xid, userId, name from watchLists where id=?",
                new Object[] { watchListId }, new WatchListRowMapper());
        populateWatchlistData(watchList);
        return watchList;
    }
//同观察列表的信息得到移动后的信息
    public void populateWatchlistData(List<WatchList> watchLists) {
        for (WatchList watchList : watchLists)
            populateWatchlistData(watchList);
    }

    public void populateWatchlistData(WatchList watchList) {
        if (watchList == null)
            return;

        // Get the points for each of the watch lists.
        List<Integer> pointIds = queryForList(
                "select dataPointId from watchListPoints where watchListId=? order by sortOrder",
                new Object[] { watchList.getId() }, Integer.class);
        List<DataPointVO> points = watchList.getPointList();
        DataPointDao dataPointDao = new DataPointDao();
        for (Integer pointId : pointIds)
            points.add(dataPointDao.getDataPoint(pointId));

        setWatchListUsers(watchList);
    }

    /**
     * Note: this method only returns basic watchlist information. No data points or share users.
     */
    public WatchList getWatchList(String xid) {
        return queryForObject("select id, xid, userId, name from watchLists where xid=?", new Object[] { xid },
                new WatchListRowMapper(), null);
    }
    
    class WatchListRowMapper implements GenericRowMapper<WatchList> {
        public WatchList mapRow(ResultSet rs, int rowNum) throws SQLException {
            WatchList wl = new WatchList();
            wl.setId(rs.getInt(1));
            wl.setXid(rs.getString(2));
            wl.setUserId(rs.getInt(3));
            wl.setName(rs.getString(4));
            return wl;
        }
    }
//保存选中的观察列表
    public void saveSelectedWatchList(int userId, int watchListId) {
        ejt.update("update users set selectedWatchList=? where id=?", new Object[] { watchListId, userId });
    }
//创建新的观察列表
    public WatchList createNewWatchList(WatchList watchList, int userId) {
        watchList.setUserId(userId);
        watchList.setXid(generateUniqueXid());
        watchList.setId(doInsert("insert into watchLists (xid, userId, name) values (?,?,?)",
                new Object[] { watchList.getXid(), userId, watchList.getName() }));
        return watchList;
    }
    
  //创建新的观察列表,点击左边点的时候创建空的观察列表
    public WatchList createNewWatchList(WatchList watchList, User user) {
        watchList.setUserId(user.getId());
        watchList.setXid(generateUniqueXid());
        watchList.setId(doInsert("insert into watchLists (xid, userId, name,factoryId) values (?,?,?,?)",
                new Object[] { watchList.getXid(), user.getId(), watchList.getName(),user.getCurrentScope().getId() }));
        return watchList;
    }
    
//保存观察列表
    public void saveWatchList(final WatchList watchList) {
        final ExtendedJdbcTemplate ejt2 = ejt;
        getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {
            @SuppressWarnings("synthetic-access")
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                if (watchList.getId() == Common.NEW_ID)
                    watchList.setId(doInsert("insert into watchLists (xid, name, userId,factoryId) values (?,?,?,?)", new Object[] {
                            watchList.getXid(), watchList.getName(), watchList.getUserId(),watchList.getFactoryId() }));
                else
                    ejt2.update("update watchLists set xid=?, name=? where id=?", new Object[] { watchList.getXid(),
                            watchList.getName(), watchList.getId() });
                ejt2.update("delete from watchListPoints where watchListId=?", new Object[] { watchList.getId() });
                ejt2.batchUpdate("insert into watchListPoints values (?,?,?)", new BatchPreparedStatementSetter() {
                    public int getBatchSize() {
                        return watchList.getPointList().size();
                    }

                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, watchList.getId());
                        ps.setInt(2, watchList.getPointList().get(i).getId());
                        ps.setInt(3, i);
                    }
                });

                saveWatchListUsers(watchList);
            }
        });
    }
//删除观察列表
    public void deleteWatchList(final int watchListId) {
        final ExtendedJdbcTemplate ejt2 = ejt;
        getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                deleteWatchListUsers(watchListId);
                ejt2.update("delete from watchListPoints where watchListId=?", new Object[] { watchListId });
                ejt2.update("delete from watchLists where id=?", new Object[] { watchListId });
            }
        });
    }
    //getWatchListIds
    public List<Integer> getWatchListIds(int factory) {
        return queryForList("select id from watchLists where factoryId=?", new Object[] { factory },Integer.class);
    }
    //
    //设置观察列表
    // Watch list users
    //
    private void setWatchListUsers(WatchList watchList) {
        watchList.setWatchListUsers(query("select userId, accessType from watchListUsers where watchListId=?",
                new Object[] { watchList.getId() }, new WatchListUserRowMapper()));
    }

    class WatchListUserRowMapper implements GenericRowMapper<ShareUser> {
        public ShareUser mapRow(ResultSet rs, int rowNum) throws SQLException {
            ShareUser wlu = new ShareUser();
            wlu.setUserId(rs.getInt(1));
            wlu.setAccessType(rs.getInt(2));
            return wlu;
        }
    }

    void deleteWatchListUsers(int watchListId) {
    	
        ejt.update("delete from watchListUsers where watchListId=?", new Object[] { watchListId });
    }

    void saveWatchListUsers(final WatchList watchList) {
        // Delete anything that is currently there.
        deleteWatchListUsers(watchList.getId());

        // Add in all of the entries.
        ejt.batchUpdate("insert into watchListUsers values (?,?,?)", new BatchPreparedStatementSetter() {
            @Override
            public int getBatchSize() {
                return watchList.getWatchListUsers().size();
            }

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ShareUser wlu = watchList.getWatchListUsers().get(i);
                ps.setInt(1, watchList.getId());
                ps.setInt(2, wlu.getUserId());
                ps.setInt(3, wlu.getAccessType());
            }
        });
    }
//将用户从观察列表中删除
    public void removeUserFromWatchList(int watchListId, int userId) {
        ejt.update("delete from watchListUsers where watchListId=? and userId=?", new Object[] { watchListId, userId });
    }
}
