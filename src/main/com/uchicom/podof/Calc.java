/**
 * (c) 2013 uchicom
 */
package com.uchicom.podof;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author uchicom: Shigeki Uchiyama
 *
 */
public class Calc {

	public int count = 1;
	public Type type;
	public SortedMap<Integer, Integer> map = new TreeMap<Integer, Integer>();

	public Calc(Type type, int index) {
		this.type = type;
		addIndex(index);
	}

	public String toString() {
		return type.getTypeString() + ":" + count + "[" + getIndexes() + "]";
	}

	public String getIndexes() {
		StringBuffer strBuff = new StringBuffer();
		Iterator<Entry<Integer, Integer>> iterator = map.entrySet().iterator();
		int start = -1;
		int end = -1;
		while (iterator.hasNext()) {
			Entry<Integer, Integer> entry = iterator.next();
			if (start < 0) {
				start = entry.getValue();
				end = start;
				if (strBuff.length() > 0) {
					strBuff.append(",");
				}
				strBuff.append(start);
			} else if (end + 1 == entry.getValue()) {
				end = entry.getValue();
			} else if (start == end) {
				start = -1;
				end = -1;
			} else {
				strBuff.append("-");
				strBuff.append(end);
				start = -1;
				end = -1;
			}
		}
		if (start != -1 && end != -1) {
			if (start == end) {
				start = -1;
				end = -1;
			} else {
				strBuff.append("-");
				strBuff.append(end);
				start = -1;
				end = -1;
			}
		}
		return strBuff.toString();
	}

	public void increment() {
		count++;
	}

	public void addIndex(int index) {
		map.put(index, index);
	}

	public static List<Calc> getCalcList(List<Page> pageList) {
		List<Calc> calcList = new ArrayList<Calc>();
		Map<Type, Calc> map = new HashMap<Type, Calc>();
		for (Page page : pageList) {
			for (Type type : page.getTypeList()) {
				if (map.containsKey(type)) {
					Calc calc = map.get(type);
					calc.addIndex(page.getIndex() + 1);
					calc.increment();
				} else {
					Calc calc = new Calc(type, page.getIndex() + 1);
					map.put(type, calc);
					calcList.add(calc);
				}
			}
		}
		return calcList;
	}
}
