package ru.hh.headhuntersearch.entity.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class VacancyVO implements Parcelable {
    private String vacancyName;
    private String employerName;

    public VacancyVO(String vacancyName, String employerName) {
        this.vacancyName = vacancyName;
        this.employerName = employerName;
    }

    public String getVacancyName() {
        return vacancyName;
    }

    public void setVacancyName(String vacancyName) {
        this.vacancyName = vacancyName;
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.vacancyName);
        dest.writeString(this.employerName);
    }

    protected VacancyVO(Parcel in) {
        this.vacancyName = in.readString();
        this.employerName = in.readString();
    }

    public static final Creator<VacancyVO> CREATOR = new Creator<VacancyVO>() {
        @Override
        public VacancyVO createFromParcel(Parcel source) {
            return new VacancyVO(source);
        }

        @Override
        public VacancyVO[] newArray(int size) {
            return new VacancyVO[size];
        }
    };
}
