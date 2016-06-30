/**
 * (c) 2016 uchicom
 */
package com.uchicom.podof.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.uchicom.podof.MetaFrame;

/**
 * @author uchicom: Shigeki Uchiyama
 *
 */
public class UpdateAction extends AbstractAction {

	private MetaFrame metaFrame;
	/**
	 *
	 */
	public UpdateAction(MetaFrame metaFrame) {
		putValue(NAME, "更新");
		this.metaFrame = metaFrame;
	}

	/* (非 Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		metaFrame.updateMeta();
	}

}
