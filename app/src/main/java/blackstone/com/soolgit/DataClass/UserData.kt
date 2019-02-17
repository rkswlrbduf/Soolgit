package blackstone.com.soolgit.DataClass

import java.io.Serializable

data class UserData(val id: String?, val name: String?, val profileUrl: String?, val type: String?, val email: String? = null, val birthday: String? = null, val idToken: String? = null) : Serializable {

    //constructor(id: String?, name: String?, email: String?, profileUrl: String?) : this(id, name, email, profileUrl, null, null)
//    constructor(id: String?, name: String?, email: String?, profileUrl: String?, birthday: String?) : this(id, name, email, profileUrl, birthday, null)
//    constructor(id: String?, name: String?, email: String?, profileUrl: String?, idToken: String?) : this(id, name, email, profileUrl, null, idToken)

}