package com.example.storybaru

import com.example.storybaru.responses.ListStoryItem

object DataDummy {
    fun generateDummyStoryResponse():List<ListStoryItem>{
        val item : MutableList<ListStoryItem> = arrayListOf()
        for(i in 0..5){
            val story = ListStoryItem(
                "https://raw.githubusercontent.com/C23-PR539-NutriPlan/.github/main/profile/Logo1.png",
                "2023-06-04",
                "Elroi Yos",
                "Hi",
                "107.5671272",
                "story-IDslcnxA4R3N6e5p",
                "-6.9286992"
            )
            item.add(story)
        }
        return item
    }
}