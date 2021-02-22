@weather
@functional @regression
Feature: Know the weather status of the location before sending the drone with the programmed route
  As An Area-Checker
  Want to know area weather condition to fly a drone
  In order to send a drone to inspect area before cleaning it

  Background:
    Given following "known areas":
      | areaName  | lat   | lon   | countryCode |
      | Ipiñaburu | 43.07 | -2.75 | ES          |
      | Ibarra    | 43.05 | -2.57 | ES          |
      | Zegama    | 42.97 | -2.29 | ES          |
    And following "weather conditions":
      | areaName  | weatherCondition |
      | Ipiñaburu | Clouds           |
      | Ibarra    | Clear            |
      | Zegama    | Drizzle          |

  Scenario: report weather of all known areas
    When request report for all known areas
    Then report should contain "known areas"
    And report should contain "weather conditions"

  Scenario Outline: report if particular area "<areaName>" is currently suitable to fly the drone
    When request report for area "<areaName>"
    And reported weather should be "<weatherCondition>"
    And reported checkable should be "<checkable>"

    Examples:
      | areaName  | weatherCondition | checkable |
      | Ipiñaburu | Clouds           | false     |
      | Ibarra    | Clear            | true      |
      | Zegama    | Drizzle          | false     |
