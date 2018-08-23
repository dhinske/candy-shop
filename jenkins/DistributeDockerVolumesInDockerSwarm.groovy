/**
Find the Docker-volume of a running container and distribute it to all other nodes of the swarm. This way, a Docker-service with a single replicant can always fail and be respawned on any node with the latest backup, provided by this Jenkins-job.

Preconditions
Jenkins needs to have access to all nodes of the swarm. He will copy the volumes to his home-folder (/home/jenkins) and then copy it over (with sudo) to the docker volume storage. Depending on your authentication-setup you might want to change the pipeline script.

HOWTO
1) On your Jenkins, create a pipeline job
2) Use this script as pipeline code
3) Edit the pipeline script (replace all 'TODO' with the specific information of your environment)
**/

import groovy.json.JsonSlurper;
import groovy.xml.XmlUtil
import hudson.FilePath;

node (TODO){
    def swarm_manager = TODO            // swarm_manager will get asked about other nodes in the swarm
    def service_name = TODO             // name of the Docker-service helps to look for the current running container
    def user = "jenkins"                // user jenkins uses to connect to the Docker-hosts
    def user_home = "/home/jenkins"     // directory where jenkins copies the volume to
    def docker_volume_path = "/var/lib/docker/volumes"
    def jenkins_ssh = TODO              // jenkins credentials id to connect to all hosts

    def source_node         // this node runs the service-container
    def target_nodes = []   // these nodes will receive the docker-volume of the current running container
    def volume_name         // name of the docker-volume on the hosts

    stage('Collect Swarm Information') {
        println "Get all nodes of the swarm..."
        def response = jsonSlurperRequest("http://"+swarm_manager+":4243/nodes")
        if (response == null) {
            error "  Master node not reachable, abort!"
        }
        def nodes = []
        response.each { node ->
            nodes.add(node.Status.Addr)
        }
        println "Found " + nodes

        println "Looking for the current running container..."
        nodes.each { node ->
            println "on " + node
            response = jsonSlurperRequest("http://"+node+":4243/containers/json")
            if (response == null) {
                println "  Host " + node + " is currently not available, skipping..."
                return
            }
            target_nodes.add(node)
            response.each { container ->
                if (container.Labels."com.docker.swarm.service.name".equals(service_name)) {
                    source_node = node
                    volume_name = container.Mounts.Name[0]
                }
            }
        }
        println "Found service at node " + source_node + " with volume " + volume_name
        target_nodes = target_nodes.findAll { it != source_node }
        println "Distributing volumes to " + target_nodes

        if (target_nodes.size() == 0) {
            println "No nodes to distribute data to..."
            return
        }
    }

    sshagent([jenkins_ssh]) {
        stage('Copy volume to agent') {
            sh "ssh -o StrictHostKeyChecking=no -l "+user+" "+source_node+" 'sudo ls -la "+docker_volume_path+"/"+volume_name+"/_data'"
            sh "rsync -r --rsync-path='sudo rsync' "+user+"@"+source_node+":"+docker_volume_path+"/"+volume_name+"/_data ."
        }
        target_nodes.each { node ->
            stage('Distribute to ' + node) {
                sh "ssh -o StrictHostKeyChecking=no -l "+user+" "+node+" 'sudo rm -rf "+user_home+"/_data'"
                sh "ssh -o StrictHostKeyChecking=no -l "+user+" "+node+" 'sudo rm -rf "+docker_volume_path+"/"+volume_name+"/_data'"
                sh "scp -r -o StrictHostKeyChecking=no _data "+user+"@"+node+":"+user_home
                sh "ssh -o StrictHostKeyChecking=no -l "+user+" "+node+" 'sudo mkdir -p "+docker_volume_path+"/"+volume_name+"/_data'"
                sh "ssh -o StrictHostKeyChecking=no -l "+user+" "+node+" 'sudo cp -r "+user_home+"/_data/* "+docker_volume_path+"/"+volume_name+"/_data'"
                sh "ssh -o StrictHostKeyChecking=no -l "+user+" "+node+" 'sudo chown -R 1000 "+docker_volume_path+"/"+volume_name+"'"
            }
        }
    }
}

/**
    Performs a GET-Request at an url. Returns null if no valid response is available.
**/
def jsonSlurperRequest(urlString) {
    println "Calling " + urlString
    try {
        def url = new URL(urlString)
        def connection = (HttpURLConnection)url.openConnection()
        connection.setRequestMethod("GET")
        connection.setRequestProperty("Accept", "application/json")
        connection.setRequestProperty("User-Agent", "Mozilla/5.0")
        new JsonSlurper().parse(connection.getInputStream())
    }
    catch (Exception e) {
        return null
    }
}
