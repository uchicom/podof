/**
 * (c) 2013 uchicom
 */
package com.uchicom.podof;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

/**
 * @author uchicom: Shigeki Uchiyama
 *
 */
public class TrimMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for (String arg : args) {
			change(arg);
		}

	}

	@SuppressWarnings("unchecked")
	public static void change(String filename) {

		System.out.println(filename);
		System.out.println("----------------------------------------");
		try (FileInputStream fis = new FileInputStream(filename);
				PDDocument document = PDDocument.load(fis);) {

			PDPageTree pageList = document.getDocumentCatalog().getPages();
			float pixel_bleed = ((int) Math.ceil(3.17 * 600 / 25.4)) * 72 / 600f;
			for (int i = 0; i < pageList.getCount(); i++) {
				PDPage pdpage = pageList.get(i);

				System.out.println("getCropBox:" + pdpage.getCropBox());
				System.out.println("getMediaBox:" + pdpage.getMediaBox());
				System.out.println("getArtBox:" + pdpage.getArtBox());
				System.out.println("getBleedBox:" + pdpage.getBleedBox());
				System.out.println("getTrimBox:" + pdpage.getTrimBox());

				//クリアする
				pdpage.setCropBox(null);
				pdpage.setArtBox(null);

				PDRectangle bleedBox = pdpage.getMediaBox();
				//                bleedBox.setUpperRightX(value);
				//                bleedBox.setUpperRightY(value);
				//                bleedBox.setLowerLeftX(value);
				//                bleedBox.setLowerLeftY(value);

				System.out.println("----------------------------------------");
				System.out.println("setBleedBox:" + bleedBox);
				pdpage.setBleedBox(bleedBox);

				PDRectangle trimBox = new PDRectangle();
				trimBox.setUpperRightX(bleedBox.getUpperRightX() - pixel_bleed);
				trimBox.setUpperRightY(bleedBox.getUpperRightY() - pixel_bleed);
				trimBox.setLowerLeftX(pixel_bleed);
				trimBox.setLowerLeftY(pixel_bleed);
				pdpage.setTrimBox(trimBox);
				System.out.println("setTrimBox:" + trimBox);

			}
			document.save(filename + "_bleedtrim.pdf");

			System.out.println("============================================");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
