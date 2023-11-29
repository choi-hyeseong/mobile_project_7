package com.home.mindsnap.component

import android.content.Context
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class StringDataTest {


    @Test
    fun CHECK_IS_RESOURCE() {
        val data = StringData(15, null)
        assertTrue(data.isResourceMessage())
    }

    @Test
    fun CHECK_IS_NOT_RESOURCE() {
        val data = StringData(message = "테스트")
        assertFalse(data.isResourceMessage())
    }

    @Test
    fun TEST_GET_STRING_NO_PARAM() {
        val data = StringData(15, null)
        val context = mockk<Context>()

        every { context.getString(any()) } returns ""
        data.getResourceMessage(context)
        verify(exactly = 1) { context.getString(any()) } //verify는 다 수행하고 맨 마지막에..
    }

    @Test
    fun TEST_GET_STRING_PARAM() {
        val data = StringData(15, null, "배고파")
        val context = mockk<Context>()

        every { context.getString(any(), any()) } returns ""
        data.getResourceMessage(context)
        verify(exactly = 1) { context.getString(any(), any()) } //verify는 다 수행하고 맨 마지막에..
    }
}