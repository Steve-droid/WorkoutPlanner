package com.example.auth.presentation.intro

sealed interface IntroAction {
   data object OnSignInClick : IntroAction

   data object OnSignUpClick : IntroAction
}