Feature: Sonar Rules

Scenario:
Given a metric with the key critical_violations
Then the value should be equal to 0

Scenario:
Given a metric with the key blocker_violations
Then the value should be equal to 0

Scenario:
Given a metric with the key major_violations
Then the value should be strictly less than 10

Scenario:
Given a metric with the key minor_violations
Then the value should be strictly less than 20