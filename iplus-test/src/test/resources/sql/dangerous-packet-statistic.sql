SELECT dangerousType, totalDangerousPacket, totalPacket, totalDangerous, indexCode, start_time, end_time
from (select id, index_code as indexCode from t_sd_entrance group by id, index_code) st1
         right join
     (SELECT totalDangerousPacket,
             totalPacket,
             totalDangerous,
             t1.channel_id,
             t3.type as dangerousType,
             t1.start_time,
             t1.end_time
      from (select sum(total) as totalPacket, channel_id, start_time, end_time
            from t_sd_day_statistic_266241
            where '2020-07-02T00:00:00.000Z' <= start_time
              and end_time <= '2020-09-02T00:00:00.000Z'
              and (type = 'dangerous' or type = 'normal')
              and channel_id = any (values (3)
            )
            group by channel_id, start_time, end_time
           ) t1
               LEFT JOIN
           (select sum(total) as totalDangerousPacket, channel_id, start_time, end_time
            from t_sd_day_statistic_266241
            where '2020-07-02T00:00:00.000Z' <= start_time
              and end_time <= '2020-09-02T00:00:00.000Z'
              and type = 'dangerous'
              and channel_id = any (values (3)
            )
            group by channel_id, start_time, end_time
           ) t2 ON t1.channel_id = t2.channel_id and t1.start_time = t2.start_time and t1.end_time = t2.end_time
               FULL JOIN
           (select type, sum(total) as totalDangerous, channel_id, start_time, end_time
            from t_sd_day_statistic_266241
            where '2020-07-02T00:00:00.000Z' <= start_time
              and end_time <= '2020-09-02T00:00:00.000Z'
              and type != 'dangerous'
              and type != 'normal'
              and channel_id = any (values (3)
            )
            group by type, channel_id, start_time, end_time
           ) t3 on t1.channel_id = t3.channel_id and t1.start_time = t3.start_time and t1.end_time = t3.end_time) st2
     on st1.id = st2.channel_id