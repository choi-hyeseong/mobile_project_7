package com.home.mindsnap.repository.user

interface UserRepository {
    //유저가 튜토리얼을 완료했는지 확인하는 레포지토리

    fun isFirstJoined() : Boolean

    fun saveVisit() //튜토리얼 완료

}