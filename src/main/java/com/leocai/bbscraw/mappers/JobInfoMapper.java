package com.leocai.bbscraw.mappers;

import com.leocai.bbscraw.beans.JobInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by caiqingliang on 2016/7/29.
 */
public interface JobInfoMapper {

    @Insert("insert into jobinfo(title, hot, jobdate) values (#{title}, #{hot}, #{jobDate})")
    public int insertJobInfo(JobInfo jobInfo);

    @Select("select * from jobinfo")
    public List<JobInfo> getJobInfos();

    @Select("select * from jobinfo where jobdate >#{start} and jobdate<#{end}")
    public List<JobInfo> getJobInfosByDateRange(@Param("start")Date start, @Param("end") Date end);

}
