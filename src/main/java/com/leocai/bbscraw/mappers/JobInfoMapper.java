package com.leocai.bbscraw.mappers;

import com.leocai.bbscraw.beans.JobInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * Created by caiqingliang on 2016/7/29.
 */
public interface JobInfoMapper {

    //CREATE DATABASE jobharvest  DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

    @Insert("CREATE TABLE IF NOT EXISTS jobinfo(id INT PRIMARY KEY AUTO_INCREMENT,\n" + " title TEXT, hot INT, \n"
            + " jobdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP, \n" + " company CHAR(20),\n" + " href VARCHAR(300),\n"
            + " isread BOOLEAN,\n" + " source CHAR(20), contentmd5 char(32) unique \n"
            + " )") void createTableIfNotExits();

    @Insert(" DROP TABLE IF EXISTS jobinfo\n") void dropTableIfExists();

    @Insert("insert into jobinfo(title, hot, jobdate, company, href,source,isread,contentmd5) values (#{title}, #{hot}, #{jobDate}, #{company},#{href},#{source},#{isRead},#{contentMD5})") int insertJobInfo(
            JobInfo jobInfo);

    @Select("select * from jobinfo order by jobdate desc") List<JobInfo> getJobInfos();

    @Select("select * from jobinfo where jobdate >#{start} and jobdate<#{end}") List<JobInfo> getJobInfosByDateRange(
            @Param("start") Date start, @Param("end") Date end);

    @Select("select distinct(company) from jobinfo") List<String> getCompanys();


}
