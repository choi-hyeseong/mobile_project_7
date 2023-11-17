package com.home.mindsnap.repository.user.dao

interface UserDao {
    //실질적 데이터 접근 클래스

    fun isFirstJoined() : Boolean

    fun saveVisit()
}