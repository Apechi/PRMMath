package com.theprime.primemath.data.dao

import androidx.room.*
import com.theprime.primemath.data.model.Player
import com.theprime.primemath.data.model.PlayerWithExercises
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface UserDao {
    @Query("SELECT * FROM Player")
    fun getAll(): Single<List<Player>>

    @Query("SELECT * FROM Player WHERE nick LIKE :nick")
    fun findByNick(nick: String): Single<Player>

    @Insert
    fun insert(player: Player): Completable

    @Transaction
    @Query("SELECT * FROM Player WHERE nick LIKE :nick")
    fun getPlayerWithScores(nick: String): Single<List<PlayerWithExercises>>

    @Delete
    fun delete(player: Player): Completable

    @Update
    fun update(player: Player): Completable
}