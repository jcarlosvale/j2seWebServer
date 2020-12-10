package com.test.king.dto;

import java.util.Objects;

/**
 * Represents the Score of an User in certain Level stored in the application.
 */
public class ScoreDto {
    private final int level;
    private final int score;
    private final int userId;

    public ScoreDto(final int level, final int score, final int userId) {
        this.level = level;
        this.score = score;
        this.userId = userId;
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
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ScoreDto scoreDto = (ScoreDto) o;
        return level == scoreDto.level && userId == scoreDto.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(level, userId);
    }
}
