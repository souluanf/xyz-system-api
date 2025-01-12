package dev.luanfernandes.domain.response;

import java.util.List;

public record AnagramResponse(String input, Integer totalAnagrams, List<String> anagrams) {}
