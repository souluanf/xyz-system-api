package dev.luanfernandes.service.impl;

import dev.luanfernandes.domain.request.AnagramRequest;
import dev.luanfernandes.domain.response.AnagramResponse;
import dev.luanfernandes.service.AnagramService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class AnagramServiceImpl implements AnagramService {

    @Override
    public AnagramResponse generateAnagrams(AnagramRequest anagramRequest) {
        String processedInput = anagramRequest
                .input()
                .chars()
                .distinct()
                .sorted()
                .mapToObj(c -> String.valueOf((char) c))
                .collect(Collectors.joining());

        List<String> result = new ArrayList<>();
        permute(processedInput.toCharArray(), 0, result);

        return new AnagramResponse(processedInput, result.size(), result);
    }

    private void permute(char[] chars, int index, List<String> result) {
        if (index == chars.length - 1) {
            result.add(new String(chars));
            return;
        }
        for (int i = index; i < chars.length; i++) {
            swap(chars, i, index);
            permute(chars, index + 1, result);
            swap(chars, i, index);
        }
    }

    private void swap(char[] chars, int i, int j) {
        if (i != j) {
            char temp = chars[i];
            chars[i] = chars[j];
            chars[j] = temp;
        }
    }
}
