package test;

import java.io.IOException;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.junit.Test;

import com.lsscl.app.bean.QC;

public class XMLTest {
	@Test
	public void readXml(){
		SAXBuilder builder = new SAXBuilder();
		try {
			Document doc = builder.build(this.getClass().getClassLoader().getResourceAsStream("QC.xml"));
			String s = "";
			Element rootEl = doc.getRootElement();
			//获取所有子元素
			List<Element> list = rootEl.getChildren();
			QC qc = new QC();
			for(Element el:list){
				String tagName = el.getName();
				String content = el.getText();
				if("MSGID".equals(tagName)){//
					//qc.setMgsId(content);
				}else if("SIMNO".equals(tagName)){
					qc.setSimNo(content);
				}else if("IMEI".equals(tagName)){
					qc.setImei(content);
				}else if("MSGBODY".equals(tagName)){
					List<Element> cList = el.getChildren();
					for(Element e:cList){
						System.out.println(e.getName()+","+e.getText());
					}
				}
			}
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
