package com.darius.teamEventManager.controller;

import com.darius.teamEventManager.service.InitializationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/init")
public class InitController {
    @Autowired
    InitializationService initializationService;

    @GetMapping
    ResponseEntity<Void> initializeRoles() {
        log.debug("Initialization request");
        initializationService.Initialize();
        return ResponseEntity.ok().build();
    }
}
