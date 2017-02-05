// (c) 2017 uchicom
package com.uchicom.podof.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.uchicom.podof.FileListFrame;

/**
 * @author uchicom: Shigeki Uchiyama
 *
 */
public class ViewerAction extends AbstractAction {

	private FileListFrame fileListFrame;
	/**
	 *
	 */
	public ViewerAction(FileListFrame fileListFrame) {
		putValue(NAME, "PDF表示");
		this.fileListFrame = fileListFrame;
	}
	/* (非 Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		fileListFrame.showView();

	}

}
