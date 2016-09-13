package com.serotonin.mango.vo;

import java.util.Comparator;

import com.serotonin.mango.vo.acp.ACPVO;
import com.serotonin.util.StringUtils;

/**
 * 名称排序需要用到的类 详细是根据com.serotonin.mango.vo.DataPointNameComparator类模仿
 * @author 王金阳 2011年9月13日 08:48:27
 *
 */
public class AcpNameComparator implements Comparator<ACPVO>{

	public static final AcpNameComparator instance = new AcpNameComparator();
	
	public int compare(ACPVO o1, ACPVO o2) {
		if(StringUtils.isEmpty(o1.getAcpname())){
			return -1;
		}
		return o1.getAcpname().compareToIgnoreCase(o2.getAcpname());
	}

}
