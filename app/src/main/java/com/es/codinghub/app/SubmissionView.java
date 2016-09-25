package com.es.codinghub.app;

public class SubmissionView {

    private int judgeImage;
    private String problemName;
    private String timeSubmitted;
    private String languageName;
    private int verdictImage;

    public SubmissionView(int judge, String problem, String time, String language, int verdict) {
        problemName = problem;
        timeSubmitted = time;
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

    public String getTimeSubmitted() {
        return timeSubmitted;
    }

    public void setTimeSubmitted(String timeSubmitted) {
        this.timeSubmitted = timeSubmitted;
    }

    public int getJudgeImage() {
        return judgeImage;
    }

    public void setJudgeImage(int judgeImage) {
        this.judgeImage = judgeImage;
    }
}
