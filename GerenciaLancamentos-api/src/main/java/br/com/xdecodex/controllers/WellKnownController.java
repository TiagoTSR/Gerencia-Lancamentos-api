package br.com.xdecodex.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/.well-known/appspecific")
public class WellKnownController {

    @GetMapping("/com.chrome.devtools.json")
    public ResponseEntity<Void> chromeDevtools() {
        return ResponseEntity.noContent().build(); 
    }
}
