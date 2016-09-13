import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.sql.DataSource;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.serotonin.mango.db.dao.BaseDao;
import com.serotonin.mango.db.dao.DataPointDao;
import com.serotonin.mango.vo.DataPointVO;

public class PointValuesTrigger extends BaseDao {
	private static int count = 0;
	private static final String acp_attr = "select a.id,aa.attrname "
			+ "from aircompressor a "
			+ "left join aircompressor_type at on a.actid = at.id "
			+ "left join aircompressor_type_attr ata on ata.actid = at.id "
			+ "left join aircompressor_attr aa on ata.acaid = aa.id "
			+ "left join aircompressor_members am on am.acaid = aa.id and am.acid = a.id "
			+ "left join statisticsConfiguration ac on ac.acpaid = aa.id "
			+ "left join statisticsParam sp on sp.id = ac.spid "
			+ "where sp.paramname is null " + "and aa.attrname like '报警%' "
			+ "and am.dpid = ?";

	public PointValuesTrigger(DataSource ds) {
		super(ds);
	}

	public static void main(String[] args) {

		long l = System.currentTimeMillis();
		if (args.length == 2) {
			String host = args[0];
			String password = args[1];
			System.out.println("正在连接..." + host);
			SQLServerDataSource dataSource = new SQLServerDataSource();
			dataSource.setURL("jdbc:sqlserver://" + host
					+ ":1433; DatabaseName=LssclDB");
			dataSource.setUser("sa");
			dataSource.setPassword(password);
			PointValuesTrigger pvt = new PointValuesTrigger(dataSource);
			DataPointDao dao = new DataPointDao(dataSource);
			List<Integer> ids = dao.getDataPointIds();
			for (Integer id : ids) {
				DataPointVO dp = dao.getDataPoint(id);
				if ("common.dataTypes.binary".equals(dp.getDataTypeMessage()
						.getKey())) {// 二进制数据
					pvt.makeTrigger(id);
				}
			}
			System.out.println("时间：" + (System.currentTimeMillis() - l) / 1000);
			System.out.println("共有触发器：" + count);
		}
	}

	public void makeTrigger(int id) {
		String sql = "select top 1 * from pointValues_" + id
				+ " order by id desc";
		Map<String, Object> map = queryForObject(acp_attr, new Object[] { id },
				new com.serotonin.mango.vo.ResultData(),
				new HashMap<String, Object>());
		Integer aid = (Integer) map.get("id");
		String attrname = (String) map.get("attrname");
		if (attrname != null) {
			ejt.execute(makeTriggerStr(id));
			ejt.execute(makeTriggerStr2(id));
			System.out.println(id + "," + attrname);
			count++;
		}
	}

	public static String makeTriggerStr(int id) {
		StringBuilder sb = new StringBuilder();
		sb.append("if exists(select name from sysobjects where xtype='tr' and name='tri_pv_"
				+ id + "') \n");
		sb.append("begin \n");
		sb.append("    drop trigger tri_pv_" + id + " \n");
		sb.append("end \n");
		return sb.toString();
	}

	public static String makeTriggerStr2(int id) {
		StringBuilder sb = new StringBuilder();
		sb.append("create trigger tri_pv_" + id + " \n");
		sb.append("    on dbo.pointValues_" + id + " \n");
		sb.append("    after insert \n");
		sb.append("as \n");
		// 定义变量
		sb.append("    declare @data int,@ts bigint,@cTime bigint,@id int \n");
		sb.append("    select top 1 @data= pointValue,@ts=ts from pointValues_"
				+ id + " order by id desc \n");
		sb.append("    if @data=1 \n");
		sb.append("    begin \n");
		// --显示插入id
		sb.append("    select @id=id,@cTime=cTime from mobileEvents where id="
				+ id + " \n");
		sb.append("    if @id is null \n");
		sb.append("       begin \n");
		sb.append("					set identity_insert mobileEvents on \n");
		sb.append("					insert into mobileEvents(id,cTime)values(" + id
				+ ",@ts) \n");
		sb.append("       end \n");
		sb.append("    else \n");
		sb.append("        BEGIN \n");
		sb.append("          if @cTime is null \n");
		sb.append("					update mobileEvents set cTime=@ts where id=" + id
				+ " \n");
		sb.append("        end \n");
		sb.append("    end \n");
		return sb.toString();
	}
}
