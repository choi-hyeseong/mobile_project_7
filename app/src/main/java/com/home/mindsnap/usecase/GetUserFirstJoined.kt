package com.home.mindsnap.usecase

import com.home.mindsnap.repository.user.UserRepository

class GetUserFirstJoined(private val userRepository: UserRepository) {

    fun isFirstJoined() : Boolean {
        return userRepository.isFirstJoined()
    }
}