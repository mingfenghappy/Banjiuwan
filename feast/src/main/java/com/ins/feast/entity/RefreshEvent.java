package com.ins.feast.entity;

/**
 * author 边凌
 * date 2017/3/6 9:39
 * desc ${TODO}
 */

public class RefreshEvent {
    private boolean shouldRefresh;

    public boolean isShouldRefresh() {
        return shouldRefresh;
    }

    public void setShouldRefresh(boolean shouldRefresh) {
        this.shouldRefresh = shouldRefresh;
    }

    public RefreshEvent(boolean shouldRefresh) {

        this.shouldRefresh = shouldRefresh;
    }
}
