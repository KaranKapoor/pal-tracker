package io.pivotal.pal.tracker.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class EnvController {

    private String port,memory,index,address;
    public EnvController (
            @Value("${PORT: NOT SET}") String port,
            @Value("${MEMORY_LIMIT: NOT SET}") String memory,
            @Value("${CF_INSTANCE_INDEX: NOT SET}") String index,
            @Value("${CF_INSTANCE_ADDR: NOT SET}") String address
            ) {
        this.port = port;
        this.memory = memory;
        this.index = index;
        this.address = address;

    }

    @GetMapping("/env")
    public Map<String, String> getEnv() {

        Map<String, String> map = new HashMap<>();
        map.put("PORT", port);
        map.put("MEMORY_LIMIT", memory);
        map.put("CF_INSTANCE_INDEX", index);
        map.put("CF_INSTANCE_ADDR", address);

        return map;
    }

    @GetMapping("/test")
    public String getTest() {
        return "Test";
    }
}
