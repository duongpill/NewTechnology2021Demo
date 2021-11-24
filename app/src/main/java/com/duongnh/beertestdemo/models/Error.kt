package com.duongnh.beertestdemo.models

import androidx.annotation.StringRes

/**
 * Represents error states in a given view state
 */
data class Error(@StringRes val message: Int)