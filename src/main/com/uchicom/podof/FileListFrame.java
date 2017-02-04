// (c) 2016 uchicom
package com.uchicom.podof;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.HeadlessException;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.TransferHandler;

import com.uchicom.podof.action.ColorViewAction;
import com.uchicom.podof.action.MetaEditAction;

/**
 * @author uchicom: Shigeki Uchiyama
 *
 */
public class FileListFrame extends JFrame {

	private DefaultListModel<File> listModel = new DefaultListModel<>();
	private JList<File> list = new JList<File>(listModel);
	private List<File> fileList = new ArrayList<>();
	private JProgressBar progressBar = new JProgressBar(JProgressBar.HORIZONTAL);
	private JMenuItem metaEditMenuItem = new JMenuItem(new MetaEditAction(this));
	/**
	 * @throws HeadlessException
	 */
	public FileListFrame(List<File> fileList){
		super("ファイル一覧画面");
		this.fileList = fileList;
		initList();
		initComponents();
	}

	public void initComponents() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		progressBar.setVisible(false);
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("編集");
        menu.add(metaEditMenuItem);
        menuBar.add(menu);
        menu = new JMenu("表示");
        JMenuItem menuItem = new JMenuItem(new ColorViewAction(this));
        menu.add(menuItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);
		list.setTransferHandler(new TransferHandler() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;
            @Override
            public boolean canImport(TransferSupport support) {
                DataFlavor[] dfs = support.getDataFlavors();
                for (DataFlavor df : dfs) {
                    if (DataFlavor.javaFileListFlavor.equals(df)) {
                        return support.getDropAction() == MOVE;
                    }
                }
                return false;
            }
            @SuppressWarnings("unchecked")
            @Override
            public boolean importData(TransferSupport support) {
                if (support.isDrop()) {
                    // ドロップ処理
                    int action = support.getDropAction();
                    if (action == COPY || action == MOVE) {
                        Transferable t = support.getTransferable();
                        try {
                            fileList = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
                            initList();
                        } catch (UnsupportedFlavorException e) {
                            return false;
                        } catch (IOException e) {
                            return false;
                        }
                    }
                    return true;
                }
                return false;
            }
        });

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(new JScrollPane(list), BorderLayout.CENTER);
		getContentPane().add(progressBar, BorderLayout.SOUTH);
	}

	private void initList() {

        listModel.removeAllElements();
        for (File file : fileList) {
        	listModel.addElement(file);
        }
	}

    public void editMeta() {
    	final List<File> selectList = list.getSelectedValuesList();
    	if (selectList != null && selectList.size() > 0) {

			setCursor(Cursor.getPredefinedCursor(
		            Cursor.WAIT_CURSOR));
			metaEditMenuItem.setEnabled(false);
    		new Thread() {
    			public void run() {
		    		progressBar.setVisible(true);
		    		progressBar.setValue(0);
		    		progressBar.setMaximum(fileList.size());
			    	MetaFrame frame = new MetaFrame(selectList, progressBar);
			    	frame.pack();
			    	frame.setVisible(true);
					progressBar.setValue(progressBar.getMaximum());
					progressBar.setVisible(false);

					setCursor(Cursor.getPredefinedCursor(
				            Cursor.DEFAULT_CURSOR));

					metaEditMenuItem.setEnabled(true);
    			}
    		}.start();
    	} else {
    		JOptionPane.showMessageDialog(this, "ファイルを選択してください");
    	}

    }

    public void showColorView() {
    	List<File> selectList = list.getSelectedValuesList();
    	if (selectList != null && selectList.size() > 0) {
            ColorViewFrame colorViewFrame = new ColorViewFrame(selectList);
            colorViewFrame.pack();
            colorViewFrame.setVisible(true);
    	} else {
    		JOptionPane.showMessageDialog(this, "ファイルを選択してください");
    	}
    }
}
