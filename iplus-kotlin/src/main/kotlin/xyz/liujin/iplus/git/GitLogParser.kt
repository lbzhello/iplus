package xyz.liujin.iplus.git

import reactor.core.publisher.Flux
import xyz.liujin.iplus.util.FileUtils
import xyz.liujin.iplus.util.StringUtils
import java.io.File

object CommitFlag {
    val add: String = "A"
    val modify: String = "M"
    val delete: String = "D"

    val author: String = "Author:"
    val date: String = "Date:"

    fun isChangeLine(lines: List<String>): Boolean {
        return add == lines[0] || modify == lines[0]
    }
}

//
fun main(args: Array<String>) {
    parseLog("G:/test.txt", "https://sys-gitlab.hikvision.com.cn/ISD/USTA-scbc/blob/b_dev_scbc_v1.0")?.forEach {
        println(it)
    }

}

// git log --name-status --author=liubaozhu --after=2020-05-01 --before=2020-05-31
fun parseLog(logFile: String, basePath: String): List<List<Any>>? {
    return Flux.just(FileUtils.toString(File(logFile)))
            .flatMap { Flux.fromIterable(it.split("commit ")) }
            .filter { StringUtils.isNotBlank(it) }
            // 解析每条 commit log
            .flatMap {
                val commitLines = it.split("\n")
                val commitId = commitLines[0]
                return@flatMap Flux.fromIterable(commitLines)
                        .skip(1)
                        .filter { line -> StringUtils.isNotBlank(line) }
                        .map { line -> line.split("\\s+".toRegex(), 2) }
                        // 修改的文件
                        .filter { line -> CommitFlag.isChangeLine(line) }
                        .map { line -> listOf(line[1], commitId) }
            }
            // 以文件名分组，值为每次 commitId
            .groupBy({ it[0] }, 10000)
            .flatMap { it.collectList() }
            .map {
                val commids = it.map { files -> files[1] }.toList()
                val rightPath = it[0][0]

                val leftPath = if (basePath.endsWith("/")) {
                    basePath.substring(0, basePath.length - 1)
                } else {
                    basePath
                }
                val fullPath = if (rightPath.startsWith("/")) {
                    leftPath + rightPath
                } else {
                    "$leftPath/$rightPath"
                }
                listOf(fullPath, commids)
            }
            .collectList()
            .block()
}