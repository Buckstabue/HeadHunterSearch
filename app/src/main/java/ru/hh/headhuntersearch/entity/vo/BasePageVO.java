package ru.hh.headhuntersearch.entity.vo;

import java.util.List;

public class BasePageVO<T> {
    private int pagesCount;
    private int currentPage;
    private List<T> items;

    public BasePageVO(int pagesCount, int currentPage, List<T> items) {
        this.pagesCount = pagesCount;
        this.currentPage = currentPage;
        this.items = items;
    }

    public int getPagesCount() {
        return pagesCount;
    }

    public void setPagesCount(int pagesCount) {
        this.pagesCount = pagesCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
