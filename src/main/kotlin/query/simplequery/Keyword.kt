package query.simplequery

import Post

class Keyword(text: String = "") {

    var text: String = ""
        set(value) {
            field = value
            textNoSpacesLowerCase = value.replace("\\s+".toRegex(), "")
                .toLowerCase()
        }

    private var textNoSpacesLowerCase: String = ""

    init {
        this.text = text
    }

    fun matches(post: Post): Boolean {
        return post.searchNoSpacesLowerCase(textNoSpacesLowerCase)
    }

}