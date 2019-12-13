package xyz.liujin.iplus;

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
public class TestHelper {
    public static void printCurrentThread(String method) {
        System.out.println("【" + method + "】 method running in thread: <<" + Thread.currentThread().getName() + ">>");
    }
}
