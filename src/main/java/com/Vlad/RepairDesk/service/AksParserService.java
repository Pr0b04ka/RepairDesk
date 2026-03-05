package com.Vlad.RepairDesk.service;

import com.Vlad.RepairDesk.dto.AksProductDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AksParserService {

    private static final String SEARCH_URL = "https://www.aks.ua/uk/search?for=";

    public List<AksProductDTO> search(String query) {
        List<AksProductDTO> results = new ArrayList<>();
        try {
            String url = SEARCH_URL + java.net.URLEncoder.encode(query, "UTF-8");
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .timeout(8000)
                    .get();

            Elements items = doc.select("div.catalog-item");
            for (Element item : items) {
                // Name + URL from image alt
                Element link = item.selectFirst("div.catalog-img a");
                Element img  = item.selectFirst("div.catalog-img a img");
                if (link == null || img == null) continue;

                String name    = img.attr("alt");
                String itemUrl = link.attr("href");

                // Price
                Element priceEl    = item.selectFirst("div.catalog-price-new");
                Element oldPriceEl = item.selectFirst("span.old-price__value");
                String price    = priceEl    != null ? priceEl.text()    : "—";
                String oldPrice = oldPriceEl != null ? oldPriceEl.text() : null;

                // Stock — active = in stock
                Element bottom = item.selectFirst("div[class*=catalog-item-id]");
                boolean inStock = bottom != null && bottom.hasClass("active");

                results.add(new AksProductDTO(name, price, oldPrice, itemUrl, inStock));
            }
        } catch (Exception e) {

        }
        return results;
    }
}