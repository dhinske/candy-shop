# Jenkins Automated Build Trigger On Github Pull Request

source: https://devopscube.com/jenkins-build-trigger-github-pull-request/

tags: jenkins, github

It is better have an automated build process that kicks of the CI/CD pipeline on a pull request rather than manually triggering the jobs.

1. On Jenkins, install 'Github Pull Request Builder Plugin'
2. Configure Plugin
  1. Manange Jenkins --> Configure System
  2. Find “GitHub Pull Request Builder” section and click add credentials.
  3. Enter your Github username and password and add it.
  4. Test connection
3. Configure GitHub
  1. Go to Github repository settings, and under webhooks, add the Jenkins pull request builder payload URL. It has the following format: http://<Jenkins-IP>:<port>/ghprbhook/
  2. “Let me select individual events” option and select events of your choice
  3. Once saved, go back to the webhook option and see if there is a green tick. It means, Github is able to successfully deliver the events to Jenkins webhook URL
4. Configure Jenkins-job
  1. In your Job, under general tab, select Github project option and enter the Github repo URL for which you want the PR builds without .git extension
  2. Click advanced option and enable automatic PR build trigger and add the target branches you would raise the PR for
  3. Save
