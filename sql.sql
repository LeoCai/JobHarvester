DROP DATABASE jobharvest
CREATE DATABASE jobharvest  DEFAULT CHARSET utf8 COLLATE utf8_general_ci; 
USE jobharvest;
DROP TABLE jobinfo;
CREATE TABLE IF NOT EXISTS jobinfo(id INT PRIMARY KEY AUTO_INCREMENT,
 title TEXT NOT NULL, hot INT NOT NULL DEFAULT 0, 
 jobdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
 company CHAR(15) NOT NULL DEFAULT '未识别',
 href VARCHAR(300) NOT NULL DEFAULT '',
 contentmd5 CHAR(32) UNIQUE NOT NULL DEFAULT '',
 isread BOOLEAN NOT NULL DEFAULT FALSE,
 source CHAR(20) NOT NULL DEFAULT '无',
 INDEX index_md5(contentmd5),
 INDEX index_jobdate(jobdate)

 );
 
SELECT * FROM jobinfo JOIN (SELECT jobdate FROM jobinfo ORDER BY jobdate) b ON jobinfo.jobdate = b.jobdate;

DROP TABLE  IF EXISTS jobinfo

SHOW VARIABLES LIKE 'char%'
 
INSERT INTO jobinfo(title,jobdate) VALUES('水电费','2016-07-01');

EXPLAIN SELECT COUNT(*) FROM jobinfo

SELECT MD5('asdas');

EXPLAIN SELECT contentmd5 FROM jobinfo WHERE source = '南大bbs' ORDER BY jobdate DESC LIMIT 1

SELECT * FROM jobinfo ORDER BY jobdate DESC

SET profiling = 1

SHOW PROFILES

SELECT COUNT(*) FROM jobinfo
 
