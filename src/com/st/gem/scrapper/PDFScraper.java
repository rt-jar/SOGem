package com.st.gem.scrapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class PDFScraper {

	private String url;

	private String processingLocation;

	void scrapThemAll(){
		int noOfPages = -1, i = 1;
		try {
			do {
				Document doc = Jsoup.connect(url+"&page_no="+i).get();
				noOfPages = (noOfPages == -1) ? Integer.parseInt(doc.select("a:contains(Last)").attr("data-ci-pagination-page")) : noOfPages;
				doc.select(".bid_no>a").stream().forEach(e -> {
					String fileURL = e.absUrl("href");
					try {
						System.out.println(fileURL);
						String savedFileName = e.text().replaceAll("/", "_").concat(".pdf");
						File toBeSaved = new File(processingLocation + "/" +savedFileName);
						if(!toBeSaved.exists()) {

							byte[] bytes = Jsoup.connect(fileURL)
									.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
									.referrer(url)
									.ignoreContentType(true)
									.maxBodySize(0)
									.timeout(600000)
									.execute()
									.bodyAsBytes();

							FileOutputStream fos = new FileOutputStream(toBeSaved);
							fos.write(bytes);
							fos.close();
							System.out.println("File has been downloaded.");
						}else {

							System.out.println("File has been already downloaded.");
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				});
				i++;
			}while(i <= noOfPages );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//Change download location and it will download all bids from GEM site
	public static void main(String[] args) {
		PDFScraper scrap = new PDFScraper("https://bidplus.gem.gov.in/bidlists?bidlists", "/home/ratnesh.kumar/test");
		scrap.scrapThemAll();
	}


}
