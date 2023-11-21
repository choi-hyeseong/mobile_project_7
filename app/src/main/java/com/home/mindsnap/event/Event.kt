package com.home.mindsnap.event

open class Event<T>(private val content : T) {
    //toast와 같은 1회성 이벤트를 livedata에 넣을경우 화면 회전등과 같이 갱신시 지속적으로 호출되는 문제 해결하는 클래스
    var isHandled : Boolean = false
        private set

    fun getContent() : T? {
        if (isHandled)
            return null
        else {
            isHandled = true
            return content
        }
    }

    fun getContentForce() : T = content
}