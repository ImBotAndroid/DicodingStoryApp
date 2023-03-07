package com.example.submissiondicodingintermediate_1.data.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailStory(
    var name: String,
    var description: String,
    var photoUrl: String
): Parcelable
