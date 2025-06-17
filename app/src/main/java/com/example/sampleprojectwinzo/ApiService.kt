package com.example.sampleprojectwinzo

import retrofit2.http.GET

interface ApiService {
    @GET("v3/13d0c3f3-b1bb-4cc2-af83-321226b178cd")
    suspend fun getData(): LanguageData

}