/**
 * (c) 2013 uchicom
 */
package com.uchicom.podof;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
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
import org.apache.pdfbox.rendering.PDFRenderer;

/**
 * @author uchicom: Shigeki Uchiyama
 *
 */
public class ViewerMain extends JFrame {

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
		ViewerMain main = new ViewerMain(fileList);
		main.pack();
		main.setVisible(true);
	}

	private DefaultMutableTreeNode node = new DefaultMutableTreeNode("check一覧");
	private JTree tree;

	public ViewerMain(List<File> fileList) {
		super("PDFViewer");
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
		try (FileInputStream fis = new FileInputStream(file);
				PDDocument document = PDDocument.load(fis);) {

			PDPageTree pageList = document.getPages();
			progressBar.setMaximum(progressBar.getMaximum() + pageList.getCount() * 3);//5
			progressBar.setValue(progressBar.getValue() + pageList.getCount());//1/5ファイル読みこみで

			PDFRenderer pdfRenderer = new PDFRenderer(document);
			for (int i = 0; i < pageList.getCount(); i++) {
				Page page = new Page(i);
				resultList.add(page);
				progressBar.setValue(progressBar.getValue() + 1);//1/5ファイル全部で
				final BufferedImage image = pdfRenderer.renderImage(i);
				JFrame frame = new JFrame("test");

				JPanel panel = new JPanel() {
					public void paint(Graphics g) {
						Graphics2D g2 = (Graphics2D) g;
						g2.drawImage(image, 0, 0, this);
						g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
								RenderingHints.VALUE_ANTIALIAS_ON);
					}
				};
				panel.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
				frame.getContentPane().add(new JScrollPane(panel));
				frame.pack();
				frame.setVisible(true);
				page.addType(new com.uchicom.podof.Type(image.getColorModel().getColorSpace().getType()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return resultList;
	}

}
