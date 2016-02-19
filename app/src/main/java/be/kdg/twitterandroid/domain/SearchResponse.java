package be.kdg.twitterandroid.domain;

import java.util.List;

public class SearchResponse {
    private List<Tweet> statuses;

    public List<Tweet> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Tweet> statuses) {
        this.statuses = statuses;
    }
}
