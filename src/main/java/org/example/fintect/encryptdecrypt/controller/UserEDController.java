package org.example.fintect.encryptdecrypt.controller;

import lombok.RequiredArgsConstructor;
import org.example.fintect.encryptdecrypt.eesponsemodel.UserResponse;
import org.example.fintect.encryptdecrypt.entity.UserED;
import org.example.fintect.encryptdecrypt.requestemodel.UserRequest;
import org.example.fintect.encryptdecrypt.service.UserEDService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserEDController {


    private final UserEDService service;

    @PostMapping("/create/usered")
    public UserResponse save(@RequestBody UserRequest request) throws Exception {
        return service.save(request);
    }


    @GetMapping("/fetch/usered/{id}")
    public UserResponse get(@PathVariable Long id) throws Exception {
        return service.get(id);
    }
}
