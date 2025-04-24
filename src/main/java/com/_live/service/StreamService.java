package com._live.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


@Service
public class StreamService {

    private final ObjectMapper mapper = new ObjectMapper();

    public ObjectNode removeDuplicateTopStreams(JsonNode input) {
        ObjectNode result = mapper.createObjectNode();

        for (JsonNode section : input) {
            String sectionID = section.get("sectionID").asText();
            ArrayNode sectionData = (ArrayNode) section.get("sectionData");

            List<String> allStreams = new ArrayList<>();
            for (JsonNode stream : sectionData) {
                allStreams.add(stream.get("streamerID").asText());
            }

            Set<String> seen = new LinkedHashSet<>();
            List<String> top3Unique = new ArrayList<>();

            int i = 0;
            while (top3Unique.size() < 3 && i < allStreams.size()) {
                String current = allStreams.get(i++);
                if (!seen.contains(current)) {
                    top3Unique.add(current);
                    seen.add(current);
                }
            }

            // Fill rest of the list
            List<String> finalList = new ArrayList<>(top3Unique);
            for (int j = i; j < allStreams.size(); j++) {
                finalList.add(allStreams.get(j));
            }

            result.putPOJO(sectionID, finalList);
        }

        return result;
    }
}

