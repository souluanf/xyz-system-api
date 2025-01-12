package dev.luanfernandes.service;

import static org.assertj.core.api.Assertions.assertThat;

import dev.luanfernandes.domain.request.AnagramRequest;
import dev.luanfernandes.domain.response.AnagramResponse;
import dev.luanfernandes.service.impl.AnagramServiceImpl;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AnagramServiceTest {

    @Spy
    @InjectMocks
    AnagramServiceImpl anagramService;

    @Test
    void generateAnagrams_ShouldReturnCorrectAnagrams() {
        String input = "abc";
        AnagramRequest request = new AnagramRequest(input);
        List<String> expectedAnagrams = List.of("abc", "acb", "bac", "bca", "cab", "cba");

        AnagramResponse response = anagramService.generateAnagrams(request);
        assertThat(response).isNotNull();
        assertThat(response.input()).isEqualTo("abc");
        assertThat(response.anagrams())
                .hasSameSizeAs(expectedAnagrams)
                .containsExactlyInAnyOrderElementsOf(expectedAnagrams);
        assertThat(response.totalAnagrams()).isEqualTo(expectedAnagrams.size());
    }

    @Test
    void generateAnagrams_ShouldHandleDuplicateCharacters() {
        String input = "aabb";
        AnagramRequest request = new AnagramRequest(input);
        List<String> expectedAnagrams = List.of("ab", "ba");
        AnagramResponse response = anagramService.generateAnagrams(request);
        assertThat(response).isNotNull();
        assertThat(response.input()).isEqualTo("ab");
        assertThat(response.anagrams())
                .hasSameSizeAs(expectedAnagrams)
                .containsExactlyInAnyOrderElementsOf(expectedAnagrams);
        assertThat(response.totalAnagrams()).isEqualTo(expectedAnagrams.size());
    }

    @Test
    void generateAnagrams_ShouldHandleEmptyInput() {
        String input = "";
        AnagramRequest request = new AnagramRequest(input);
        AnagramResponse response = anagramService.generateAnagrams(request);
        assertThat(response).isNotNull();
        assertThat(response.input()).isEmpty();
        assertThat(response.anagrams()).isEmpty();
        assertThat(response.totalAnagrams()).isZero();
    }
}
