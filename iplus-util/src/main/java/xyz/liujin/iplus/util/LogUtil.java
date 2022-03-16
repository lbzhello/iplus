package xyz.liujin.iplus.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public static void debug(Object msg, Object... nameAndValues) {
        StringBuilder sb = new StringBuilder();
        // 获取方法调用栈
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[2];
        if (StringUtils.isNotBlank(stackTraceElement.getModuleName())) {
            sb.append(stackTraceElement.getModuleName()).append("/");
        }

        sb.append(stackTraceElement.getClassName())
                .append(".")
                .append(stackTraceElement.getMethodName())
                .append(":")
                .append(stackTraceElement.getLineNumber());
        // 调用此方法的位置
        String position = sb.toString();
        Logger logger = LoggerFactory.getLogger(position);
        format(String.valueOf(msg), nameAndValues).subscribe(it -> logger.debug(it.getT1(), it.getT2()));
    }

    /**
     * params 长度必须为偶数
     * 会被格式化为 [name=value] 形式
     * e.g.
     * ("n1", "lili", "n2", "tom") => [n1=lili] [n2=tom]
     */
    private static final Object[] EMPTY = new Object[0];
    private static Mono<Tuple2<String, Object[]>> format(String msg, Object... params) {
        if (ArrayUtils.isEmpty(params)) {
            return Mono.just(Tuples.of(msg, params));
        }

        // 只有一个参数形式： [param0] msg
        if (params.length == 1) {
            return Mono.just(Tuples.of("[" + params[0] + "]", EMPTY));
        }

        // param 长度必须是偶数
        if (params.length % 2 != 0) {
            throw new IllegalArgumentException("params length mast be even number");
        }

        List<Object> paramNames = new ArrayList<>();
        for (int i = 0; i < params.length; i += 2) {
            paramNames.add(params[i]);
        }

        List<Object> paramValues = new ArrayList<>();
        for (int i = 1; i < params.length; i += 2) {
            paramValues.add(params[i]);
        }

        return Mono.just(Tuples.of(fmtParamNames(msg, paramNames), paramValues.toArray()));
    }

    // 格式化参数名 p1, p2, p3 -> [p1={}] [p2={}] [p3={}]
    private static String fmtParamNames(String msg, List<Object> paramNames) {
        return paramNames.stream()
                .map(name -> "[" + name + "={}" + "]")
                .reduce((a, b) -> a + " " + b)
                .map(it -> it + " " + msg)
                .orElse("");
    }
}
