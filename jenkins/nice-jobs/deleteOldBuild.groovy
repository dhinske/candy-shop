/**
Deletes all builds for all jobs except the last {keepAmount}
**/

node ('master') {
    def keepAmount = 10	// number of builds to keep for each job
    def removedBuilds = 0

    Jenkins.instance.getAllItems().findAll { item ->
      if (!(item instanceof com.cloudbees.hudson.plugins.folder.Folder) && !(item instanceof org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject)) {
        removedBuilds += deleteOldBuilds(item, keepAmount)
      }
    }
    println "--------------------------------------------"
    println removedBuilds + " builds were totally removed"

}

def deleteOldBuilds(job, keepAmount) {
  def latest = 0
  if (job.builds.getLastBuild() != null) {
    latest = job.builds.getLastBuild().number
  }

  int count = 0
  job.builds.findAll { build ->
    build.number <= (latest - keepAmount)
  }.each {
    it.delete()
    count++
  }
  println "   removed " + count + " builds from " + job.name
  return count
}
