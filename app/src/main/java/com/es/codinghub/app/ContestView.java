package com.es.codinghub.app;

public class ContestView {

    private int judgeImage;
    private String contestName;
    private String timeToStart;

    public ContestView(int image, String contest, String start) {
        judgeImage = image;
        contestName = contest;
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

    public void setContestName(String contestName) {
        this.contestName = contestName;
    }

    public void setJudgeImage(int judgeImage) {
        this.judgeImage = judgeImage;
    }
}
