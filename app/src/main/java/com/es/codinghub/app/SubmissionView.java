package com.es.codinghub.app;

public class SubmissionView {

    private int judgeImage;
    private String problemName;
    private String languageName;
    private int verdictImage;

    public SubmissionView(int judge, String problem, String language, int verdict) {
        problemName = problem;
        languageName = language;
        judgeImage = judge;
        verdictImage = verdict;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public int getVerdictImage() {
        return verdictImage;
    }

    public void setVerdictImage(int verdictImage) {
        this.verdictImage = verdictImage;
    }

    public String getProblemName() {
        return problemName;
    }

    public void setProblemName(String problemName) {
        this.problemName = problemName;
    }

    public int getJudgeImage() {
        return judgeImage;
    }

    public void setJudgeImage(int judgeImage) {
        this.judgeImage = judgeImage;
    }
}
