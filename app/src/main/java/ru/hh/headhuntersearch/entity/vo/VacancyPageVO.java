package ru.hh.headhuntersearch.entity.vo;

import java.util.List;

public class VacancyPageVO extends BasePageVO<VacancyVO> {
    public VacancyPageVO(int pagesCount, int currentPage, List<VacancyVO> items, int totalItemsCount) {
        super(pagesCount, currentPage, items, totalItemsCount);
    }
}
