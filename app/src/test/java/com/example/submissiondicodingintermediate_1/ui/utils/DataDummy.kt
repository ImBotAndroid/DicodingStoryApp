package com.example.submissiondicodingintermediate_1.ui.utils

import com.example.submissiondicodingintermediate_1.data.response.*

object DataDummy {

    fun dummyLoginSuccess(): LoginResponse {
        return LoginResponse(
            LoginResult("id", "name", "token"),
            false,
            "success"
        )
    }

    fun dummyLoginFailed(): LoginResponse {
        return LoginResponse(
            LoginResult(null, null, null),
            true,
            "error"
        )
    }

    fun dummyLoading(): Boolean {
        return true
    }

    fun dummyErrorMessage(): String {
        return "invalid"
    }

    fun dummyUserData(): LoginResult{
        return LoginResult(
            "a",
            "s",
            "d"
        )
    }

    fun dummyUserRegisterSuccess(): SignUpResponse {
        return SignUpResponse(
            false,
            "success"
        )
    }

    fun dummyUserRegisterFailed(): SignUpResponse {
        return SignUpResponse(
            true,
            "error"
        )
    }

    fun dummyListStory(): List<ListStoryItem> {
        val storyList: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                "Photo + $i",
                "Date + $i",
                "name + $i",
                "Desription $i",
                i.toDouble(),
                i.toDouble(),
                i.toString()
            )
            storyList.add(story)
        }
        return storyList
    }

    fun dummyUploadStory(): UploadStoriesResponse {
        return UploadStoriesResponse(
            false,
            "success"
        )
    }
}