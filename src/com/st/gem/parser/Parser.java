package com.st.gem.parser;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

public class Parser {
	//Currently supports single PDF parsing, should be extended to parse more
	public static void main(String[] args) throws IOException {
		try (PDDocument document = PDDocument.load(new File("/home/ratnesh.kumar/Downloads/GeM-Bidding-647305.pdf"))) {

			if (!document.isEncrypted()) {

				PDFTextStripperByArea stripper = new PDFTextStripperByArea();
				stripper.setSortByPosition(true);

				PDFTextStripper tStripper = new PDFTextStripper();

				String pdfFileInText = tStripper.getText(document);
				System.out.println("Text:" + pdfFileInText);
				List<java.lang.reflect.Field> metaData = getMetaData(BidVO.class);
				// split by whitespace
				String lines[] = pdfFileInText.split("\\r?\\n");
				List<String> fromFile = Arrays.asList(lines).stream().filter(StringUtils::isNotBlank).map(st -> st.trim()).collect(Collectors.toList());
				BidVO vo = populateVO(fromFile, metaData, pdfFileInText);
				System.out.println(vo.toString());
			}

		}

	}
	
	
	private static List<Field> getMetaData(Class<BidVO> bidvo) {
		
		return Arrays.asList(BidVO.class.getDeclaredFields());
		
	}


	static BidVO populateVO(List<String> dataFromFile, List<Field> voMetaData, String textInFile) {
		BidVO vo = new BidVO();
		vo.setTextContent(textInFile);
		dataFromFile.stream().filter(Objects::nonNull).peek(s -> {
			if(!s.contains("Technical Specifications")) {
				Optional<Field> key = voMetaData.stream().filter(t -> s.contains(t.getAnnotation(com.st.gem.parser.Field.class).value())).findFirst();
				
				try {
					if(key.isPresent()) {
						com.st.gem.parser.Field ann = key.get().getAnnotation(com.st.gem.parser.Field.class);
						String val = ann.value();
						key.get().setAccessible(true);
						if(ann.separator().equalsIgnoreCase("")) {
							val = s.substring(val.length(), s.length()).trim();
						}else {
							val = s.split(ann.separator())[1].trim();
						}
						
						key.get().set(vo, val);
					}else {
						vo.getAdditionalDtl().add(s);
					}
					
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			
		}).allMatch(s -> !s.contains("Technical Specifications"));
		return vo;
	}
	
}
