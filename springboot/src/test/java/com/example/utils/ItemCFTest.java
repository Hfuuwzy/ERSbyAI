package com.example.utils;

import com.example.entity.RelateDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ItemCFTest {

    @Test
    void recommendsItemsWithHigherUserOverlapFirst() {
        List<RelateDTO> data = List.of(
                relate(1, 10, 3),
                relate(2, 10, 1),
                relate(3, 10, 1),
                relate(4, 10, 1),
                relate(2, 20, 1),
                relate(3, 20, 1),
                relate(4, 20, 1),
                relate(2, 30, 1));

        List<Integer> recommendations = ItemCF.recommend(1, data, 10);

        assertThat(recommendations).containsExactly(20, 30);
    }

    @Test
    void ignoresItemsWithNoOverlappingUsers() {
        List<RelateDTO> data = List.of(
                relate(1, 10, 3),
                relate(2, 20, 1));

        List<Integer> recommendations = ItemCF.recommend(1, data, 10);

        assertThat(recommendations).isEmpty();
    }

    @Test
    void excludesItemsCurrentUserAlreadyInteractedWith() {
        List<RelateDTO> data = List.of(
                relate(1, 10, 3),
                relate(1, 20, 2),
                relate(2, 10, 1),
                relate(2, 20, 1),
                relate(2, 30, 1),
                relate(3, 10, 1),
                relate(3, 30, 1));

        List<Integer> recommendations = ItemCF.recommend(1, data, 10);

        assertThat(recommendations).containsExactly(30);
    }

    private RelateDTO relate(Integer userId, Integer positionId, Integer index) {
        return new RelateDTO(userId, positionId, index);
    }
}
