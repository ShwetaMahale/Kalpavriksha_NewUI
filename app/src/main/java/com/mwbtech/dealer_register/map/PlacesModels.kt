package com.mwbtech.dealer_register.map

class PlacePredictions {
    var predictions: ArrayList<PlaceAutoComplete>? = null
}

class PlaceAutoComplete {
    var description: String? = ""
    var place_id: String? = ""
    var structured_formatting: StructuredData = StructuredData()
}

data class StructuredData(
        var main_text: String? = ""
)

  /*val map = HashMap<String, String>()
        try {
            map["input"] = URLEncoder.encode("jaipur", "utf8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        //map["components"] = "country:ind"
        map["language"] = "en"
        map["radius"] = "500"
        map["location"] = "0.0,0.0"
        map["key"] = "AIzaSyDMpxEF3MzJp7JTzeBPWnYB4jjMifJKn9w"*/