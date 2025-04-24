package com._live.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StreamServiceTest {

    private StreamService streamService;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        streamService = new StreamService();
        mapper = new ObjectMapper();
    }

    @Test
    void testNoDuplicatesInTop3() throws Exception {
        String inputJson = """
        [
          {
            "sectionID": "TestSection1",
            "sectionData": [
              { "streamerID": "A" },
              { "streamerID": "B" },
              { "streamerID": "C" },
              { "streamerID": "D" }
            ]
          }
        ]
        """;

        JsonNode result = streamService.removeDuplicateTopStreams(mapper.readTree(inputJson));
        assertEquals(4, result.get("TestSection1").size());
        assertEquals("A", result.get("TestSection1").get(0).asText());
        assertEquals("B", result.get("TestSection1").get(1).asText());
        assertEquals("C", result.get("TestSection1").get(2).asText());
    }

    @Test
    void testDuplicatesWithinTop3() throws Exception {
        String inputJson = """
        [
          {
            "sectionID": "TestSection2",
            "sectionData": [
              { "streamerID": "A" },
              { "streamerID": "A" },
              { "streamerID": "B" },
              { "streamerID": "C" },
              { "streamerID": "D" }
            ]
          }
        ]
        """;

        JsonNode result = streamService.removeDuplicateTopStreams(mapper.readTree(inputJson));
        assertEquals(4, result.get("TestSection2").size());
        assertTrue(result.get("TestSection2").toString().contains("C"));
    }

    @Test
    void testAllDuplicatesInTop3() throws Exception {
        String inputJson = """
        [
          {
            "sectionID": "TestSection3",
            "sectionData": [
              { "streamerID": "X" },
              { "streamerID": "X" },
              { "streamerID": "X" },
              { "streamerID": "Y" },
              { "streamerID": "Z" }
            ]
          }
        ]
        """;

        JsonNode result = streamService.removeDuplicateTopStreams(mapper.readTree(inputJson));
        assertEquals(3, result.get("TestSection3").size());
        assertTrue(result.get("TestSection3").toString().contains("Y"));
        assertTrue(result.get("TestSection3").toString().contains("Z"));
    }

    @Test
    void testLessThan3Streamers() throws Exception {
        String inputJson = """
        [
          {
            "sectionID": "TestSection4",
            "sectionData": [
              { "streamerID": "A" },
              { "streamerID": "B" }
            ]
          }
        ]
        """;

        JsonNode result = streamService.removeDuplicateTopStreams(mapper.readTree(inputJson));
        assertEquals(2, result.get("TestSection4").size());
    }

    @Test
    void testDuplicatesAfterTop3Allowed() throws Exception {
        String inputJson = """
        [
          {
            "sectionID": "TestSection5",
            "sectionData": [
              { "streamerID": "A" },
              { "streamerID": "B" },
              { "streamerID": "C" },
              { "streamerID": "A" },
              { "streamerID": "D" }
            ]
          }
        ]
        """;

        JsonNode result = streamService.removeDuplicateTopStreams(mapper.readTree(inputJson));
        assertEquals("A", result.get("TestSection5").get(0).asText());
        assertEquals("B", result.get("TestSection5").get(1).asText());
        assertEquals("C", result.get("TestSection5").get(2).asText());
        assertEquals("A", result.get("TestSection5").get(3).asText());
    }
}
