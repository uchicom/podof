/**
 * (c) 2016 uchicom
 */
package com.uchicom.podof;

import java.io.File;

/**
 * @author uchicom: Shigeki Uchiyama
 *
 */
public class PdfFile {

	File file;
	/**
	 *
	 */
	public PdfFile(File file) {
		this.file = file;
	}

	/**
	 * fileを取得します.
	 *
	 * @return file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * fileを設定します.
	 *
	 * @param file file
	 */
	public void setFile(File file) {
		this.file = file;
	}

	public String toString() {
		return file.getName();
	}

}
