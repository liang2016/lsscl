package com.serotonin.mango.db.dao.power;

import com.serotonin.mango.db.dao.BaseDao;

public class UserEventLimtDao extends BaseDao {
	private static final String USER_EVENT_HANDLER_LIMIT = "update userEventHandlers set limit=? where factoryId=?";
	private static final String INSERT_USER_EVENT_HANDLER_LIMIT = "insert into userEventHandlers(factoryId,eventHandlerCount,limit) values(?,?,?)";
	private static final String UPDATE_USER_COUNT="update  userEventHandlers set eventHandlerCount=eventHandlerCount+1 where factoryId=?";
	private static final String DELETE_COUNT="update  userEventHandlers set eventHandlerCount=eventHandlerCount-1 where factoryId=?";
	private static final String SEARCH_USER_LIMIT="select limit-eventHandlerCount from userEventHandlers where factoryId=?";
	/**
	 * 修改用户创建事件处理器上限
	 * 
	 * @param userId
	 *            用户编号
	 * @param limit
	 *            上线
	 */
	public void updateUserEventHandler(int factoryId, int limit) {
		ejt.update(USER_EVENT_HANDLER_LIMIT, new Object[] { limit,factoryId });
	}
	/**
	 * 初始化用户设置事件处理器个数
	 * @param userId 用户id
	 * @param limit 数量
	 */
    public void insertUserEventHandler(int factoryId,int limit){
    	ejt.update(INSERT_USER_EVENT_HANDLER_LIMIT, new Object[] { factoryId,0,limit });    	
    }
	/**
	 * 修改用户已经设置处理器的个数
	 * @param userId 用户id
	 * @param limit 数量
	 */
    public void updateUserEventHandlersCount(int factoryId){
    	ejt.update(UPDATE_USER_COUNT, new Object[] { factoryId});    	
    }
    /**
     * 用户删除一个事件处理器
     * @param factoryId 工厂id
     */
    public void deleteCount(int factoryId){
    	ejt.update(DELETE_COUNT, new Object[] { factoryId});    	
    }
    /**
     * 查询用户剩余可添加事件处理器的个数
     * @param userId
     * @return
     */
    public int getUserLimit(int factoryId){
    	return ejt.queryForInt(SEARCH_USER_LIMIT,new Object[]{factoryId},-1);
    }
    public void deleteUserLimit(int factoryId){
    	 ejt.update("delete userEventHandlers where factoryId=?",new Object[]{factoryId});
    }
}
