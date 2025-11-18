🎨 Met ArtPiece Finder

A simple Android app that lets you search artworks from The Metropolitan Museum of Art Collection API
 by artist name.
Built with Kotlin, Coroutines, Retrofit, and Jetpack Compose.

- Features

 Search by artist name — e.g. Vincent van Gogh, Claude Monet

 Fetches real artwork data (title, medium, image, year, etc.)

 Coroutine-based async loading with proper error handling

 Clean UI architecture (MVVM) for maintainability

 Filters to public domain artworks only

 Error messages displayed gracefully in English

- Tech Stack
Layer	Libraries / Tools
Language	Kotlin
Async / Concurrency	Kotlin Coroutines, Flow
Networking	Retrofit, OkHttp
Serialization	kotlinx.serialization
Architecture	MVVM + Repository pattern
UI	Jetpack Compose
Logging	Android Logcat

- API Endpoints Used

Base URL:

https://collectionapi.metmuseum.org/public/collection/v1/

- Endpoints:

GET /search?q={artistName}&artistOrCulture=true — search for artwork IDs by artist

GET /objects/{objectID} — get full artwork details

- License

This project uses The Met Museum Open Access API
.
All artwork data and images are © The Metropolitan Museum of Art and fall under the Public Domain unless otherwise stated.

- Screen shots

<img width="353" height="769" alt="Screenshot 2025-11-17 at 5 33 24 PM" src="https://github.com/user-attachments/assets/44a30cf1-55de-4c12-834a-e54c30c75a71" />
<img width="349" height="771" alt="Screenshot 2025-11-17 at 5 33 59 PM" src="https://github.com/user-attachments/assets/4c37c80a-b887-4a60-8db5-8be4dc8686be" />

