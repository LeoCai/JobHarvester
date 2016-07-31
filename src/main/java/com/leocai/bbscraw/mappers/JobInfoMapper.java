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

    //CREATE DATABASE jobharvest  DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

    @Insert("CREATE TABLE IF NOT EXISTS jobinfo(id INT PRIMARY KEY AUTO_INCREMENT,\n" + " title TEXT, hot INT, \n"
            + " jobdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP, \n" + " company CHAR(20),\n" + " href VARCHAR(300),\n"
            + " isread BOOLEAN,\n" + " source CHAR(20) \n" + " )")
    public void createTableIfNotExits();

    @Insert("insert into jobinfo(title, hot, jobdate, company, href,source,isread) values (#{title}, #{hot}, #{jobDate}, #{company},#{href},#{source},#{isRead})")
    public int insertJobInfo(JobInfo jobInfo);

    @Select("select * from jobinfo order by jobdate desc")
    public List<JobInfo> getJobInfos();

    @Select("select * from jobinfo where jobdate >#{start} and jobdate<#{end}")
    public List<JobInfo> getJobInfosByDateRange(@Param("start")Date start, @Param("end") Date end);

    @Select("select distinct(company) from jobinfo")
    public List<String> getCompanys();

}
