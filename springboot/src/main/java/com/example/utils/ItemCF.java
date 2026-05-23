package com.example.utils;

import cn.hutool.core.collection.CollectionUtil;
import com.example.entity.RelateDTO;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ItemCF {

    /**
     * Item-based collaborative filtering
     *
     * @param userId 当前用户
     * @param list   用户岗位评分数据
     * @param topK   每个岗位取前K个相似岗位
     * @return {@link List<Integer>}
     */
    public static List<Integer> recommend(Integer userId, List<RelateDTO> list, int topK) {
        if (userId == null || CollectionUtil.isEmpty(list) || topK <= 0) {
            return Collections.emptyList();
        }

        // 岗位 -> 关联过该岗位的用户集合
        Map<Integer, Set<Integer>> itemUserMap = new HashMap<>();
        // 用户 -> 岗位 -> 相关性指数
        Map<Integer, Map<Integer, Integer>> userItemIndexMap = new HashMap<>();

        for (RelateDTO relateDTO : list) {
            if (relateDTO == null || relateDTO.getUserId() == null || relateDTO.getPositionId() == null) {
                continue;
            }
            Integer relateUserId = relateDTO.getUserId();
            Integer positionId = relateDTO.getPositionId();
            Integer index = relateDTO.getIndex() == null ? 0 : relateDTO.getIndex();

            itemUserMap.computeIfAbsent(positionId, key -> new HashSet<>()).add(relateUserId);
            userItemIndexMap.computeIfAbsent(relateUserId, key -> new HashMap<>()).merge(positionId, index, Integer::sum);
        }

        Map<Integer, Integer> userItems = userItemIndexMap.get(userId);
        if (userItems == null || userItems.isEmpty()) {
            return Collections.emptyList();
        }

        Set<Integer> interactedItems = userItems.keySet();
        Map<Integer, Double> candidateScoreMap = new HashMap<>();

        userItems.forEach((positionId, index) -> {
            Set<Integer> currentItemUsers = itemUserMap.get(positionId);
            if (CollectionUtil.isEmpty(currentItemUsers)) {
                return;
            }

            List<Map.Entry<Integer, Double>> similarItems = itemUserMap.entrySet().stream()
                    .filter(entry -> !entry.getKey().equals(positionId))
                    .filter(entry -> !interactedItems.contains(entry.getKey()))
                    .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), cosineSimilarity(currentItemUsers, entry.getValue())))
                    .filter(entry -> entry.getValue() > 0)
                    .sorted((a, b) -> {
                        int result = Double.compare(b.getValue(), a.getValue());
                        if (result != 0) {
                            return result;
                        }
                        return a.getKey().compareTo(b.getKey());
                    })
                    .limit(topK)
                    .collect(Collectors.toList());

            similarItems.forEach(entry -> candidateScoreMap.merge(entry.getKey(), entry.getValue() * index, Double::sum));
        });

        if (candidateScoreMap.isEmpty()) {
            return Collections.emptyList();
        }

        return candidateScoreMap.entrySet().stream()
                .sorted((a, b) -> {
                    int result = Double.compare(b.getValue(), a.getValue());
                    if (result != 0) {
                        return result;
                    }
                    return a.getKey().compareTo(b.getKey());
                })
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * Calculate cosine similarity between two items
     */
    private static double cosineSimilarity(Set<Integer> users1, Set<Integer> users2) {
        if (CollectionUtil.isEmpty(users1) || CollectionUtil.isEmpty(users2)) {
            return 0D;
        }
        Set<Integer> intersection = new HashSet<>(users1);
        intersection.retainAll(users2);
        if (intersection.isEmpty()) {
            return 0D;
        }
        return intersection.size() / Math.sqrt(users1.size() * users2.size());
    }
}
