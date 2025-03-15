package com.example.groceryapp.state.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.groceryapp.state.items.ItemModel
import com.example.groceryapp.state.items.ItemWithIdModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.String
import kotlin.collections.map

class CartItemsViewModel : ViewModel() {
    private val firestore = Firebase.firestore

    private var listenerAttached = false;

    private val _itemList: MutableStateFlow<List<CartItemWithIdModel>?> = MutableStateFlow(null)
    val itemList: StateFlow<List<CartItemWithIdModel>?> = _itemList

    private fun transformDocumentSnapshot(document: DocumentSnapshot): CartItemWithIdModel? {
        val data = document.data!!

        Log.i("CART ITEM DATA", document.id)
        Log.i("CART ITEM DATA", data.toString())

        try {
            return CartItemWithIdModel(
                quantitySelected = data["quantitySelected"] as Long,
                itemWithIdModel = ItemWithIdModel(
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
            )
        } catch (e: Exception) {
            return null
        }
    }

    private fun transformQuerySnapshot(querySnapshot: QuerySnapshot): List<CartItemWithIdModel> {
        return querySnapshot.mapNotNull { transformDocumentSnapshot(it) }
    }

    fun startSnapshotListener(user: FirebaseUser) {
        if (listenerAttached) {
            return
        }

        listenerAttached = true

        viewModelScope.launch {
            firestore
                .collection("carts")
                .document(user.uid)
                .collection("items")
                .snapshots()
                .onCompletion { listenerAttached = false }
                .catch { err ->
                    Log.e("ERROR", err.message.toString())
                }
                .map { transformQuerySnapshot(it) }
                .collect { _itemList.value = it }
        }
    }

    suspend fun modifyCartItemCount(userId: String, cartItemId: String, newCount: Long) {
        if (newCount <= 0) {
            return
        }

        firestore
            .collection("carts")
            .document(userId)
            .collection("items")
            .document(cartItemId)
            .update("quantitySelected", newCount)
            .await()
    }

    suspend fun deleteCartItem(userId: String, cartItemId: String) {
        firestore
            .collection("carts")
            .document(userId)
            .collection("items")
            .document(cartItemId)
            .delete()
            .await()
    }

    suspend fun addCartItem(userId: String, itemWithIdModel: ItemWithIdModel) {
        firestore.runTransaction { transaction ->
            val cartItemReference = firestore
                .collection("carts")
                .document(userId)
                .collection("items")
                .document(itemWithIdModel.id)

            val cartItemDocument = transaction.get(cartItemReference)

            if (cartItemDocument.exists()) {
                Log.i("FLAT FB", "updating")
                transaction.update(cartItemReference, "quantitySelected", FieldValue.increment(1))
            } else {
                val obj = CartItemWithIdModel(
                    quantitySelected = 1,
                    itemWithIdModel = itemWithIdModel
                ).toFlatFirebaseObject()

                Log.i("FLAT FB", obj.toString())

                transaction.set(
                    cartItemReference,
                    obj
                )
            }
        }.await()
    }

    override fun onCleared() {
        super.onCleared()
    }
}

