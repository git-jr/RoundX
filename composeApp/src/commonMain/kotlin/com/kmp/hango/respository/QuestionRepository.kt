package com.kmp.hango.respository

import com.kmp.hango.data.QuestionService
import com.kmp.hango.data.database.QuestionDao
import com.kmp.hango.model.Question
import kotlinx.coroutines.flow.Flow


interface QuestionRepository {
    suspend fun getAll(): List<Question>
    suspend fun getAllLocal(): Flow<List<Question>>
    suspend fun saveLocal(questions: List<Question>)
    fun getRandomQuestions(categoryId: String): Flow<List<Question>>
}

class QuestionRepositoryImpl(
    private val service: QuestionService,
    private val dao: QuestionDao
) : QuestionRepository {

    override suspend fun getAll(): List<Question> {
        val questions = service.getAll().map { it.toQuestion() }
        return questions
    }

    override suspend fun getAllLocal(): Flow<List<Question>> {
        return dao.getAll()
    }

    override suspend fun saveLocal(questions: List<Question>) {
        dao.insert(questions)
    }

    override fun getRandomQuestions(categoryId: String): Flow<List<Question>> {
        return dao.getRandomQuestions(categoryId)
    }
}