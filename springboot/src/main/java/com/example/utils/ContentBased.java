package com.example.utils;

import cn.hutool.core.collection.CollectionUtil;
import com.example.entity.EduExp;
import com.example.entity.Position;
import com.example.entity.ProExp;
import com.example.entity.Resume;
import com.example.entity.WorkExp;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Content-Based Recommendation
 * 基于内容的推荐算法
 */
public class ContentBased {

    private static final int DEFAULT_LIMIT = 10;
    private static final double TITLE_WEIGHT = 3.0;
    private static final double TAG_WEIGHT = 4.0;
    private static final double INDUSTRY_WEIGHT = 2.0;
    private static final double RESUME_SKILL_WEIGHT = 4.0;
    private static final double EXPERIENCE_WEIGHT = 2.0;
    private static final double EDUCATION_WEIGHT = 2.0;
    private static final double SALARY_WEIGHT = 1.5;
    private static final double HISTORY_WEIGHT = 2.5;
    private static final double CONTENT_WEIGHT = 1.0;
    private static final Pattern TOKEN_PATTERN = Pattern.compile("[\\p{IsHan}]+|[a-zA-Z0-9+#.]+|\\d+(?:\\.\\d+)?");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+(?:\\.\\d+)?");

    /**
     * 基于内容推荐岗位
     *
     * @param userId     当前用户ID
     * @param positions  所有岗位列表
     * @param resume     用户简历
     * @param userHistory 用户历史偏好关键词
     * @return {@link List<Integer>} 推荐的岗位id列表
     */
    public static List<Integer> recommend(Integer userId,
                                           List<Position> positions,
                                           Resume resume,
                                           List<String> userHistory) {
        if (CollectionUtil.isEmpty(positions) || resume == null) {
            return Collections.emptyList();
        }

        // 提取简历特征
        Map<String, Double> resumeFeatures = extractResumeFeatures(resume, userHistory);
        if (resumeFeatures.isEmpty()) {
            return Collections.emptyList();
        }

        // 计算每个岗位的匹配分数
        Map<Integer, Double> positionScores = new HashMap<>();

        for (Position position : positions) {
            if (position == null || position.getId() == null) {
                continue;
            }
            // 跳过未审核通过的岗位
            if (hasText(position.getStatus()) && !"审核通过".equals(position.getStatus())) {
                continue;
            }

            Map<String, Double> positionFeatures = extractPositionFeatures(position);
            if (positionFeatures.isEmpty()) {
                continue;
            }

            double score = calculateSimilarity(resumeFeatures, positionFeatures);
            if (score > 0) {
                positionScores.put(position.getId(), score);
            }
        }

        // 按分数排序，返回岗位id列表
        return positionScores.entrySet().stream()
                .sorted((a, b) -> {
                    int result = Double.compare(b.getValue(), a.getValue());
                    if (result != 0) {
                        return result;
                    }
                    return a.getKey().compareTo(b.getKey());
                })
                .limit(DEFAULT_LIMIT)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * 兼容旧调用：没有历史记录时只根据简历内容推荐
     */
    public static List<Integer> recommend(Integer userId,
                                           List<Position> positions,
                                           Resume resume) {
        return recommend(userId, positions, resume, Collections.emptyList());
    }

    /**
     * 提取简历特征向量
     */
    private static Map<String, Double> extractResumeFeatures(Resume resume, List<String> userHistory) {
        Map<String, Double> features = new HashMap<>();

        addCategoryFeature(features, "edu", resume.getEducation(), EDUCATION_WEIGHT);
        addCategoryFeature(features, "exp", resume.getExperience(), EXPERIENCE_WEIGHT);
        addSalaryFeatures(features, resume.getSalary(), SALARY_WEIGHT);

        // 简历中的工作、项目、专业和课程更能代表技能偏好
        addTextFeatures(features, resume.getWorkExps(), RESUME_SKILL_WEIGHT);
        addTextFeatures(features, resume.getProExps(), RESUME_SKILL_WEIGHT);
        addTextFeatures(features, resume.getEduExps(), EDUCATION_WEIGHT);

        if (CollectionUtil.isNotEmpty(resume.getWorkExpList())) {
            for (WorkExp workExp : resume.getWorkExpList()) {
                if (workExp == null) {
                    continue;
                }
                addTextFeatures(features, workExp.getPosition(), RESUME_SKILL_WEIGHT);
                addTextFeatures(features, workExp.getProject(), RESUME_SKILL_WEIGHT);
                addTextFeatures(features, workExp.getType(), RESUME_SKILL_WEIGHT);
                addTextFeatures(features, workExp.getContent(), RESUME_SKILL_WEIGHT);
            }
        }

        if (CollectionUtil.isNotEmpty(resume.getProExpList())) {
            for (ProExp proExp : resume.getProExpList()) {
                if (proExp == null) {
                    continue;
                }
                addTextFeatures(features, proExp.getName(), RESUME_SKILL_WEIGHT);
                addTextFeatures(features, proExp.getContent(), RESUME_SKILL_WEIGHT);
            }
        }

        if (CollectionUtil.isNotEmpty(resume.getEduExpList())) {
            for (EduExp eduExp : resume.getEduExpList()) {
                if (eduExp == null) {
                    continue;
                }
                addTextFeatures(features, eduExp.getSpeciality(), EDUCATION_WEIGHT);
                addTextFeatures(features, eduExp.getCourse(), EDUCATION_WEIGHT);
                addCategoryFeature(features, "edu", eduExp.getEducation(), EDUCATION_WEIGHT);
            }
        }

        if (CollectionUtil.isNotEmpty(userHistory)) {
            for (String history : userHistory) {
                addTextFeatures(features, history, HISTORY_WEIGHT);
            }
        }

        return features;
    }

    /**
     * 提取岗位特征向量
     */
    private static Map<String, Double> extractPositionFeatures(Position position) {
        Map<String, Double> features = new HashMap<>();

        addTextFeatures(features, position.getName(), TITLE_WEIGHT);
        addTextFeatures(features, position.getTags(), TAG_WEIGHT);
        addTextFeatures(features, position.getContent(), CONTENT_WEIGHT);
        addTextFeatures(features, position.getIndustryName(), INDUSTRY_WEIGHT);
        addTextFeatures(features, position.getType(), TAG_WEIGHT);
        addCategoryFeature(features, "edu", position.getEducation(), EDUCATION_WEIGHT);
        addCategoryFeature(features, "exp", position.getExperience(), EXPERIENCE_WEIGHT);
        addSalaryFeatures(features, position.getSalary(), SALARY_WEIGHT);

        if (CollectionUtil.isNotEmpty(position.getTagList())) {
            for (String tag : position.getTagList()) {
                addTextFeatures(features, tag, TAG_WEIGHT);
            }
        }

        if (position.getIndustryId() != null) {
            addFeature(features, "industry_" + position.getIndustryId(), INDUSTRY_WEIGHT);
        }

        return features;
    }

    /**
     * 计算两个特征向量的余弦相似度
     */
    private static double calculateSimilarity(Map<String, Double> features1,
                                               Map<String, Double> features2) {
        if (features1.isEmpty() || features2.isEmpty()) {
            return 0.0;
        }

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        // 计算第一个向量的模
        for (double value : features1.values()) {
            norm1 += value * value;
        }

        // 计算第二个向量的模
        for (double value : features2.values()) {
            norm2 += value * value;
        }

        // 计算点积（只计算共同特征）
        for (Map.Entry<String, Double> entry : features1.entrySet()) {
            String key = entry.getKey();
            double value1 = entry.getValue();
            Double value2 = features2.get(key);
            if (value2 != null) {
                dotProduct += value1 * value2;
            }
        }

        // 余弦相似度
        double denominator = Math.sqrt(norm1) * Math.sqrt(norm2);
        return denominator == 0 ? 0.0 : dotProduct / denominator;
    }

    /**
     * 增加分类字段特征，并拆分关键词用于宽松匹配
     */
    private static void addCategoryFeature(Map<String, Double> features, String prefix, String value, double weight) {
        if (!hasText(value)) {
            return;
        }
        addFeature(features, prefix + "_" + normalize(value), weight);
        addTextFeatures(features, value, weight * 0.5);
    }

    /**
     * 增加文本关键词特征
     */
    private static void addTextFeatures(Map<String, Double> features, String text, double weight) {
        if (!hasText(text)) {
            return;
        }

        Matcher matcher = TOKEN_PATTERN.matcher(text.toLowerCase(Locale.ROOT));
        while (matcher.find()) {
            String token = normalize(matcher.group());
            if (!hasText(token)) {
                continue;
            }
            addFeature(features, "kw_" + token, weight);
            addChineseNgrams(features, token, weight * 0.35);
        }
    }

    /**
     * 薪资范围按文本和数字区间同时建模，提升 15-20K 与 20-30K 这类范围的匹配能力
     */
    private static void addSalaryFeatures(Map<String, Double> features, String salary, double weight) {
        if (!hasText(salary)) {
            return;
        }
        addCategoryFeature(features, "salary", salary, weight);

        List<Double> numbers = extractNumbers(salary);
        if (numbers.isEmpty()) {
            return;
        }

        double min = Collections.min(numbers);
        double max = Collections.max(numbers);
        if (max < min) {
            return;
        }

        int start = (int) Math.floor(min / 5D) * 5;
        int end = (int) Math.ceil(max / 5D) * 5;
        for (int bucket = start; bucket <= end && bucket <= start + 100; bucket += 5) {
            addFeature(features, "salary_bucket_" + bucket, weight);
        }
    }

    private static List<Double> extractNumbers(String text) {
        List<Double> numbers = new ArrayList<>();
        Matcher matcher = NUMBER_PATTERN.matcher(text);
        while (matcher.find()) {
            numbers.add(Double.parseDouble(matcher.group()));
        }
        return numbers;
    }

    private static void addChineseNgrams(Map<String, Double> features, String token, double weight) {
        if (weight <= 0 || !containsChinese(token) || token.length() <= 2) {
            return;
        }
        for (int i = 0; i <= token.length() - 2; i++) {
            addFeature(features, "kw_" + token.substring(i, i + 2), weight);
        }
    }

    private static boolean containsChinese(String token) {
        for (int i = 0; i < token.length(); i++) {
            Character.UnicodeScript script = Character.UnicodeScript.of(token.charAt(i));
            if (script == Character.UnicodeScript.HAN) {
                return true;
            }
        }
        return false;
    }

    private static void addFeature(Map<String, Double> features, String key, double weight) {
        if (!hasText(key) || weight <= 0) {
            return;
        }
        features.merge(key, weight, Double::sum);
    }

    private static String normalize(String text) {
        return text == null ? "" : text.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", "");
    }

    private static boolean hasText(String text) {
        return text != null && !text.trim().isEmpty();
    }
}
