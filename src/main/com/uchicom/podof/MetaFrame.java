// (c) 2016 uchicom
package com.uchicom.podof;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.common.PDMetadata;

import com.uchicom.podof.action.UpdateAction;

/**
 * @author uchicom: Shigeki Uchiyama
 *
 */
public class MetaFrame extends JFrame {
	JTable table;
	List<File> fileList;
	String[][] data = null;
	JTextField applicationTextField = new JTextField();
	JTextField pdfTransferTextField = new JTextField();
	JButton updateButton = new JButton(new UpdateAction(this));
	private JProgressBar progressBar = new JProgressBar(JProgressBar.HORIZONTAL);

	/**
	 * @throws HeadlessException
	 */
	public MetaFrame(List<File> fileList, JProgressBar progressBar) throws HeadlessException {
		super("メタ情報編集画面");
		this.fileList = fileList;
		data = new String[fileList.size()][5];
		initComponents(progressBar);
	}

	private void initComponents(JProgressBar progressBar) {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.progressBar.setVisible(false);
		//メタ情報テキストフィールド・エリア
		//JTable
		init(data, progressBar);
		table = new JTable(data, new String[] { "ファイル名", "アプリケーション", "PDF変換", "アプリケーション（XMP）", "PDF変換（XMP）" });
		table.setEnabled(false);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setRightComponent(new JScrollPane(table));
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		panel.add(new JLabel("アプリケーション"), constraints);
		constraints.gridy = 1;
		panel.add(new JLabel("PDF変換"), constraints);
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.BOTH;
		panel.add(applicationTextField, constraints);
		constraints.gridy = 1;
		panel.add(pdfTransferTextField, constraints);
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.NORTH;
		panel.add(updateButton, constraints);
		splitPane.setLeftComponent(panel);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(splitPane, BorderLayout.CENTER);
		getContentPane().add(this.progressBar, BorderLayout.SOUTH);

	}

	public void updateMeta() {
		updateButton.setEnabled(false);
		setCursor(Cursor.getPredefinedCursor(
	            Cursor.WAIT_CURSOR));
		new Thread() {
			public void run() {
				progressBar.setVisible(true);
				progressBar.setValue(0);
				progressBar.setMaximum(fileList.size() * 2);
				for (int i = 0; i < fileList.size(); i++) {
					File file = fileList.get(i);
					try (PDDocument doc = PDDocument.load(file, MemoryUsageSetting.setupTempFileOnly());){

						doc.getDocumentInformation().setCreator(applicationTextField.getText());
						doc.getDocumentInformation().setProducer(pdfTransferTextField.getText());
						PDDocumentCatalog documentCatalog = doc.getDocumentCatalog();
						String xml = null;
						if (documentCatalog.getMetadata() != null) {
							xml = MetaFrame.toString(documentCatalog.getMetadata().exportXMPMetadata());
							xml = xml.replaceAll("^[^<]*", "");
							xml = xml.replaceAll(":Producer>[^<]*</", ":Producer>"
									+ escapeXml(pdfTransferTextField.getText())
									+ "</");
							xml = xml.replaceAll(":CreatorTool>[^<]*</", ":CreatorTool>"
									+ escapeXml(applicationTextField.getText())
									+ "</");
							System.out.println(xml);
						} else {
							xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
									+ "<x:xmpmeta xmlns:x=\"adobe:ns:meta/\">"
									+ "<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns:pdf=\"http://ns.adobe.com/pdf/1.3/\"  xmlns:dc=\"http://purl.org/dc/elements/1.1/\">"
									+ "<rdf:Description rdf:about=\"\">"
									+ "<pdf:Producer>"
									+ escapeXml(pdfTransferTextField.getText())
									+ "</pdf:Producer>"
									+ "<pdf:CreatorTool>"
									+ escapeXml(applicationTextField.getText())
									+ "</pdf:CreatorTool>"
									+ "</rdf:Description>"
									+ "</rdf:RDF>"
									+ "</x:xmpmeta>";
						}

						ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes("utf-8"));
						PDMetadata newMetadata = new PDMetadata(doc, bais);
						documentCatalog.setMetadata(newMetadata);
						doc.save(file);
						table.repaint();
					} catch (IOException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}

					progressBar.setValue(progressBar.getValue()+ 1);
				}
				init(data, progressBar);

				progressBar.setValue(progressBar.getMaximum());
				progressBar.setVisible(false);

				updateButton.setEnabled(true);
				table.repaint();

				setCursor(Cursor.getPredefinedCursor(
			            Cursor.DEFAULT_CURSOR));
			}
		}.start();
	}

	public void init(String[][] data, JProgressBar progressBar) {
		for (int i = 0; i < fileList.size(); i++) {
			File file = fileList.get(i);
			try (PDDocument doc = PDDocument.load(file, MemoryUsageSetting.setupTempFileOnly());) {

				data[i][0] = file.getName();
				data[i][1] = doc.getDocumentInformation().getCreator();
				data[i][2] = doc.getDocumentInformation().getProducer();
				PDDocumentCatalog documentCatalog = doc.getDocumentCatalog();
				System.out.println(file.getName());
				if (documentCatalog.getMetadata() != null) {
					String xml = toString(documentCatalog.getMetadata().exportXMPMetadata());
					System.out.println("[" + xml + "]");
					data[i][3] = unescapeXml(xml.replaceAll("(?s)(.*<[^/<>]*:CreatorTool>)|(</[^/<>]*:CreatorTool>.*)",
							""));
					if (xml.equals(data[i][3])) {
						data[i][3] = "";
					}
					data[i][4] = unescapeXml(xml.replaceAll("(?s)(.*<[^/<>]*:Producer>)|(</[^/<>]*:Producer>.*)", ""));
					if (xml.equals(data[i][4])) {
						data[i][4] = "";
					}
				}
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			progressBar.setValue(progressBar.getValue()+ 1);
		}
	}

	public static String escapeXml(String text) {
		String escapeText = text.replaceAll("&", "&amp;");
		escapeText = escapeText.replaceAll("<", "&lt;");
		escapeText = escapeText.replaceAll(">", "&gt;");
		return escapeText;
	}

	public static String unescapeXml(String text) {
		String escapeText = text.replaceAll("&gt;", ">");
		escapeText = escapeText.replaceAll("&lt;", "<");
		escapeText = escapeText.replaceAll("&amp;", "&");
		return escapeText;
	}

	public static String toString(InputStream is) throws IOException {
		ByteArrayOutputStream strBuff = new ByteArrayOutputStream();
		byte[] bytes = new byte[1024];
		int length = 0;
		while ((length = is.read(bytes)) > 0) {
			strBuff.write(bytes, 0, length);
		}
		return new String(strBuff.toByteArray(), "utf-8");
	}
}
