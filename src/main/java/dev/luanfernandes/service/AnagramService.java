package dev.luanfernandes.service;

import dev.luanfernandes.domain.request.AnagramRequest;
import dev.luanfernandes.domain.response.AnagramResponse;

public interface AnagramService {
    AnagramResponse generateAnagrams(AnagramRequest request);
}
