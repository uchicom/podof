/**
 * (c) 2013 uchicom
 */
package com.uchicom.podof;

import java.awt.BorderLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageTree;

/**
 * @author uchicom: Shigeki Uchiyama
 *
 */
public class TextMain extends JFrame {

	/**
     *
     */
	private static final long serialVersionUID = 1L;

	JProgressBar progressBar = new JProgressBar(JProgressBar.HORIZONTAL);
	JPanel panel = new JPanel(new BorderLayout());
	List<File> fileList;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<File> fileList = new ArrayList<File>();
		for (String arg : args) {
			fileList.add(new File(arg));
		}
		TextMain main = new TextMain(fileList);
		main.pack();
		main.setVisible(true);
	}

	private DefaultMutableTreeNode node = new DefaultMutableTreeNode("check一覧");
	private JTree tree;

	public TextMain(List<File> fileList) {
		super("PDFテキスト抽出");
		initComponents();

		if (fileList.size() > 0) {
			this.fileList = fileList;
			showResult();
		}
	}

	public void initComponents() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		tree = new JTree(node);
		JScrollPane scrollPane = new JScrollPane(tree);
		panel.add(scrollPane, BorderLayout.CENTER);
		panel.add(progressBar, BorderLayout.SOUTH);
		progressBar.setVisible(false);
		getContentPane().add(panel);
		tree.setTransferHandler(new TransferHandler() {
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
							node.removeAllChildren();
							showResult();
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

	}

	/**
	 * ファイルが指定されている場合に処理処理を実施する
	 *
	 * @param fileList
	 */
	public void showResult() {
		progressBar.setVisible(true);
		new Thread() {
			public void run() {
				progressBar.setValue(0);
				progressBar.setMaximum(fileList.size());
				for (int i = 0; i < fileList.size(); i++) {
					File file = fileList.get(i);
					List<Page> pageList = check(file);//2/5
					//ファイルノード
					DefaultMutableTreeNode child = new DefaultMutableTreeNode(file.getName());
					node.add(child);
					//計算結果ノード
					for (Calc calc : Calc.getCalcList(pageList)) {
						DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(calc.toString());
						child.add(leaf);
					}
				}
				progressBar.setValue(progressBar.getMaximum());//5/5
				((DefaultTreeModel) tree.getModel()).nodeStructureChanged(node);
				//展開表示
				for (int i = 0; i < tree.getRowCount(); i++) {
					tree.expandRow(i);
				}
				System.out.println(fileList);

				progressBar.setVisible(false);

			}
		}.start();
	}

	public List<Page> check(File file) {
		List<Page> resultList = new ArrayList<Page>();
		try (
				//PDF
				FileInputStream fis = new FileInputStream(file);
				//TXT
				FileOutputStream fos = new FileOutputStream("out.txt");
				PDDocument document = PDDocument.load(fis);) {

			PDPageTree pageList = document.getDocumentCatalog().getPages();
			for (int i = 0; i < pageList.getCount(); i++) {
				//                PDPage pdpage = pageList.get(i);
				//                COSStream stream = pdpage.getContents().getStream();
				//
				//            	java.io.InputStream is = stream.getUnfilteredStream();
				//
				//            	int ch = is.read();
				//            	boolean isText = false;
				//            	boolean isFirst = false;
				//            	StringBuffer strBuff = new StringBuffer();
				//            	while(ch > 0) {
				//            	    if (!isText && ch == '<') {
				//                        isText = true;
				//            	    } else if (isText) {
				//            	        if (ch == '>') {
				//            	            isText = false;
				//            	        } else {
				//            	            if (isFirst) {
				//            	                isFirst = false;
				//            	                strBuff.append((char)ch);
				//            	            } else {
				//            	                strBuff.append((char)ch);
				//            	            }
				//                        }
				//            	    }
				//
				//            	    ch = is.read();
				//            	}
				//                fos.write(new String(DatatypeConverter.parseHexBinary(strBuff.toString()),"MS932").getBytes("utf-8"));
				//                fos.write("\r\n".getBytes());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return resultList;
	}
}
