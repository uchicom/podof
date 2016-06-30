/**
 * (c) 2013 uchicom
 */
package com.uchicom.podof;

import java.util.ArrayList;
import java.util.List;

/**
 * @author uchicom: Shigeki Uchiyama
 *
 */
public class Page {

	private int index;
	private List<Type> typeList = new ArrayList<Type>();

	public Page(int index) {
		this.index = index;
	}

	public void addType(Type type) {
		typeList.add(type);
	}

	public String toString() {
		return index + ":" + typeList;
	}

	public List<Type> getTypeList() {
		return typeList;
	}

	public int getIndex() {
		return index;
	}
}
