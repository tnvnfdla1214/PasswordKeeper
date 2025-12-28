package com.passwordkeeper.di

import android.content.Context
import androidx.room.Room
import com.passwordkeeper.data.local.AppDatabase
import com.passwordkeeper.data.local.dao.PasswordDao
import com.passwordkeeper.data.local.dao.MemoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration() // 개발 중에는 마이그레이션 대신 DB 재생성
            .build()
    }

    @Provides
    @Singleton
    fun providePasswordDao(database: AppDatabase): PasswordDao {
        return database.passwordDao()
    }

    @Provides
    @Singleton
    fun provideMemoDao(database: AppDatabase): MemoDao {
        return database.memoDao()
    }
}
