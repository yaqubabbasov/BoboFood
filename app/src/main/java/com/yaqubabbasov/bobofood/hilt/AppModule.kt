package com.yaqubabbasov.bobofood.hilt

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yaqubabbasov.bobofood.data.datasource.LocalDataSource
import com.yaqubabbasov.bobofood.data.datasource.PrefsManager
import com.yaqubabbasov.bobofood.data.datasource.ProductDataSource
import com.yaqubabbasov.bobofood.data.repository.ProductRepository
import com.yaqubabbasov.bobofood.retrofit.ApiUtil
import com.yaqubabbasov.bobofood.retrofit.ProductDao
import com.yaqubabbasov.bobofood.room.DataBase
import com.yaqubabbasov.bobofood.room.FeatureDao
import com.yaqubabbasov.bobofood.room.ProductRoomDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideproductdao(): ProductDao{
        return ApiUtil.getProductDao()

    }
    @Provides
    @Singleton
    fun provideproductdatasource( pdao: ProductDao): ProductDataSource{
        return ProductDataSource(pdao)
    }
    @Provides
    @Singleton
    fun  provideproductrepo(pdata: ProductDataSource,proom: LocalDataSource): ProductRepository {
        return ProductRepository(pdata,proom)
    }
    @Provides
    @Singleton
    fun provideproductroomdao(@ApplicationContext context: Context): ProductRoomDao{
        val db= Room.databaseBuilder(context, DataBase::class.java, "bobofavourite.sqlite")
            .createFromAsset("bobofavourite.sqlite")
            .fallbackToDestructiveMigration(false)
            .build()

        return db.getdao()
    }
    @Provides
    @Singleton
    fun providelocadatasource(dao: ProductRoomDao, fdao: FeatureDao): LocalDataSource{
        return LocalDataSource(dao,fdao)
    }
    @Provides
    @Singleton
    fun providefeaturesdao(@ApplicationContext context: Context): FeatureDao{
        val db= Room.databaseBuilder(context, DataBase::class.java,"featurespr.sqlite.db")
            .createFromAsset("featurespr.sqlite.db")
            .fallbackToDestructiveMigration()
            .build()
        return  db.featureDao()
    }
    @Provides
    @Singleton
    fun provideDataStoreManager(@ApplicationContext context: Context): PrefsManager {
        return PrefsManager(context)
    }
}