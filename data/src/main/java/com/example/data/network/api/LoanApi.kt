package com.example.data.network.api

import com.example.data.network.dto.request.ApplyLoanRequestDto
import com.example.data.network.dto.request.RepayLoanRequestDto
import com.example.data.network.dto.response.LoanResponseDto
import com.example.data.network.dto.response.MessageResponseDto
import retrofit2.http.*

interface LoanApi {
    @POST("/api/v1/loans/apply")
    suspend fun applyForLoan(@Body request: ApplyLoanRequestDto): LoanResponseDto

    @POST("/api/v1/loans/repay")
    suspend fun repayLoan(@Body request: RepayLoanRequestDto): MessageResponseDto

    @GET("/api/v1/loans")
    suspend fun getMyLoans(): List<LoanResponseDto>
}