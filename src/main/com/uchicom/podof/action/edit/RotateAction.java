// (c) 2017 uchicom
package com.uchicom.podof.action.edit;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.uchicom.podof.FileListFrame;

/**
 * @author uchicom: Shigeki Uchiyama
 *
 */
public class RotateAction extends AbstractAction {

	private FileListFrame main;
	private int degree;
	/**
	 *
	 */
	public RotateAction(FileListFrame main, int degree) {
		putValue(NAME, "右へ" + degree + "度回転");
		this.main = main;
		this.degree = degree;
	}
	/* (非 Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		main.rotate(degree);
	}

}
