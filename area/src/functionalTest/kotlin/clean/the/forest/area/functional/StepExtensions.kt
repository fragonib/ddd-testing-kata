package clean.the.forest.area.functional

import io.cucumber.datatable.DataTable

fun <T> DataTable.extract(extractor: (List<String>) -> T) =
    this.cells().drop(/* header size */ 1).map(extractor)
