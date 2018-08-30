# InsightDataChallenge
The program is written in Scala and does not depend on anything outside of its standard library. It reads and processes data in batches (controlled by the batchSizeInHours variable in main) and hence is able to scale to large datasets.

Discrepancies with the official output due to rounding are observed, but they are no larger than +-0.01.
A personal test is included to verify that if there are no predictions within a window period, NA is output.

To compile the jar go to src/insight_challenge and run mvn clean package.
