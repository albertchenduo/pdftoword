package com.example.pdftoword.util;

import com.example.pdftoword.entity.PdfToWord;

/**
 * @Description
 * @Author chenduo
 * @Date 2020/3/17 15:02
 **/
public class TestUtil {

	public static void main(String[] args) {
		PdfToWord convert=new PdfToWord();
		convert.convertText("E:\\2020年初中英语分类汇编之单项选择.pdf");
	}
}
