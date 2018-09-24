/**
Deletes all builds for all jobs except the last {keepAmount}
**/

def keepAmount = 8	// number of builds to keep for each job

Jenkins.instance.getAllItems().findAll { job ->
  def latest = job.builds.getLastBuild().number
  println latest
  job.builds.findAll { build ->
    build.number <= (latest - keepAmount)
  }.each {
    build.delete()
  } 
}
