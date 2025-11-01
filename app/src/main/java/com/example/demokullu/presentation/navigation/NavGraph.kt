package com.example.demokullu.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.example.demokullu.domain.model.Article
import com.example.demokullu.presentation.bookmarks.BookmarksScreen
import com.example.demokullu.presentation.detail.DetailScreen
import com.example.demokullu.presentation.news.NewsScreen
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val selectedArticle = remember { mutableStateOf<Article?>(null) }

    NavHost(navController = navController, startDestination = "news") {
        composable("news") {
            NewsScreen(onArticleClick = { article ->
                selectedArticle.value = article
                navController.navigate("detail")
            })
        }
        composable("bookmarks") {
            BookmarksScreen(onArticleClick = { article ->
                selectedArticle.value = article
                navController.navigate("detail")
            })
        }
        composable("detail") {
            selectedArticle.value?.let { DetailScreen(article = it) }
        }
    }
}
