// 读取文件
def file = new File("C:/tmp.txt")
file.eachLine {
    println it
}

// 转字节数组
println file.getBytes()

file.withReader {
    while (it.readLine()) {
        // do something...
    }
}

file = new File("G:/tmp.txt")

file.bytes = [66,22,11]

file << '''
hello world
'''

// 遍历目录
def dir = new File("G:/liubaozhu")
dir.eachFile {
    println it.name
}
dir.eachFileMatch(~/.*\.txt/) {
    println it.name
}

// 递归遍历，包括子目录
dir.eachFileRecurse {
    println it.name
}

println '-------------'

// 字符串列表
dir.list().each {
    println it
}

println '-------------'
// 命令行
def process = "cmd /c ls".execute()
process.in.getText("gbk").eachLine { line ->
    println line
}

