#!/usr/bin/groovy
/*
Create/Update a CHANGELOG.md file depending on current git history.
	1) Install needed plugins: Git Changelog, SSH agent
	2) Add credentials in Jenkins which allows you to commit to your repository
	3) Replace all {TODO: ...} with your settings

Read https://github.com/tomasbjerre/git-changelog-lib for more templates/documentation
*/
def call() {

    def changelogString = gitChangelog returnType: 'STRING', template: '''
# Changelog

Changelog for {{ownerName}} {{repoName}}.

{{#tags}}
## {{name}}

  {{#commits}}
**{{{messageTitle}}}**

{{#messageBodyItems}}
 * {{.}} 
{{/messageBodyItems}}

[{{hash}}](http://{TODO: your git url}/{{ownerName}}/{{repoName}}/commit/{{hash}}) {{authorName}} *{{commitTime}}*

  {{/commits}}

{{/tags}}'''

    sshagent(['{TODO: your jenkins-credentials to commit}']) {
        writeFile file: 'CHANGELOG.md', text: changelogString

        sh "git config --global user.email '{TODO: your jenkins-commit-adress}'"
        sh "git config --global user.name 'Jenkins'"
        sh "git add CHANGELOG.md"
        sh "git commit -m 'updated CHANGELOG.md'"
        sh "git status"
        sh "git push origin master"
    }
}

