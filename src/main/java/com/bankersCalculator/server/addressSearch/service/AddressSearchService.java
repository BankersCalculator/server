package com.bankersCalculator.server.addressSearch.service;

import com.bankersCalculator.server.addressSearch.dto.AddressSearchResponse;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AddressSearchService {

    private final RestTemplate restTemplate;
    private final XmlMapper xmlMapper;

    public AddressSearchService() {
        this.restTemplate = new RestTemplate();
        this.xmlMapper = new XmlMapper(); // Use XmlMapper for XML parsing
    }

    public AddressSearchResponse searchAddress(String keyword) {
        String url = "https://business.juso.go.kr/addrlink/addrLinkApi.do?confmKey=devU01TX0FVVEgyMDI0MDgwNTIxMTIzNjExNDk4OTM=&currentPage=1&countPerPage=10&keyword="+keyword;

        String response = restTemplate.getForObject(url, String.class);

        System.out.println("API Response: " + response); // Print the XML response

        try {
            // Parse the XML response
            return xmlMapper.readValue(response, AddressSearchResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse response", e);
        }
    }
}
