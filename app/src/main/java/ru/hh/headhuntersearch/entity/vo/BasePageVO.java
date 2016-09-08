package ru.hh.headhuntersearch.entity.vo;

import java.util.List;

public class BasePageVO<T> {
    private int pagesCount;
    private int currentPage;
    private List<T> items;
    private int totalItemsCount;

    public BasePageVO(int pagesCount, int currentPage, List<T> items, int totalItemsCount) {
        this.pagesCount = pagesCount;
        this.currentPage = currentPage;
        this.items = items;
        this.totalItemsCount = totalItemsCount;
    }

    public int getPagesCount() {
        return pagesCount;
    }

    public void setPagesCount(int pagesCount) {
        this.pagesCount = pagesCount;
    }

    public int getPageNumber() {
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

    public int getTotalItemsCount() {
        return totalItemsCount;
    }

    public void setTotalItemsCount(int totalItemsCount) {
        this.totalItemsCount = totalItemsCount;
    }
}
