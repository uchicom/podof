/**
 * (c) 2013 uchicom
 */
package com.uchicom.podof;
import java.awt.color.ColorSpace;


/**
 * @author uchicom: Shigeki Uchiyama
 *
 */
public class Type {

	private int type;
	private String name;

	public Type(String name) {
		this.name = name;
	}

	public Type(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public String getTypeString() {
		String value = null;
		if (name != null)
			return name;
		switch (type) {
		case ColorSpace.TYPE_2CLR:
			value = "2CLR";
			break;
		case ColorSpace.TYPE_3CLR:
			value = "3CLR";
			break;
		case ColorSpace.TYPE_4CLR:
			value = "4CLR";
			break;
		case ColorSpace.TYPE_5CLR:
			value = "5CLR";
			break;
		case ColorSpace.TYPE_6CLR:
			value = "6CLR";
			break;
		case ColorSpace.TYPE_7CLR:
			value = "7CLR";
			break;
		case ColorSpace.TYPE_8CLR:
			value = "8CLR";
			break;
		case ColorSpace.TYPE_9CLR:
			value = "9CLR";
			break;
		case ColorSpace.TYPE_ACLR:
			value = "ACLR";
			break;
		case ColorSpace.TYPE_BCLR:
			value = "BCLR";
			break;
		case ColorSpace.TYPE_CCLR:
			value = "CCLR";
			break;
		case ColorSpace.TYPE_CMY:
			value = "CMY";
			break;
		case ColorSpace.TYPE_CMYK:
			value = "CMYK";
			break;
		case ColorSpace.TYPE_DCLR:
			value = "DCLR";
			break;
		case ColorSpace.TYPE_ECLR:
			value = "DCLR";
			break;
		case ColorSpace.TYPE_FCLR:
			value = "FCLR";
			break;
		case ColorSpace.TYPE_GRAY:
			value = "GRAY";
			break;
		case ColorSpace.TYPE_HLS:
			value = "HLS";
			break;
		case ColorSpace.TYPE_HSV:
			value = "HSV";
			break;
		case ColorSpace.TYPE_Lab:
			value = "Lab";
			break;
		case ColorSpace.TYPE_Luv:
			value = "Luv";
			break;
		case ColorSpace.TYPE_RGB:
			value = "RGB";
			break;
		case ColorSpace.TYPE_XYZ:
			value = "XYZ";
			break;
		case ColorSpace.TYPE_YCbCr:
			value = "YCbCr";
			break;
		case ColorSpace.TYPE_Yxy:
			value = "Yxy";
			break;
		default:
			value = "none";
		}
		return value;
	}

	@Override
	public String toString() {
		return getTypeString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Type) {
			Type type = (Type) obj;
			if (type != null) {
				return type.type == this.type;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return type;
	}
}
