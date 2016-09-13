package com.serotonin.mango.vo.statistics;

import java.util.Comparator;
import com.serotonin.mango.vo.statistics.IndexOrderVO;

public class ComparatorIndexOrder implements Comparator {

	@Override
	public int compare(Object arg0, Object arg1) {
		IndexOrderVO indexOrderVO0 = (IndexOrderVO) arg0;
		IndexOrderVO indexOrderVO1 = (IndexOrderVO) arg1;
		if (indexOrderVO0.getValue() > indexOrderVO1.getValue()) {// 第一个比第二个大，返回-1
			return -1;
		} else if (indexOrderVO0.getValue() == indexOrderVO1.getValue()) {// 第一个和第二个相等，返回0
			return 0;
		} else {// 第一个比第二个小，返回1
			return 1;
		}
	}

}
