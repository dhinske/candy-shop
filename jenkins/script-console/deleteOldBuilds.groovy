/**
Deletes all builds for all jobs except the last {keepAmount}
**/

def keepAmount = 10	// number of builds to keep for each job

Jenkins.instance.getAllItems().findAll { item ->
  if (!(item instanceof com.cloudbees.hudson.plugins.folder.Folder) && !(item instanceof org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject)) {
    deleteOldBuilds(item, keepAmount)   
  }
}

def deleteOldBuilds(job, keepAmount) {
  def latest = 0
  if (job.builds.getLastBuild() != null) {
    latest = job.builds.getLastBuild().number
  }
  println job.name + " " + latest
  job.builds.findAll { build ->
    build.number <= (latest - keepAmount)
  }.each {
    it.delete()
    print "|"
  } 
}
