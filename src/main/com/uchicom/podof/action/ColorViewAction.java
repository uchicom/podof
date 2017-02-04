// (c) 2016 uchicom
package com.uchicom.podof.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.uchicom.podof.FileListFrame;

/**
 * @author uchicom: Shigeki Uchiyama
 *
 */
public class ColorViewAction extends AbstractAction {

	private FileListFrame fileListFrame;
	/**
	 *
	 */
	public ColorViewAction(FileListFrame fileListFrame) {
		putValue(NAME, "画像カラー情報");
		this.fileListFrame = fileListFrame;
	}

	/* (非 Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		fileListFrame.showColorView();
	}

}
