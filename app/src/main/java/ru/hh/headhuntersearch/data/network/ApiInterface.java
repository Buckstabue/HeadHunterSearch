package ru.hh.headhuntersearch.data.network;

import retrofit.http.GET;
import retrofit.http.Query;
import ru.hh.headhuntersearch.entity.dto.VacancyPaginationDto;

public interface ApiInterface {

    @GET("/vacancies")
    VacancyPaginationDto searchVacancy(@Query("text") String text,
                                       @Query("per_page") int perPage,
                                       @Query("page") int page);
}
