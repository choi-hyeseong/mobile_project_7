package com.home.mindsnap.usecase

import com.home.mindsnap.repository.user.UserRepository

class SaveUserVisited(private val userRepository: UserRepository) {

    fun saveVisited() {
        userRepository.saveVisit()
    }
}