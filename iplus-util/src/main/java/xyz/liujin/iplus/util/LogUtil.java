package xyz.liujin.iplus.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HIK所有，<br/>
 * 受到法律的保护，任何公司或个人，未经授权不得擅自拷贝。<br/>
 *
 * @describtion
 * @copyright Copyright: 2015-2020
 * @creator 姓名
 * @create-time 17:08 2019/12/13
 * @department 安检业务部
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user :{修改人} :{修改时间}
 * @modify by reason :{原因}
 **/
public class LogUtil {
    public static void debug(String msg) {
        // 调用此方法的位置
        String position = Thread.currentThread().getStackTrace()[2].toString();
        Logger logger = LoggerFactory.getLogger(position);
        logger.debug("{}", msg);
    }
}
