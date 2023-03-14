package ru.practicum;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StatsClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String appName;
    private final String start = "1970-01-01 00:00:00";
    private final String end = "2100-01-01 00:00:00";


    @Autowired
    public StatsClient(@Value("${stats-server.url}") String url,
                       @Value("${application.name}") String appName,
                       RestTemplateBuilder restTemplateBuilder,
                       ObjectMapper objectMapper) {
        this.restTemplate = restTemplateBuilder
                .uriTemplateHandler(new DefaultUriBuilderFactory(url))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
        this.objectMapper = objectMapper;
        this.appName = appName;
    }

    public void createHit(String uri, String ip) {
        String path = "/hit";
        HitDto hitDto = new HitDto();
        hitDto.setApp(appName);
        hitDto.setUri(uri);
        hitDto.setIp(ip);
        hitDto.setTimestamp(LocalDateTime.now());
        HttpEntity<Object> requestEntity = new HttpEntity<>(hitDto, defaultHeaders());
        restTemplate.exchange(path, HttpMethod.POST, requestEntity, Object.class);
    }

    public List<StatsDto> getAllStatsWithUniqueIp(Set<String> uris) {
        String path = "/stats?start={start}&end={end}&uris={uris}&unique={unique}";

        Map<String, Object> params = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", true
        );

        return sendStatsRequest(path, params);
    }

    public List<StatsDto> getAllStats(Set<String> uris) {
        String path = "/stats?start={start}&end={end}&uris={uris}";

        Map<String, Object> params = Map.of(
                "start", start,
                "end", end,
                "uris", uris
        );

        return sendStatsRequest(path, params);
    }

    private List<StatsDto> sendStatsRequest(String path, Map<String, Object> parameters) {
        ResponseEntity<Object[]> response = restTemplate.getForEntity(path, Object[].class, parameters);
        Object[] objects = response.getBody();
        if (objects != null) {
            return Arrays.stream(objects)
                    .map(object -> objectMapper.convertValue(object, StatsDto.class))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}