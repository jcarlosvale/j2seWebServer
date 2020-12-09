package com.test.king.dto;

import java.util.Objects;

public class ScoreDto {
    private final int level;
    private final int score;
    private final int userId;

    public ScoreDto(int level, int score, int userId) {
        this.level = level;
        this.score = score;
        this.userId = userId;
    }

    public int getLevel() {
        return level;
    }

    public int getScore() {
        return score;
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "ScoreDto{" + "level=" + level + ", score=" + score + ", userId=" + userId + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScoreDto scoreDto = (ScoreDto) o;
        return level == scoreDto.level && score == scoreDto.score && userId == scoreDto.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(level, score, userId);
    }
}
