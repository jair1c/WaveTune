# 🎵 WaveTune

Reproductor de música local para Android — moderno, minimalista y completamente offline.

---

## ✨ Características

| Categoría | Detalle |
|---|---|
| **Reproducción** | MP3, FLAC, WAV, AAC vía ExoPlayer/Media3 |
| **Offline** | Sin login, sin internet, sin anuncios |
| **Escaneo** | Lectura automática de MediaStore del dispositivo |
| **Metadata** | Título, artista, álbum, carátula (albumart) |
| **Playlists** | Persistencia local con Room + SQLite |
| **Modo oscuro** | Automático (sigue al sistema) |
| **Notificación** | Controles en la barra de notificaciones (Media3 Session) |

---

## 🏗️ Arquitectura

```
WaveTune/
├── data/
│   ├── model/          Song, Playlist, PlaylistSongCrossRef
│   ├── db/             WaveTuneDatabase, SongDao, PlaylistDao
│   └── repository/     MusicRepository  ← fuente única de verdad
├── di/                 AppModule (Hilt)
├── player/
│   ├── WaveTunePlaybackService   (MediaSessionService + ExoPlayer)
│   └── PlayerController          (StateFlow, controles globales)
└── ui/
    ├── theme/          WaveTuneTheme, colores, tipografía
    ├── components/     AlbumArtImage, SongRow, MiniPlayerBar
    ├── screens/
    │   ├── welcome/    WelcomeScreen
    │   ├── library/    LibraryScreen + LibraryViewModel
    │   └── player/     PlayerScreen + PlayerViewModel
    └── NavGraph.kt     Navegación Compose
```

**Stack:** Kotlin · Jetpack Compose · MVVM · Hilt · Room · Media3/ExoPlayer · Coil

---

## 🚀 Instrucciones de compilación

### Requisitos
- Android Studio Hedgehog (2023.1.1) o superior
- JDK 17
- Android SDK 35 (compileSdk), minSdk 26

### Pasos

```bash
# 1. Clonar / abrir en Android Studio
git clone <repo> && cd WaveTune

# 2. Sincronizar Gradle
./gradlew build

# 3. Instalar en dispositivo / emulador
./gradlew installDebug
```

> **Nota:** En Android 13+ el permiso solicitado es `READ_MEDIA_AUDIO`.  
> En versiones anteriores se usa `READ_EXTERNAL_STORAGE`.

---

## 📱 Pantallas

### 1. Welcome Screen
- Fondo claro con blobs animados (InfiniteTransition + scale)
- Headline mixto bold/light: *"Elevate Every Moment With **Music**"*
- Botón **Start Listening** → navega a Library

### 2. Library Screen
- Tabs: **Songs** · **Artists** · **Playlists**
- Barra de búsqueda reactiva (StateFlow + combine)
- Grid de artistas (3 columnas, iniciales de artista)
- Escaneo bajo demanda con botón **Scan**

### 3. Player Screen
- Album art con bordes redondeados + sombra dinámica
- Scale animado: grande al reproducir, pequeño al pausar
- Slider de progreso actualizado cada 500 ms
- Controles: Shuffle · ← · ▶/⏸ · → · Repeat

### Mini Player Bar
- Visible en Library siempre que haya canción activa
- Toque → navega al player
- Botón play/pause integrado

---

## 🔧 Extensiones sugeridas

- [ ] Ecualizador (`AudioEffect` / `Equalizer`)
- [ ] Widget de reproducción (`AppWidgetProvider`)
- [ ] Letras embebidas (leer tag `USLT` con JAudioTagger)
- [ ] Gestos de swipe en el player
- [ ] Ordenamiento y filtros avanzados en Library

---

## 📄 Licencia

MIT — úsalo como base para tus proyectos.
