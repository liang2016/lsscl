package com.serotonin.mango.web.dwr.scope;
import com.serotonin.mango.vo.scope.ScopeVO;
import com.serotonin.mango.db.dao.scope.ScopeDao;
import com.serotonin.mango.web.mvc.form.ScopeForm;
import java.util.List;
public class FactoryDwr {
	/**
	 * 查询所有工厂
	 * 
	 * @return
	 */
	public List<ScopeVO> getFactories() {
		ScopeDao dao = new ScopeDao();
		return dao.getFactoryByHq();
	}
	
	/**
	 * 根据工厂编号查询工厂
	 * 
	 * @param id
	 *            工厂编号
	 * @return
	 */
	public ScopeVO getFactoriesById(int id) {
		ScopeDao dao = new ScopeDao();
		return dao.findFactoryById(id);
	}
	

	/**
	 * 根据子区域编号查询工厂
	 * 
	 * @param SZId
	 *            子区域编号
	 * @return
	 */
	public List<ScopeVO> getFactoriesBySZId(int SZId) {
		ScopeDao dao = new ScopeDao();
		return dao.getFactoryBySubZone(SZId);
	}

	/**
	 * 根据区域编号查询工厂
	 * 
	 * @param ZId
	 *            区域编号
	 * @return
	 */
	public List<ScopeVO> getFactoriesByZId(int ZId) {
		ScopeDao dao = new ScopeDao();
		return dao.getFactoryByZone(ZId);
	}
	/**
	 * 删除一个工厂
	 * @param factoryId 工厂编号
	 * @return 工厂id
	 */
	public int deleteFactoryById(int factoryId){
		ScopeDao dao = new ScopeDao();
         return dao.delete(factoryId);
	}
	 public int savefactory(ScopeForm factory){
		 ScopeDao dao = new ScopeDao();
		 try{
			 dao.saveFactory(factory);
			 return 1;
		 }
		 catch (Exception e) {
			// TODO: handle exception
			 return 0;
		}
	 }
}
