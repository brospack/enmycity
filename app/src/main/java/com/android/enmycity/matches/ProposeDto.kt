package com.android.enmycity.matches

import com.android.enmycity.common.StatusId
import java.util.Date

data class ProposeDto(
    val proponentUid: String = "",
    val proponentPhoto: String = "",
    val proponentName: String = "",
    val proposerUid: String = "",
    val proposerPhoto: String = "",
    val proposerName: String = "",
    val status: Int = StatusId.DISSABLED,
    val message: String = "",
    val creationDate: Date = Date(),
    val alterDate: Date = Date()
)
