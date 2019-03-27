package blackstone.com.soolgit.DataClass

import java.io.Serializable

data class ServiceCompleteData(
        val storeid: String,
        val userid: String,
        val storenm: String,
        val storemlcn: String,
        val servicenm: String,
        val servicecost: String,
        val serviceimg: String,
        val storecode: String) : Serializable