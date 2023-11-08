package com.example.dragonbackend.domain.enums;

public enum Difficulty {

    VERY_EASY_1("Piece of cake", 1),
    VERY_EASY_2("Sure thing", 1),
    VERY_EASY_3("Walk in the park", 1),
    EASY_1("Quite likely", 2),
    MEDIUM_1("Gamble", 3),
    HARD_1("Playing with fire", 4),
    HARD_2("Risky", 5),
    HARD_3("Hmmm....", 5),
    HARD_4("Rather detrimental", 5),
    VERY_HARD_1("Suicide mission", 6);

    private final String probability;
    private final int rank;

    Difficulty(String probability, int rank) {
        this.probability = probability;
        this.rank = rank;
    }

    public String getProbability() {
        return probability;
    }

    public int getRank() {
        return rank;
    }

    public static Difficulty fromProbability(String probability) {
        for (Difficulty difficulty : values()) {
            if (difficulty.getProbability().equalsIgnoreCase(probability)) {
                return difficulty;
            }
        }
        throw new IllegalArgumentException("Unknown probability: " + probability);
    }
}
