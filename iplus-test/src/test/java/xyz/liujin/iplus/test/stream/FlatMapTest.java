package xyz.liujin.iplus.test.stream;

import org.junit.jupiter.api.Test;
import xyz.liujin.iplus.util.LogUtil;

import java.util.stream.Stream;

/**
 * HIK所有，<br/>
 * 受到法律的保护，任何公司或个人，未经授权不得擅自拷贝。<br/>
 *
 * @describtion
 * @copyright Copyright: 2015-2020
 * @creator liubaozhu
 * @create-time 16:26 2022/3/15
 * @department 安检业务部
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user :{修改人} :{修改时间}
 * @modify by reason :{原因}
 **/
public class FlatMapTest {
    @Test
    public void asyncTest() {
        Stream.of(1, 2, 3)
                .flatMap(it -> {
                    LogUtil.debug("flatMap");
                    return asyncStream(it);
                })
                .forEach(it -> {
                    LogUtil.debug(it);
                });
    }

    private Stream<String> asyncStream(Integer i) {
        return Stream.of("a", "b").parallel();
    }
}
