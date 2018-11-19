/*
    Set url, groupId and artifactId
*/

def url = ""
def groupId = ""
def artifactId = ""

/* -------------------------------------------------- */

if (!url.startsWith("http")) {
    url = "http://" + url
}
if (!url.endsWith("/")) {
    url = url + "/"
}
groupId = groupId.replace(".", "/") + "/"
artifactId = artifactId.replace(".", "/") + "/"

def releaseVersions = []
def snapshotVersions = []
def project
try {
    project = new XmlSlurper().parseText(new URL (url + 'releases/' + groupId + artifactId + 'maven-metadata.xml').getText())

    releaseVersions = project.versioning.versions.version.list()
} catch (Exception e) {
    releaseVersions = ["No Release-Version found"]
}

try {
    project = new XmlSlurper().parseText(new URL (url + 'snapshots/' + groupId + artifactId + 'maven-metadata.xml').getText())

    snapshotVersions = project.versioning.versions.version.list()
} catch (Exception e) {
    snapshotVersions = ["No SNAPSHOT-Version found"]
}

String date = project.versioning.lastUpdated.text()
String lastUpdated = [date.substring(6,8),date.substring(4,6),date.substring(0, 4)].join('.') + ' ' + [date.substring(8,10),date.substring(10,12),date.substring(12, 14)].join(':')

return releaseVersions.reverse().take(5) + ['# last snapshot: ' + lastUpdated + ' #'] + snapshotVersions.reverse().take(5)
