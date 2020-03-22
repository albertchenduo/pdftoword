package com.example.pdftoword.pack2;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 没成功
 */
public class Pdf2word {

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
			Writer writer = new OutputStreamWriter(fos,"utf-8");
			PDFTextStripper stripper = new PDFTextStripper();
			stripper.setSortByPosition(true);
			stripper.setStartPage(1);
			stripper.setEndPage(pageNumber);
			stripper.writeText(doc, writer);

			List<PDImageXObject> imageList = getImageListFromPdf(doc,0);
			System.out.println("图片个数:"+ imageList.size());
			for (int i = 0;i<imageList.size();i++){
				writeImageInputStream(imageList.get(i));
			}


			writer.close();
			doc.close();
			System.out.println("pdf转换word成功");



		} catch (InvalidPasswordException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List<PDImageXObject> getImageListFromPdf(PDDocument document,Integer startPage) throws IOException {
		List<PDImageXObject> imageList = new ArrayList<>();
		if (null!=document){
			PDPageTree pages = document.getPages();
			startPage = startPage == null ? 0 : startPage;
			int len = pages.getCount();
			System.out.println("页数：" + len);
			if (startPage<len){
				for (int i = startPage;i<len;i++){
					PDPage pdPage = pages.get(i);
					if (pdPage!=null){
						if (pdPage.getResources()!=null){
							Iterable<COSName> xObjectNames = pdPage.getResources().getXObjectNames();
							for (COSName xObjectName : xObjectNames) {
								if (pdPage.getResources().isImageXObject(xObjectName)){
									imageList.add((PDImageXObject) pdPage.getResources().getXObject(xObjectName));
								}
							}
						}else {
							System.out.println("当前页没有图片");
						}
					}else {
						System.out.println("page is null");
					}
				}
			}
		}
		return imageList;
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


	public static void writeImageInputStream(PDImageXObject image) throws IOException {
		if (null!=image && null!=image.getImage()){
			Date date=new Date();
			String name = date.getTime()+"_image" ;
			File imgFile = new File("E:/images/" + name + "." + image.getSuffix());//写入的地址
			FileOutputStream fout = new FileOutputStream(imgFile);
			ByteArrayOutputStream os = new ByteArrayOutputStream();

			BufferedImage imageb = image.getImage();
			ImageIO.write(imageb, image.getSuffix(), os);
			InputStream is = new ByteArrayInputStream(os.toByteArray());
			int byteCount = 0;
			byte[] bytes = new byte[1024];
			while ((byteCount = is.read(bytes)) > 0) {
				fout.write(bytes, 0, byteCount);
			}
			fout.close();
			is.close();

		}
	}
}
