package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class TblQuestion {

    @SerializedName("ID")
    @Expose
    private Integer id;
    @SerializedName("QuestionID")
    @Expose
    private Integer questionID;
    @SerializedName("QuestionName")
    @Expose
    private String questionName;
    @SerializedName("OptionA")
    @Expose
    private String optionA;
    @SerializedName("OptionB")
    @Expose
    private String optionB;
    @SerializedName("OptionC")
    @Expose
    private String optionC;
    @SerializedName("OptionD")
    @Expose
    private String optionD;
    @SerializedName("CorrectAnswer")
    @Expose
    private String correctAnswer;
    @SerializedName("CreationDate")
    @Expose
    private String creationDate;
    @SerializedName("CreatedBy")
    @Expose
    private Integer createdBy;
    @SerializedName("QuestionNameHindi")
    @Expose
    private String questionNameHindi;
    @SerializedName("OptionAHindi")
    @Expose
    private String optionAHindi;
    @SerializedName("OptionBHindi")
    @Expose
    private String optionBHindi;
    @SerializedName("OptionCHindi")
    @Expose
    private String optionCHindi;
    @SerializedName("OptionDHindi")
    @Expose
    private String optionDHindi;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuestionID() {
        return questionID;
    }

    public void setQuestionID(Integer questionID) {
        this.questionID = questionID;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public String getQuestionNameHindi() {
        return questionNameHindi;
    }

    public void setQuestionNameHindi(String questionNameHindi) {
        this.questionNameHindi = questionNameHindi;
    }

    public String getOptionAHindi() {
        return optionAHindi;
    }

    public void setOptionAHindi(String optionAHindi) {
        this.optionAHindi = optionAHindi;
    }

    public String getOptionBHindi() {
        return optionBHindi;
    }

    public void setOptionBHindi(String optionBHindi) {
        this.optionBHindi = optionBHindi;
    }

    public String getOptionCHindi() {
        return optionCHindi;
    }

    public void setOptionCHindi(String optionCHindi) {
        this.optionCHindi = optionCHindi;
    }

    public String getOptionDHindi() {
        return optionDHindi;
    }

    public void setOptionDHindi(String optionDHindi) {
        this.optionDHindi = optionDHindi;
    }

}