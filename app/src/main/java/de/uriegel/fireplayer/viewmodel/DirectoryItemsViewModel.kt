package de.uriegel.fireplayer.viewmodel

import androidx.lifecycle.ViewModel
import de.uriegel.fireplayer.requests.DirectoryItem

class DirectoryItemsViewModel() : ViewModel() {
    var items: List<DirectoryItem> = listOf()
}