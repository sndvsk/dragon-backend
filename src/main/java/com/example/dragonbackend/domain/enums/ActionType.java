package com.example.dragonbackend.domain.enums;

public enum ActionType {
    HELP("Help", 1),
    CREATE("Create", 1),
    ESCORT("Escort", 1),
    RESCUE("Rescue", 1),
    STEAL("Steal", 2);

    private final String action;
    private final int priority;

    ActionType(String action, int priority) {
        this.action = action;
        this.priority = priority;
    }

    public String getAction() {
        return action;
    }

    public int getPriority() {
        return priority;
    }

    public static ActionType fromMessage(String message) {
        String firstWord = message.split("\\s+")[0];
        for (ActionType actionType : values()) {
            if (actionType.getAction().equalsIgnoreCase(firstWord)) {
                return actionType;
            }
        }
        throw new IllegalArgumentException("Unknown action in message: " + firstWord);
    }
}
