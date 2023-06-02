package com.example.storybaru.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storybaru.api.ApiService
import com.example.storybaru.responses.ListStoryItem

class StoryPagingSource(private val apiService: ApiService) :PagingSource<Int,ListStoryItem>() {


    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        TODO("Not yet implemented")
    }
}