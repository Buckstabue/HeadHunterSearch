package ru.hh.headhuntersearch.entity.converter;

import java.util.ArrayList;
import java.util.List;

import ru.hh.headhuntersearch.entity.dto.VacancyDto;
import ru.hh.headhuntersearch.entity.dto.VacancyPaginationDto;
import ru.hh.headhuntersearch.entity.vo.VacancyPageVO;
import ru.hh.headhuntersearch.entity.vo.VacancyVO;

public class DtoToVoConverter {

    public VacancyVO toVacancyVO(VacancyDto dto) {
        VacancyVO vacancyVO = new VacancyVO(dto.getName(), dto.getEmployer().getName());
        return vacancyVO;
    }

    public VacancyPageVO toVacancyPageVO(VacancyPaginationDto dto) {
        List<VacancyDto> dtoItems = dto.getItems();
        List<VacancyVO> voItems = new ArrayList<>(dtoItems.size());
        for (VacancyDto dtoItem : dtoItems) {
            voItems.add(toVacancyVO(dtoItem));
        }
        return new VacancyPageVO(dto.getPages(), dto.getPage(), voItems, dto.getFound());
    }
}
