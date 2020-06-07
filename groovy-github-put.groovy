@Grab(group='org.apache.httpcomponents', module='httpclient', version='4.5.2')

import groovy.json.*

import org.apache.http.client.methods.*
import org.apache.http.entity.*
import org.apache.http.impl.client.*

class GroovyGithubApi {
    static final String token = "YOUR_TOKEN"

    def put(path, body) {
        def jsonBody = new JsonBuilder(body).toString()

        def url = "https://api.github.com/$path"
        def post = new HttpPut(url)

        post.addHeader("Authorization", "token $token")
        post.addHeader("User-Agent", "Groovy")
        post.setEntity(new StringEntity(jsonBody))

        def client = HttpClientBuilder.create().build()
        def response = client.execute(post)

        def bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))
        def jsonResponse = bufferedReader.getText()
        println "response: \n" + jsonResponse

        def slurper = new JsonSlurper()
        def resultMap = slurper.parseText(jsonResponse)
    }
}

def github = new GroovyGithubApi()
def path = "repos/YOUR_GITHUB_USERNAME/YOUR_REPO/contents/FILE_NAME"
def values = ""
def variables = [
    variable1: "=line1\n", 
    variable2: "=line2\n", 
    variable3: "=line3\n", 
    variable4: "=line4\n", 
    variable5: "=line5\n", 
]
variables.findAll { key, value -> 
    values = values + key + value
}
String encoded = values.bytes.encodeBase64().toString()
def body = [:]
    body["committer"] = [:]
    body["message"] = "Sent by Groovy API"
    body["committer"]["name"] = "Groovy API"
    body["committer"]["email"] = "YOU_EMAIL"
    body["content"] = encoded

github.put(path, body)
