import reactor.core.publisher.Flux

// windowUntil
def list = ['1', '2', '*', '@', '5', '6', '(', '(', '(', '7', ')', ')', '8']
Flux.fromIterable(list).filter {
    it != '@'
}.windowUntil {
    it == '*' || it == '@' || it == '(' || it == ')'
}.flatMap {
    it.collectList()
}.subscribe {
    println it
}

