// (c) 2016 uchicom
package com.uchicom.podof;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author uchicom: Shigeki Uchiyama
 *
 */
public class Main {

	/**
	 *
	 */
	public Main() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

    /**
     * @param args
     */
    public static void main(String[] args) {
        List<File> fileList = new ArrayList<File>();
        for (String arg : args) {
            fileList.add(new File(arg));
        }
        FileListFrame main = new FileListFrame(fileList);
        main.pack();
        main.setVisible(true);

    }

}
