package com.example.demokullu.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.demokullu.data.local.AppDatabase
import com.example.demokullu.data.local.ArticleDao
import com.example.demokullu.data.remote.NewsApi
import com.example.demokullu.data.repository.NewsRepositoryImpl
import com.example.demokullu.domain.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "https://dummyjson.com/"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .build()

    @Provides
    @Singleton
    fun provideNewsApi(client: OkHttpClient): NewsApi =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApi::class.java)

    // Example migration: v1 -> v2 (no-op). Replace with your schema changes when needed.
    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // example: database.execSQL("ALTER TABLE bookmarked_articles ADD COLUMN foo TEXT");
            // currently no-op
        }
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "news_db")
            // if you bumped version, add migration(s) OR fallback to destructive migration:
            .addMigrations(MIGRATION_1_2) // include only if version >=2 and you actually have migration code
            .fallbackToDestructiveMigration() // keep or remove depending on your migration strategy
            .build()
    }

    @Provides
    fun provideArticleDao(db: AppDatabase): ArticleDao = db.articleDao()

    @Provides
    @Singleton
    fun provideNewsRepository(api: NewsApi, dao: ArticleDao): NewsRepository =
        NewsRepositoryImpl(api, dao)
}
