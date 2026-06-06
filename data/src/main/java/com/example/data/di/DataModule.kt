package com.example.data.di

import com.example.data.CompanionObject
import com.example.data.local.datastore.TokenManager
import com.example.data.network.api.AccountApi
import com.example.data.network.api.AuthApi
import com.example.data.network.api.DepositApi
import com.example.data.network.api.LoanApi
import com.example.data.network.api.TransactionApi
import com.example.data.network.interceptor.AuthInterceptor
import com.example.data.repository.AccountRepositoryImpl
import com.example.data.repository.AuthRepositoryImpl
import com.example.data.repository.DepositRepositoryImpl
import com.example.data.repository.LoanRepositoryImpl
import com.example.data.repository.TransactionRepositoryImpl
import com.example.domain.repository.AccountRepository
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.DepositRepository
import com.example.domain.repository.LoanRepository
import com.example.domain.repository.TransactionRepository
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

val dataModule = module {
    single { TokenManager(androidContext()) }

    single {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(get()))
            .addInterceptor(loggingInterceptor)
            .build()
    }

    single {
        val json = Json { ignoreUnknownKeys = true }
        val contentType = "application/json".toMediaType()

        Retrofit.Builder()
            .baseUrl(CompanionObject.BASE_URL)
            .client(get())
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    single<AuthApi> { get<Retrofit>().create(AuthApi::class.java) }
    single<AccountApi> { get<Retrofit>().create(AccountApi::class.java) }
    single<DepositApi> { get<Retrofit>().create(DepositApi::class.java) }
    single<LoanApi> { get<Retrofit>().create(LoanApi::class.java) }
    single<TransactionApi> { get<Retrofit>().create(TransactionApi::class.java) }

    single<AuthRepository> { AuthRepositoryImpl(authApi = get(), tokenManager = get()) }
    single<DepositRepository> { DepositRepositoryImpl(depositApi = get()) }
    single<LoanRepository> { LoanRepositoryImpl(loanApi = get()) }
    single<TransactionRepository> { TransactionRepositoryImpl(transactionApi = get()) }
    single<AccountRepository> { AccountRepositoryImpl(accountApi = get()) }
    

}