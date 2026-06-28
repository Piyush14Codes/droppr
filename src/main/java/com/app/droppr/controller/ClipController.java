package com.app.droppr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/api/clip")
public class ClipController {

    private final AtomicReference<String> clipboard = new AtomicReference<>("");

    @PostMapping
    public String setClipboard(@RequestBody String text) {
        clipboard.set(text);
        return "Clipboard updated.";
    }

    @GetMapping
    public String getClipboard() {
        return clipboard.get();
    }
}
