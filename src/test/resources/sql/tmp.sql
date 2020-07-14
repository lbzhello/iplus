select totalPerson, totalMetalDangerous, totalFaceTemp, indexCode, start_time, end_time
from (select id, index_code as indexCode from t_sd_entrance group by id, index_code) st1
         right join (SELECT totalPerson, totalMetalDangerous, totalFaceTemp, t1.channel_id, t1.start_time, t1.end_time
                     from (select sum(total) as totalPerson, channel_id, start_time, end_time
                           from t_sd_day_statistic_131616
                           where start_time >= ?
                             and ? >= end_time
                             and channel_id = any (values (?))
                           group by channel_id, start_time, end_time) t1
                              FULL JOIN (select sum(total) as totalMetalDangerous, channel_id, start_time, end_time
                                         from t_sd_day_statistic_266242
                                         where start_time >= ?
                                           and ? >= end_time
                                           and type = 'dangerous'
                                           and channel_id = any (values (?))
                                         group by channel_id, start_time, end_time) t2
                                        on t1.channel_id = t2.channel_id and t1.start_time = t2.start_time and
                                           t1.end_time = t2.end_time
                              FUll JOIN (select sum(total) as totalFaceTemp, channel_id, start_time, end_time
                                         from t_sd_day_statistic_131614
                                         where start_time >= ?
                                           and ? >= end_time
                                           and type = 'dangerous'
                                           and channel_id = any (values (?))
                                         group by channel_id, start_time, end_time) t3
                                        on t1.channel_id = t3.channel_id and t1.start_time = t3.start_time and
                                           t1.end_time = t3.end_time) st2 on st1.id = st2.channel_id
--     ==> Parameters: 2020-07-06 00:00:00.0(Timestamp), 2020-07-10 15:12:38.0(Timestamp), 3(Integer), 2020-07-06 00:00:00.0(Timestamp), 2020-07-10 15:12:38.0(Timestamp), 3(Integer), 2020-07-06 00:00:00.0(Timestamp), 2020-07-10 15:12:38.0(Timestamp), 3(Integer)
--     <==    Columns: totalperson, totalmetaldangerous, totalfacetemp, indexcode, start_time, end_time
--     <==        Row: null, null, 17460, null, null, null
--     <==        Row: null, null, 17460, null, null, null
--     <==        Row: null, null, 15790, null, null, null
--     <==        Row: null, null, 17460, null, null, null
--     <==        Row: null, 7805, null, null, null, null
--     <==      Total: 5