package ru.pushapp.amazing_jewels;

import android.support.annotation.NonNull;

public class LeaderUnit implements Comparable {

    String name;
    int score;

    public LeaderUnit(String name, int score) {
        this.name = name;
        this.score = score;
    }


    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        int comparescore = ((LeaderUnit) o).getScore();
        return -(this.score - comparescore);
    }
}
