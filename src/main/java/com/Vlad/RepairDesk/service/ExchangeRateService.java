package com.Vlad.RepairDesk.service;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {

    private static final String API_URL =
            "https://api.privatbank.ua/p24api/pubinfo?exchange&coursid=5";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public Map<String, BigDecimal> getRates() {
        Map<String, BigDecimal> rates = new HashMap<>();
        try {
            String json = restTemplate.getForObject(API_URL, String.class);
            JsonNode array = objectMapper.readTree(json);
            for (JsonNode node : array) {
                String ccy = node.get("ccy").asText();
                BigDecimal sale = new BigDecimal(node.get("sale").asText());
                rates.put(ccy, sale);
            }
        } catch (Exception e) {
            // fallback если API недоступен
            rates.put("USD", BigDecimal.valueOf(40.00));
            rates.put("EUR", BigDecimal.valueOf(50.00));
        }
        return rates;
    }

    public BigDecimal convertToUsd(BigDecimal uah, Map<String, BigDecimal> rates) {
        if (uah == null) return null;
        BigDecimal usdRate = rates.getOrDefault("USD", BigDecimal.valueOf(44));
        return uah.divide(usdRate, 2, RoundingMode.HALF_UP);
    }
}