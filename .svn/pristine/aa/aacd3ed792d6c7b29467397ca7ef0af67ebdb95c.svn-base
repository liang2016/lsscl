package com.serotonin.mango.web.dwr;

import com.serotonin.mango.Common;
import com.serotonin.mango.view.text.TextRenderer;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.mango.vo.RenderVo;
import com.serotonin.mango.vo.User;
import com.serotonin.mango.db.dao.TextRenderDao;
import com.serotonin.web.dwr.DwrResponseI18n;
import com.serotonin.mango.vo.dataSource.modbus.ModbusPointLocatorVO;
import com.serotonin.mango.view.text.BaseTextRenderer;
import java.util.List;
import com.serotonin.mango.view.ImplDefinition;
import com.serotonin.mango.view.text.AnalogRenderer;
import com.serotonin.mango.view.text.BinaryTextRenderer;
import com.serotonin.mango.view.text.MultistateRenderer;
import com.serotonin.mango.view.text.MultistateValue;
import com.serotonin.mango.view.text.NoneRenderer;
import com.serotonin.mango.view.text.PlainRenderer;
import com.serotonin.mango.view.text.RangeRenderer;
import com.serotonin.mango.view.text.RangeValue;
import com.serotonin.mango.view.text.TextRenderer;
import com.serotonin.mango.view.text.TimeRenderer;
import com.serotonin.web.dwr.DwrResponseI18n;
public class TextRenderDwr extends BaseDwr{
	//加载所有
	public  List<RenderVo>  findAll() {
		List<RenderVo> list=new TextRenderDao().findAll();
		return list;
	}
	//根据id查询
	
	public RenderVo  findById(int id) {
		RenderVo render=new TextRenderDao().findById(id);
		setEditPoint(render.getTextRenderer());
		return render;
	}
	
	//编辑renderVo
	public DwrResponseI18n edit(int id,int dataType,String name){
		DwrResponseI18n response = new DwrResponseI18n();
		RenderVo renderVo=new RenderVo();
		renderVo.setId(id);
		renderVo.setDataType(dataType);
		renderVo.setName(name);
		User user = Common.getUser();
		DataPointVO textRendererIn = user.getEditPoint();
		TextRenderer textRenderer = textRendererIn.getTextRenderer();//TextRenderer 对象
		renderVo.setTextRenderer(textRenderer);
		TextRenderDao textRenderDao=new TextRenderDao();
		renderVo.setId(textRenderDao.edit(renderVo));
		response.addData("renderVo",renderVo);
		return response;
	}
	public DwrResponseI18n deleteRender(int id) {
		DwrResponseI18n response = new DwrResponseI18n();
		TextRenderDao textRenderDao=new TextRenderDao();
		textRenderDao.delete(id);
		response.addData("id",id);
		return response;
	}
	/**
	 * 根据数据类型获取的渲染器类型集合
	 * @return 类型集合
	 */
	public DwrResponseI18n showTextRenderer(int dataType){
		DwrResponseI18n response = new DwrResponseI18n();
		User user = Common.getUser();
		DataPointVO dp =user.getEditPoint(); 
		if(dp==null){
			dp=new DataPointVO();
			ModbusPointLocatorVO modbusLocatorVO = new ModbusPointLocatorVO();
			modbusLocatorVO.setModbusDataType(dataType);
			dp.setPointLocator(modbusLocatorVO);
			response.addData("textRenderer",null);
		}else{
		//	ModbusPointLocatorVO modbusLocatorVO = dp.getPointLocator();
		//	modbusLocatorVO.setModbusDataType(dataType);
			//dp.setPointLocator(modbusLocatorVO);
			response.addData("textRenderer",dp.getTextRenderer());
		}
		List<ImplDefinition> definitionList = BaseTextRenderer.getImplementation(dataType);
		response.addData("definitionList",definitionList);
		return response;
	}
	public void newEditPoint(){
		User user = Common.getUser();
		user.setEditPoint(new DataPointVO());
	}
	
	
	/**
	 * 获取当前编辑的点（此处是记录讲渲染器暂时记录在点对象中）
	 * @return 点
	 */
	public DataPointVO getEditPoint(){
		User user = Common.getUser();
		return user.getEditPoint();	
	}
	public void setEditPoint(TextRenderer tr){
		User user = Common.getUser();
		DataPointVO dp=new DataPointVO();
		dp.setTextRenderer(tr);
		user.setEditPoint(dp);
	}
	/**
	 * 将不同类型的渲染器存放到正在编辑的点对象中红，下面的方法一样作用
	 * @param format 构造器需要的参数
	 * @param suffix 构造器需要的参数
	 */
    public void setAnalogTextRenderer(String format, String suffix) {
    	getEditPoint().setTextRenderer(new AnalogRenderer(format, suffix));
    }

    public void setBinaryTextRenderer(String zeroLabel, String zeroColour, String oneLabel, String oneColour) {
    	getEditPoint().setTextRenderer(new BinaryTextRenderer(zeroLabel, zeroColour, oneLabel, oneColour));
    }

    public void setMultistateRenderer(List<MultistateValue> values) {
        MultistateRenderer r = new MultistateRenderer();
        for (MultistateValue v : values)
            r.addMultistateValue(v.getKey(), v.getText(), v.getColour());
        getEditPoint().setTextRenderer(r);
    }

    public void setNoneRenderer() {
    	getEditPoint().setTextRenderer(new NoneRenderer());
    }

    public void setPlainRenderer(String suffix) {
    	getEditPoint().setTextRenderer(new PlainRenderer(suffix));
    }

    public void setRangeRenderer(String format, List<RangeValue> values) {
        RangeRenderer r = new RangeRenderer(format);
        for (RangeValue v : values)
            r.addRangeValues(v.getFrom(), v.getTo(), v.getText(), v.getColour());
        getEditPoint().setTextRenderer(r);
    }

    public void setTimeTextRenderer(String format, int conversionExponent) {
    	getEditPoint().setTextRenderer(new TimeRenderer(format, conversionExponent));
    }
}
