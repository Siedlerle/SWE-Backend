package com.eventmaster.backend.entities;

public enum EnumQuestionType {
    MULTIPLECHOICE("multipleChoice"),
    TEXT("text");
    public final String questionType;

    EnumQuestionType(String questionType) {
        this.questionType = questionType;
    }
}
