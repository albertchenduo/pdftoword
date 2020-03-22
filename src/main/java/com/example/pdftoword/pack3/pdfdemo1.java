package com.example.pdftoword.pack3;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class pdfdemo1 {

	public static void main(String[] args) {
		String pdfFile = "E:\\1.pdf";
		try {
			PDDocument doc = PDDocument.load(new File(pdfFile));
			int pageNumber = doc.getNumberOfPages();
			pdfFile = pdfFile.substring(0,pdfFile.lastIndexOf("."));
			String filename = pdfFile + ".doc";
			File file = new File(filename);
			if (!file.exists()){
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(filename);
			//word
			XWPFDocument word = new XWPFDocument();

			PDPageTree pages = doc.getPages();
			for (int i=0;i<pageNumber;i++){
				PDPage pdPage = pages.get(i);
				if (pdPage!=null && pdPage.getResources()!=null){
					XWPFParagraph title = word.createParagraph();
					XWPFRun run = title.createRun();
					title.setAlignment(ParagraphAlignment.LEFT);
					title.setSpacingAfterLines(10);

					PDFTextStripper pdfStripper = new PDFTextStripper();
					pdfStripper.setSortByPosition(true);
					pdfStripper.setStartPage(i+1);
					pdfStripper.setEndPage(i+1);
					//获取当前页的文本
					String pageText = pdfStripper.getText(doc);
					String[] split = pageText.split("\r\n");
					for (String s : split) {
						run.addBreak();
						run.setText(s);
						run.addBreak();
					}
					title.addRun(run);

//					Iterable<COSName> xObjectNames = pdPage.getResources().getXObjectNames();
//					for (COSName xObjectName : xObjectNames) {
//						//是不是图片
//						if (pdPage.getResources().isImageXObject(xObjectName)){
//							XWPFParagraph title1 = word.createParagraph();
//							XWPFRun run1 = title1.createRun();
//							run1.setBold(true);
//							title1.setAlignment(ParagraphAlignment.CENTER);
//
//							//pdf中图片转换成流
//							PDImageXObject image = (PDImageXObject) pdPage.getResources().getXObject(xObjectName);
//							InputStream imageStream = getImageInputStream(image);
//							run1.addBreak();
//							run1.addPicture(imageStream,XWPFDocument.PICTURE_TYPE_JPEG, "picture",
//									Units.toEMU(image.getWidth()/2), Units.toEMU(image.getHeight()/2));
//							imageStream.close();
//							run1.addBreak();
//							title1.addRun(run1);
//						}
//					}
				}else {
					System.out.println("page is null");
				}
			}
			word.write(fos);
			fos.close();

		} catch (InvalidPasswordException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	public static InputStream getImageInputStream(PDImageXObject image) throws IOException {
		if (null!=image && null!=image.getImage()){
			BufferedImage bufferedImage = image.getImage();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage,image.getSuffix(),outputStream);
			return new ByteArrayInputStream(outputStream.toByteArray());
		}
		return null;
	}
}
