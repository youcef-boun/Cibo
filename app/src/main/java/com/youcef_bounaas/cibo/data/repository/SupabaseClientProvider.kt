package com.youcef_bounaas.cibo.data.repository



import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage




object SupabaseClientProvider {

    val supabase = createSupabaseClient(
        supabaseUrl = "https://wdahqgotjpfbhkagfher.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6IndkYWhxZ290anBmYmhrYWdmaGVyIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzQ3MzcxODgsImV4cCI6MjA1MDMxMzE4OH0.suLXxhKrHnTcFhd7W_QJo4tl5NkGD5XWIGOGislwybk"
    ) {
        install(Postgrest)
        install(Auth){
            alwaysAutoRefresh = true
            autoLoadFromStorage = true
        }

        install(Storage) { // Removed the type parameter for Storage
            // settings
        }




    }
}
