package com.example.pdftoword.pack1;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.*;

/**
 * @Description
 * @Author chenduo
 * @Date 2020/3/17 14:59
 **/
public class PdfToWord {
	/**
	 * 转换
	 */
	public void convertText(String pdfPath) {
		PDDocument doc = null;
		OutputStream fos = null;
		Writer writer = null;
		PDFTextStripper stripper = null;
		try {
			doc = PDDocument.load(new File(pdfPath));
			fos = new FileOutputStream(pdfPath.substring(0, pdfPath.indexOf(".")) + ".doc");
			writer = new OutputStreamWriter(fos, "UTF-8");
			stripper = new PDFTextStripper();
			int pageNumber = doc.getNumberOfPages();
			stripper.setSortByPosition(true);
			stripper.setStartPage(1);
			stripper.setEndPage(pageNumber);
			stripper.writeText(doc, writer);
			writer.close();
			doc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("end..");
	}
}
