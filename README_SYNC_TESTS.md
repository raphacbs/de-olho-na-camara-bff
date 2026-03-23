Proposition Sync Job - Test & Run Guide

This file explains how to run the unit tests added for the Proposition sync changes.

Prerequisites
- Java and Maven installed and available in PATH.
- Run from repository root.

Run all tests

    mvn test

Run only the proposition sync tests

    mvn "-Dtest=br.com.deolhonacamara.scheduler.PropositionSyncJobTest" test

Run the QueryStringBuilder test

    mvn "-Dtest=br.com.deolhonacamara.util.QueryStringBuilderTest" test

Notes
- The job now builds a parameter map and delegates to `CamaraDeputadosService.getPropositions(Map<String,Object>)`.
- The property `sync.propositions.period.months` (default 1) is available in `src/main/resources/application.properties`.
- If Maven resource filtering fails due to encoding, ensure your environment handles UTF-8. Comments in `application.properties` were intentionally ASCII-only.

If you want, I can add these test commands to a small script or GitHub Actions workflow.
