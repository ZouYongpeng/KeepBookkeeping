package com.example.keepbookkeeping.events;

/**
 * @author 邹永鹏
 * @date 2019/2/21
 * @description :
 */
public class SearchAllDataEvent {

    private String word;

    public SearchAllDataEvent(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
