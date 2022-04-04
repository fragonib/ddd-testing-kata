@weather
@functional @regression
Feature: Know the weather status of the location before sending the drone with the programmed route
  As An Area-Checker
  Want to know area weather condition to fly a drone
  In order to send a drone to inspect area before cleaning it

  Background:
    Given following "known areas":
      | areaName    | lat   | lon   | countryCode |
      | Calderona   | 39.67 | -0.43 | ES          |
      | Mariola     | 38.72 | -0.53 | ES          |
      | Penyagolosa | 40.23 | -0.29 | ES          |
    And following "weather conditions":
      | areaName    | weatherCondition |
      | Calderona   | Clouds           |
      | Mariola     | Clear            |
      | Penyagolosa | Drizzle          |

  Scenario: report weather of all known areas
    When request report for all known areas
    Then report should contain "known areas"
    And report should contain "weather conditions"

  Scenario Outline: report if particular area "<areaName>" is currently suitable to fly the drone
    When request report for area "<areaName>"
    And reported weather should be "<weatherCondition>"
    And reported checkable should be "<checkable>"

    Examples:
      | areaName    | weatherCondition | checkable |
      | Calderona   | Clouds           | false     |
      | Mariola     | Clear            | true      |
      | Penyagolosa | Drizzle          | false     |
