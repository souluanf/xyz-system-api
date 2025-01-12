package dev.luanfernandes.controller.impl;

import dev.luanfernandes.controller.AnagramController;
import dev.luanfernandes.domain.request.AnagramRequest;
import dev.luanfernandes.domain.response.AnagramResponse;
import dev.luanfernandes.service.AnagramService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AnagramControllerImpl implements AnagramController {

    private final AnagramService anagramService;

    @Override
    public ResponseEntity<AnagramResponse> generateAnagrams(AnagramRequest request) {
        return ResponseEntity.ok(anagramService.generateAnagrams(request));
    }
}
