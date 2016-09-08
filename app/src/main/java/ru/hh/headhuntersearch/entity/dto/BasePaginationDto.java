package ru.hh.headhuntersearch.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BasePaginationDto<T> {

    @JsonProperty("per_page")
    private Integer perPage;

    @JsonProperty("page")
    private Integer page;

    @JsonProperty("pages")
    private Integer pages;

    @JsonProperty("found")
    private Integer found;

    @JsonProperty("items")
    private List<T> items;

    public Integer getPerPage() {
        return perPage;
    }

    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public Integer getFound() {
        return found;
    }

    public void setFound(Integer found) {
        this.found = found;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
