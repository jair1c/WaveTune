package com.wavetune.di

import android.content.Context
import androidx.room.Room
import com.wavetune.data.db.PlaylistDao
import com.wavetune.data.db.SongDao
import com.wavetune.data.db.WaveTuneDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): WaveTuneDatabase =
        Room.databaseBuilder(
            context,
            WaveTuneDatabase::class.java,
            WaveTuneDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideSongDao(db: WaveTuneDatabase): SongDao = db.songDao()

    @Provides
    fun providePlaylistDao(db: WaveTuneDatabase): PlaylistDao = db.playlistDao()
}
