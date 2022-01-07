# execdoc-core

Remark: *this is work in progress*.

This library provides core functionalities for developing various automation utilities 
that can mine and generate both executable code and office documents.

The idea came from the painful realization that managing all the pieces of information 
pertaining for a software development project is a laborious, time-consuming task and that
little details can have a tremendous impact on the project cost and schedule.

The objective here is to build and maintain a "single source of truth" knowledge base that 
can be intuitively or even transparently used both by humans and computers in collaboration.
Such a knowledge base can be used for querying and reasoning about the project data and can
be updated automatically.

<img src="https://raw.githubusercontent.com/codingspeedup/execdoc-core/main/src/test/resources/readme/prolog-kdm.png" width="300" height="100">

At some point the realization occurred that a Prolog program can serve as a solution for 
storing and processing project related information.
The amount of data can be large, but we are not talking about big data.
On the other hand dealing directly with Prolog on a daily basis is not something that a 
business analyst or a regular developer is willing to do.

<br/>

<img src="https://upload.wikimedia.org/wikipedia/commons/b/b8/ADM_KDM.png" width="480" height="280">

<br/>

The main functionalities implemented here are concerned with mining various types of
input sources and mapping the gathered result to Prolog facts and rules.
At the core, a taxonomy based on the
OMG [Knowledge Discovery Model](https://en.wikipedia.org/wiki/Knowledge_Discovery_Metamodel)
is used.
The conceptual hierarchy can be easily extended according to the necessities of the project.

Here is an overview picture on the supported workflows:

<img src="https://raw.githubusercontent.com/codingspeedup/execdoc-core/main/src/test/resources/readme/workflows.png" width="450" height="200">

For now minimal mining functionalities are implemented for:
- Filesystem
- Textual log files
- Java files (via [JavaParser](https://javaparser.org/))
- Excel files (via [Apache POI](https://poi.apache.org/))
- SQL schemas (via JDBC)

For generation are provided:
- Diff reports for files & folders
- A basic code mining desktop application [execdoc-apps](https://github.com/codingspeedup/execdoc-apps)
- A PoC for generating end-to-end enterprise applications via JDL [jhipster-execdoc-poc](https://github.com/codingspeedup/jhipster-execdoc-poc)




