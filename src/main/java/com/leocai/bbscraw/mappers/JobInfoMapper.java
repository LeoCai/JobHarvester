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

    //TODO company 单独建表
    //TODO source 单独建表
    //TODO sql 放到XML文件中


    //CREATE DATABASE jobharvest  DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

    @Insert("CREATE TABLE IF NOT EXISTS jobinfo(id INT PRIMARY KEY AUTO_INCREMENT,\n"
            + " title TEXT NOT NULL, hot INT NOT NULL DEFAULT 0, \n"
            + " jobdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP, \n" + " company CHAR(15) NOT NULL DEFAULT '未识别',\n"
            + " href VARCHAR(300) NOT NULL DEFAULT '',\n" + " contentmd5 CHAR(32) UNIQUE NOT NULL DEFAULT '',\n"
            + " isread BOOLEAN NOT NULL DEFAULT FALSE,\n" + " source CHAR(20) NOT NULL DEFAULT '无',\n"
            + " INDEX index_md5(contentmd5),\n" + " INDEX index_jobdate(jobdate)\n"
            + " )") void createTableIfNotExits();

    @Insert(" DROP TABLE IF EXISTS jobinfo\n") void dropTableIfExists();

    @Insert("insert into jobinfo(title, hot, jobdate, company, href,source,isread,contentmd5) values (#{title}, #{hot}, #{jobDate}, #{company},#{href},#{source},#{isRead},#{contentMD5})")
    void insertJobInfo(JobInfo jobInfo);

    @Select("select * from jobinfo order by jobdate desc") List<JobInfo> getJobInfos();

    @Select("select * from jobinfo where jobdate >#{start} and jobdate<#{end}") List<JobInfo> getJobInfosByDateRange(
            @Param("start") Date start, @Param("end") Date end);

    @Select("select distinct(company) from jobinfo") List<String> getCompanys();

    @Select("select contentmd5 from jobinfo where source = #{source} order by jobdate desc limit 1") String getLatestMd5(
            @Param("source") String source);

    @Select("select * from jobinfo where jobdate >#{date} order by jobdate desc")
    List<JobInfo> getJobInfosSince(@Param("date") Date date);

    @Select("select jobdate from jobinfo where source=#{source} order by jobdate desc limit 1")
    Date getLatestDateBySource(@Param("source") String source);
}
