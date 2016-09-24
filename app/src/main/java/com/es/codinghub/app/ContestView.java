package com.es.codinghub.app;

public class ContestView {

    private int judgeImage;
    private String contestName;
    private String judgeName;
    private String timeToStart;

    public ContestView(int image, String contest, String judge, String start) {
        judgeImage = image;
        contestName = contest;
        judgeName = judge;
        timeToStart = start;
    }

    public String getTimeToStart() {
        return timeToStart;
    }

    public void setTimeToStart(String timeToStart) {
        this.timeToStart = timeToStart;
    }

    public int getJudgeImage() {
        return judgeImage;
    }

    public String getContestName() {
        return contestName;
    }

    public String getJudgeName() {
        return judgeName;
    }

    public void setJudgeName(String judgeName) {
        this.judgeName = judgeName;
    }

    public void setContestName(String contestName) {
        this.contestName = contestName;
    }

    public void setJudgeImage(int judgeImage) {
        this.judgeImage = judgeImage;
    }
}
