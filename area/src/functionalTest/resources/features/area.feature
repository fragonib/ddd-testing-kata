@crud
@functional @regression
Feature: Manage the set of known areas
  As An Area-Maintainer
  Want to add areas to set og know areas
  In order to target new areas to clean

  Background:
    Given following "known areas":
      | areaName  | lat   | lon   | countryCode |
      | Ipiñaburu | 43.07 | -2.75 | ES          |
      | Ibarra    | 43.05 | -2.57 | ES          |
      | Zegama    | 42.97 | -2.29 | ES          |

  Scenario: add an area that is freshly new
    When add area "new area" with data:
      | areaName | lat  | lon  | countryCode |
      | new area | 40.0 | -2.0 | ES          |
    Then known areas should contain "new area" and "known areas"

  Scenario: add an area with same name than previous known one
    When add area "new area" with data:
      | areaName  | lat  | lon  | countryCode |
      | Ipiñaburu | 40.0 | -2.0 | ES          |
    Then complain about previously existing area
