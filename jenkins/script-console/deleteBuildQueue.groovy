import hudson.model.*

def queue = Jenkins.instance.queue

queue.items.each {
    queue.cancel(it.task)
}
