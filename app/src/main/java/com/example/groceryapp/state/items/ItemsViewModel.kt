package com.example.groceryapp.state.items

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.String
import kotlin.collections.map

class ItemsViewModel : ViewModel() {
    private val firestore = Firebase.firestore

    private val _itemList: MutableStateFlow<List<ItemWithIdModel>> = MutableStateFlow(emptyList())
    val itemList: StateFlow<List<ItemWithIdModel>> = _itemList

    init {
        startSnapshotListener()
    }

    private fun transformDocumentSnapshot(document: DocumentSnapshot): ItemWithIdModel {
        val data = document.data!!

        return ItemWithIdModel(
            id = document.id,
            itemModel = ItemModel(
                category = data["category"] as String,
                imageURLs = data["imageURLs"] as List<String>,
                name = data["name"] as String,
                nameLowercase = data["nameLowercase"] as String,
                price = data["price"] as Double,
                quantity = data["quantity"] as String,
            )
        )
    }

    private fun transformQuerySnapshot(querySnapshot: QuerySnapshot): List<ItemWithIdModel> {
        return querySnapshot.map { transformDocumentSnapshot(it) }
    }

    private fun startSnapshotListener() {
        viewModelScope.launch {
            firestore
                .collection("items")
                .snapshots()
                .catch { err ->
                    Log.e("ERROR", err.message.toString())
                }
                .map { transformQuerySnapshot(it) }
                .collect { _itemList.value = it }
        }
    }

    fun getItemWithIdFlow(id: String): Flow<ItemWithIdModel> {
        return firestore
            .collection("items")
            .document(id)
            .snapshots()
            .map { transformDocumentSnapshot(it) }
    }

    suspend fun search(query: String): List<ItemWithIdModel> {
        return firestore
            .collection("items")
            .where(
                Filter.and(
                    Filter.greaterThanOrEqualTo("nameLowercase", query),
                    Filter.lessThan("nameLowercase",
                        query.substring(0,query.length - 1) +
                        Character.toString(
                            query.codePointAt(query.length - 1) + 1
                        ),
                    )
                )
            )
            .get()
            .await()
            .map { transformDocumentSnapshot(it) }
    }

    suspend fun addItem(item: ItemModel): DocumentReference? {
        return firestore
            .collection("items")
            .add(item)
            .await()
    }

    suspend fun setItem(item: ItemWithIdModel) {
         firestore
            .collection("items")
            .document(item.id)
            .set(item.itemModel)
            .await()
    }

    suspend fun deleteItem(itemId: String) {
        firestore
            .collection("items")
            .document(itemId)
            .delete()
            .await()
    }
}