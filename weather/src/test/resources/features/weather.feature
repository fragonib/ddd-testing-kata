@functional
@weather
Feature: Know the weather status of the location before sending the drone with the programmed route
  As An Area-Checker
  Want to know area weather condition to fly a drone
  In order to send a drone to inspect area before cleaning it

  Background:
    Given following "known areas":
      | areaName  | lat   | lon   | country |
      | Ipiñaburu | 43.07 | -2.75 | ES      |
      | Ibarra    | 43.05 | -2.57 | ES      |
      | Zegama    | 42.97 | -2.29 | ES      |
    And following "weather condition":
      | areaName  | weatherCondition |
      | Ipiñaburu | Clouds           |
      | Ibarra    | Clear            |
      | Zegama    | Drizzle          |

  @regression
  Scenario: report weather of all known areas
    When request report for all known areas
    Then report should contain "known areas"
    And report should contain "weather condition"

  @regression
  Scenario Outline: check if area "<areaName>" is currently suitable to fly the drone
    When request area "<areaName>" report
    And report weather is "<weatherCondition>"
    And report checkable is "<checkable>"

    Examples:
      | areaName  | weatherCondition | checkable |
      | Ipiñaburu | Clouds           | false     |
      | Ibarra    | Clear            | true      |
      | Zegama    | Drizzle          | false     |
