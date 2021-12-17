//usr/bin/env jbang "$0" "$@" ; exit $?
//REPOS mavencentral,atlassian=https://packages.atlassian.com/maven/repository/public
//DEPS info.picocli:picocli:4.2.0, com.atlassian.jira:jira-rest-java-client-app:5.2.2, com.atlassian.jira:jira-rest-java-client-api:5.2.2, com.atlassian.jira:jira-rest-java-client-core:5.2.2, org.json:json:20200518, com.konghq:unirest-java:3.7.04

import com.atlassian.httpclient.api.Request;
import com.atlassian.jira.rest.client.api.AuthenticationHandler;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.net.URI;
import java.util.concurrent.Callable;

@Command(name = "run", mixinStandardHelpOptions = true, version = "run 0.1",
        description = "JIRA Query to AsciiDoc exporter")
class run implements Callable<Integer> {

    @CommandLine.Option(names = {"-t", "--jira-token"}, description = "The Personal Access Token for authenticating with the JIRA server", required = true)
    private String jiraToken;

    @CommandLine.Option(names = {"-s", "--jira-server"}, description = "The JIRA server to connect to", required = true)
    private String jiraServerURL;

    @CommandLine.Option(names = {"-q", "--query"}, description = "JIRA Query to export to AsciiDoc table", required = true)
    private String query;

    public static void main(String... args) {
        int exitCode = new CommandLine(new run()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {

        JiraRestClient restClient = new AsynchronousJiraRestClientFactory().create(new URI(jiraServerURL), new BearerHttpAuthenticationHandler(jiraToken));

        SearchResult searchResultsAll = restClient.getSearchClient().searchJql(query).claim();
        for (Issue issue :searchResultsAll.getIssues()) {
            System.out.println("* link:" + jiraServerURL + "/browse/" + issue.getKey() + "[" + issue.getKey() + "] " + issue.getSummary());
        }

        return 0;
    }

    public static class BearerHttpAuthenticationHandler implements AuthenticationHandler {

        private static final String AUTHORIZATION_HEADER = "Authorization";
        private final String token;

        public BearerHttpAuthenticationHandler(final String token) {
            this.token = token;
        }

        @Override
        public void configure(Request.Builder builder) {
            builder.setHeader(AUTHORIZATION_HEADER, "Bearer " + token);
        }
    }
}
