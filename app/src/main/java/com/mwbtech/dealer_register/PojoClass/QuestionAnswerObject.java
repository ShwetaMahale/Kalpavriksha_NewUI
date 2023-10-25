package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.SerializedName;

public class QuestionAnswerObject {

    @SerializedName("QuestionID")
    private int QuestionID;

    @SerializedName("QuestionName")
    private String QuestionName;

    @SerializedName("QuestionNameHindi")
    private String QuestionNameHindi;


    @SerializedName("OptionA")
    private String OptionA;

    @SerializedName("OptionAHindi")
    private String OptionAHindi;

    @SerializedName("OptionB")
    private String OptionB;

    @SerializedName("OptionBHindi")
    private String OptionBHindi;

    @SerializedName("OptionC")
    private String OptionC;

    @SerializedName("OptionCHindi")
    private String OptionCHindi;

    @SerializedName("OptionD")
    private String OptionD;

    @SerializedName("OptionDHindi")
    private String OptionDHindi;

    @SerializedName("CorrectAnswer")
    private String CorrectAnswer;

    public int getQuestionID() {
        return QuestionID;
    }

    public void setQuestionID(int questionID) {
        QuestionID = questionID;
    }

    public String getQuestionName() {
        return QuestionName;
    }

    public void setQuestionName(String questionName) {
        QuestionName = questionName;
    }

    public String getOptionA() {
        return OptionA;
    }

    public void setOptionA(String optionA) {
        OptionA = optionA;
    }

    public String getOptionB() {
        return OptionB;
    }

    public void setOptionB(String optionB) {
        OptionB = optionB;
    }

    public String getOptionC() {
        return OptionC;
    }

    public void setOptionC(String optionC) {
        OptionC = optionC;
    }

    public String getOptionD() {
        return OptionD;
    }

    public void setOptionD(String optionD) {
        OptionD = optionD;
    }

    public String getCorrectAnswer() {
        return CorrectAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        CorrectAnswer = correctAnswer;
    }

    public String getQuestionNameHindi() {
        return QuestionNameHindi;
    }

    public void setQuestionNameHindi(String questionNameHindi) {
        QuestionNameHindi = questionNameHindi;
    }

    public String getOptionAHindi() {
        return OptionAHindi;
    }

    public void setOptionAHindi(String optionAHindi) {
        OptionAHindi = optionAHindi;
    }

    public String getOptionBHindi() {
        return OptionBHindi;
    }

    public void setOptionBHindi(String optionBHindi) {
        OptionBHindi = optionBHindi;
    }

    public String getOptionCHindi() {
        return OptionCHindi;
    }

    public void setOptionCHindi(String optionCHindi) {
        OptionCHindi = optionCHindi;
    }

    public String getOptionDHindi() {
        return OptionDHindi;
    }

    public void setOptionDHindi(String optionDHindi) {
        OptionDHindi = optionDHindi;
    }

    @Override
    public String toString() {
        return "QuestionAnswerObject{" +
                "QuestionID=" + QuestionID +
                ", QuestionName='" + QuestionName + '\'' +
                ", QuestionNameHindi='" + QuestionNameHindi + '\'' +
                ", OptionA='" + OptionA + '\'' +
                ", OptionAHindi='" + OptionAHindi + '\'' +
                ", OptionB='" + OptionB + '\'' +
                ", OptionBHindi='" + OptionBHindi + '\'' +
                ", OptionC='" + OptionC + '\'' +
                ", OptionCHindi='" + OptionCHindi + '\'' +
                ", OptionD='" + OptionD + '\'' +
                ", OptionDHindi='" + OptionDHindi + '\'' +
                ", CorrectAnswer='" + CorrectAnswer + '\'' +
                '}';
    }
}
