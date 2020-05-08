package com.example.notekeeper.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities= arrayOf(CourseInfo::class, NoteInfo::class), version= 2, exportSchema = false)
abstract class NoteKeeperDatabase : RoomDatabase() {
    abstract fun courseInfoDao() : CourseInfoDao
    abstract fun noteInfoDao() : NoteInfoDao

    companion object {
        @Volatile
        private var INSTANCE: NoteKeeperDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): NoteKeeperDatabase {
            val tempInstance =
                INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteKeeperDatabase::class.java,
                    "NoteKeeper.db").addCallback(
                    NoteKeeperDatabaseCallback(
                        scope
                    )
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

    private class NoteKeeperDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            if(INSTANCE != null){
                scope.launch {
                    println("**************** ADDING TO DB")
                    populateCourses(INSTANCE?.courseInfoDao()!!)
                    populateNotes(INSTANCE?.noteInfoDao()!!)
                }
            }
        }

        suspend fun populateCourses(courseInfoDao: CourseInfoDao) {
            courseInfoDao.insert(
                CourseInfo(
                    "android_intents",
                    "Android Programming with Intents"
                )
            )
            courseInfoDao.insert(
                CourseInfo(
                    "android_async",
                    "Android Async Programming and Services"
                )
            )
            courseInfoDao.insert(
                CourseInfo(
                    "java_lang",
                    "Java Fundamentals: The Java Language"
                )
            )
            courseInfoDao.insert(
                CourseInfo(
                    "java_core",
                    "Java Fundamentals: The Core Platform"
                )
            )
        }

        suspend fun populateNotes(noteInfoDao: NoteInfoDao) {
            noteInfoDao.insert(
                NoteInfo(
                    "android_intents",
                    "Dynamic intent resolution",
                    "Wow, intents allow components to be resolved at runtime"
                )
            )
                    noteInfoDao.insert(
                        NoteInfo(
                            "android_intents",
                            "Delegating intents",
                            "PendingIntents are powerful; they delegate much more than just a component invocation"
                        )
                    )
                    noteInfoDao.insert(
                        NoteInfo(
                            "android_async",
                            "Service default threads",
                            "Did you know that by default an Android Service will tie up the UI thread?"
                        )
                    )
                    noteInfoDao.insert(
                        NoteInfo(
                            "android_async",
                            "Long running operations",
                            "Foreground Services can be tied to a notification icon"
                        )
                    )
            noteInfoDao.insert(
                NoteInfo(
                    "java_lang",
                    "Parameters",
                    "Leverage variable-length parameter lists?"
                )
            )
            noteInfoDao.insert(
                NoteInfo(
                    "java_lang",
                    "Anonymous classes",
                    "Anonymous classes simplify implementing one-use types"
                )
            )
                    noteInfoDao.insert(
                        NoteInfo(
                            "java_core",
                            "Compiler options",
                            "The -jar option isn't compatible with with the -cp option"
                        )
                    )
                    noteInfoDao.insert(
                        NoteInfo(
                            "java_core",
                            "Serialization",
                            "Remember to include SerialVersionUID to assure version compatibility"
                        )
                    )
        }
    }
}