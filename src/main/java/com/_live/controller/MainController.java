package com._live.controller;

import com._live.service.StreamService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/streams")
public class MainController {

    @Autowired
    private StreamService streamService;

    @PostMapping("/deduplicate")
    public JsonNode deduplicateTop3Streams(@RequestBody JsonNode inputJson) {
        return streamService.removeDuplicateTopStreams(inputJson);
    }
}
