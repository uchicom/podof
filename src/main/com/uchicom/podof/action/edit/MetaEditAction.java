// (c) 2016 uchicom
package com.uchicom.podof.action.edit;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.uchicom.podof.FileListFrame;

/**
 * @author uchicom: Shigeki Uchiyama
 *
 */
public class MetaEditAction extends AbstractAction {

	private FileListFrame main;
	/**
	 *
	 */
	public MetaEditAction(FileListFrame main) {
		putValue(NAME, "メタ編集");
		this.main = main;
	}


	/* (非 Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		main.editMeta();
	}

}
