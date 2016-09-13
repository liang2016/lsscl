package test;

import java.sql.SQLException;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.DataPointDao;
import com.serotonin.mango.db.dao.PointValueDao;
import com.serotonin.mango.rt.dataImage.PointValueTime;
import com.serotonin.mango.util.DateUtils;
import com.serotonin.mango.view.text.TextRenderer;
import com.serotonin.mango.vo.DataPointExtendedNameComparator;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.mango.vo.hierarchy.PointHierarchy;

public class DataPointDaoTest {
	private DataPointDao dao;
	private PointValueDao pvDao;
	
	@Before
	public void init(){
		SQLServerDataSource dataSource = new SQLServerDataSource();
		dataSource.setURL("jdbc:sqlserver://192.168.1.116:1433; DatabaseName=LssclDB");
		dataSource.setUser("sa");
		dataSource.setPassword("123456");
		dao = new DataPointDao(dataSource);
		pvDao = new PointValueDao(dataSource);
	}
	
	@Test
	public void test() throws SQLException{
		
		
		
		PointHierarchy ph = dao.getPointHierarchy(3).copyFoldersOnly();
		List<DataPointVO> points = dao.getDataPoints(136,DataPointExtendedNameComparator.instance, false);
		DataPointVO p2 = dao.getDataPoint(8780);
		PointValueTime pvt = pvDao.getLatestPointValue(p2.getId());
//		System.out.println(p2.getDataTypeMessage().getKey());
		System.out.println(p2.getName());
		System.out.println(p2.getTextRenderer().getText(true, 2));
		System.out.println(pvt.getStringValue());
		int id = dao.getPointFromAcpMember(284, 41);
		TextRenderer tr = p2.getTextRenderer();
//		System.out.println(tr.getText(0));
	}

	/**
	 * 获取点数据
	 */
	@Test
	public void otherPointList(){
		SQLServerDataSource dataSource = new SQLServerDataSource();
		dataSource.setURL("jdbc:sqlserver://192.168.1.116:1433; DatabaseName=LssclDB");
		dataSource.setUser("sa");
		dataSource.setPassword("123456");
		dao = new DataPointDao(dataSource);
		//不属于空压机的点/直接属于数据源的点
        List<DataPointVO> otherPointList = dao.getDataPointWhitoutAcp(16,new int[]{1,2}); 
        for(DataPointVO dp:otherPointList){
        	PointValueTime pvt = new PointValueDao(dataSource).getLatestPointValue(dp.getId());
        	String attrName=null,value = null;
        	attrName = dp.getName();
        	if(pvt!=null){
	        	if("common.dataTypes.numeric".equals(dp.getDataTypeMessage().getKey())){//数字
	        		
	        		value = dp.getTextRenderer().getText(pvt.getDoubleValue(), 2);
	        		
	        	}else if(false){
	        		
	        	}
        	}
        	System.out.println(attrName+": \t"+value+"--\t"+pvt);
        }
	}
	
	@Test
	public void getDataPoints(){
		List<DataPointVO> dataPoints = dao.getDataPoints(null, false);
		System.out.println(dataPoints.size());
		 int deleteCount = 0;
//	        for (DataPointVO dataPoint : dataPoints)
//	            deleteCount += purgePoint(dataPoint);
	        // if (deleteCount > 0)
	        // new PointValueDao().compressTables();

//	        log.info("Data purge ended, " + deleteCount + " point values deleted");
	}
}
