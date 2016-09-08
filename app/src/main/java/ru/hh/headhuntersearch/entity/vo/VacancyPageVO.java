package ru.hh.headhuntersearch.entity.vo;

import java.util.List;

public class VacancyPageVO extends BasePageVO<VacancyVO> {
    public VacancyPageVO(int pagesCount, int currentPage, List<VacancyVO> items) {
        super(pagesCount, currentPage, items);
    }
}
