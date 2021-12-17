# JIRA to AsciiDoc Exporter

Create AsciiDoc tables from a JIRA query

## Example Output
The script produces AsciiDoc like the following:

----
* link:https://issues.redhat.com/browse/QUARKUS-1553[QUARKUS-1553] Update SmallRye Config to 2.5.0
* link:https://issues.redhat.com/browse/QUARKUS-1564[QUARKUS-1564] Use proper wait-time for jar and docker launch modes
----

Which renders like this:

* link:https://issues.redhat.com/browse/QUARKUS-1553[QUARKUS-1553] Update SmallRye Config to 2.5.0
* link:https://issues.redhat.com/browse/QUARKUS-1564[QUARKUS-1564] Use proper wait-time for jar and docker launch modes


## Prerequisite

You must install JBang which can be (easily) obtained by following the instructions on  https://www.jbang.dev/.


## Usage
Run the script as follows:

 jbang ./run.java -t <JIRA Personal Access Token> -s <JIRA Server Base URL> -q <JIRA JQL Query>

For example:

 jbang ./run.java -t 'S3cR3tT0k4nFr0mJIr4' -s https://issues.redhat.com -q "project = QUARKUS AND issuetype = Bug AND fixVersion = 2.2.4.GA AND component = 'team/eng' ORDER BY priority DESC"
