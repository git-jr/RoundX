package com.kmp.hango.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kmp.hango.model.Question
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {
    @Query("SELECT * FROM question")
    fun getAll(): Flow<List<Question>>

    @Query("SELECT * FROM question WHERE id = :id")
    fun getById(id: Int): Flow<Question>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(question: Question)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(questions: List<Question>)

    @Update
    suspend fun update(question: Question)

    @Delete
    suspend fun delete(question: Question)

    @Query(" SELECT * FROM question  WHERE categoryId = :categoryId GROUP BY content ORDER BY RANDOM() LIMIT 10")
    fun getRandomQuestions(categoryId: String): Flow<List<Question>>

}