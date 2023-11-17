package com.home.mindsnap.repository.user

import com.home.mindsnap.repository.user.dao.UserDao

class PreferenceUserRepository(private val userDao: UserDao) : UserRepository {
    override fun isFirstJoined(): Boolean {
        return userDao.isFirstJoined()
    }

    override fun saveVisit() {
        return userDao.saveVisit()
    }
}