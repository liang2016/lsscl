import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.sql.DataSource;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.serotonin.mango.db.dao.DataPointDao;
import com.serotonin.mango.view.text.AnalogRenderer;
import com.serotonin.mango.view.text.TextRenderer;
import com.serotonin.mango.vo.DataPointVO;

public class TextRanderEditor {

	public static void main(String[] args) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("0", "(?i)\\s*((小时)|[A|℃|h])\\s*");
		map.put("0.00", "(?i)\\s*(m|米)\\s*");
		map.put("0.0", "(?i)\\s*((bar)|V|(吨)|(kw\\.h)|(kw)|(hz))\\s*");
		Map<String, List<String>> renders = new HashMap<String, List<String>>();
		renders.put("0", new ArrayList<String>());
		renders.put("0.00", new ArrayList<String>());
		renders.put("0.0", new ArrayList<String>());
		renders.put("other", new ArrayList<String>());
		// 获取数据源
		DataSource dataSource = getDataSource(args);
		DataPointDao dpDao = new DataPointDao(dataSource);
		Scanner s = new Scanner(System.in);
		System.out.println("F:修改整个工厂  P:单个点 exit：退出\n");
		String msg = s.next();
		while (!"exit".equals(msg)) {
			if("F".equals(msg)){
				editByFactoryId(map, renders, dpDao, s);
			}else if("P".equals(msg)){
				editByPointId(map, renders, dpDao, s);
			}
			System.out.println("选项\nF:修改整个工厂  P:单个点 exit：退出\n");
			msg = s.next();
		}
	}

	private static void editByPointId(Map<String, String> map,
			Map<String, List<String>> renders, DataPointDao dpDao, Scanner s) {
		System.out.println("请输入点ID");
		int id = s.nextInt();
		DataPointVO p = dpDao.getDataPoint(id);
		editPoint(map, renders, p);
		dpDao.updateDataPointShallow(p);
	}

	private static void editByFactoryId(Map<String, String> map,
			Map<String, List<String>> renders, DataPointDao dpDao, Scanner s) {
		System.out.println("请输入工厂ID：");
		String msg = s.next();
		int factoryId = Integer.parseInt(msg);
		List<DataPointVO> points = dpDao.getDataPoints(factoryId, null, false);
		for (DataPointVO p : points) {
			editPoint(map, renders, p);
			dpDao.updateDataPointShallow(p);
		}
		for(String key:renders.keySet()){
			System.out.println(key+"("+renders.get(key).size()+")"+":"+Arrays.toString(renders.get(key).toArray()));
		}
		System.out.print("输入工厂id或exit退出");
	}

	private static void editPoint(Map<String, String> map,
			Map<String, List<String>> renders, DataPointVO p) {
		TextRenderer render = p.getTextRenderer();
		if (render instanceof AnalogRenderer) {
			AnalogRenderer aRender = (AnalogRenderer) render;
			if (aRender.getSuffix().matches(map.get("0"))) {
				List<String> r = renders.get("0");
				r.add(p.getName() + "(" + aRender.getSuffix() + ")");
				renders.put("0", r);
				p.setTextRenderer(new AnalogRenderer("0",aRender.getSuffix()));
			} else if (aRender.getSuffix().matches(map.get("0.0"))) {
				List<String> r = renders.get("0.0");
				r.add(p.getName() + "(" + aRender.getSuffix() + ")");
				renders.put("0.0", r);
				p.setTextRenderer(new AnalogRenderer("0.0",aRender.getSuffix()));
			} else if (aRender.getSuffix().matches(map.get("0.00"))) {
				List<String> r = renders.get("0.00");
				r.add(p.getName() + "(" + aRender.getSuffix() + ")");
				renders.put("0.00", r);
			}else{
				List<String> r = renders.get("other");
				r.add(p.getName() + "(" + aRender.getSuffix() + ")");
				renders.put("other", r);
				p.setTextRenderer(new AnalogRenderer("0.00",aRender.getSuffix()));
			}
		}
	}

	public static DataSource getDataSource(String[] args) {
		String host = args[0];
		String password = args[1];
		System.out.println("正在连接..." + host);
		SQLServerDataSource dataSource = new SQLServerDataSource();
		dataSource.setURL("jdbc:sqlserver://" + host
				+ ":1433; DatabaseName=LssclDB");
		dataSource.setUser("sa");
		dataSource.setPassword(password);
		return dataSource;
	}

}
