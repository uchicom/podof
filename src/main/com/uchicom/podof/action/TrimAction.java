// (c) 2017 uchicom
package com.uchicom.podof.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.uchicom.podof.FileListFrame;

/**
 * @author uchicom: Shigeki Uchiyama
 *
 */
public class TrimAction extends AbstractAction {

	private FileListFrame fileListFrame;
	/**
	 *
	 */
	public TrimAction(FileListFrame fileListFrame) {
		putValue(NAME, "トリム");
		this.fileListFrame = fileListFrame;
	}
	/* (非 Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		fileListFrame.showTrim();
	}

}
