# YouTube Content Planner

App Android nativa (Kotlin + Jetpack Compose) para gestionar varios canales de YouTube, planificar la producción de vídeos —desde la idea hasta la publicación— y visualizar de un vistazo qué se ha subido y cuándo.

## Stack técnico

- **Lenguaje:** Kotlin
- **UI:** Jetpack Compose, 100% declarativa, Material Design 3
- **Arquitectura:** MVVM + Clean Architecture (capas `data` / `domain` / `ui`) con patrón Repository
- **Base de datos local:** Room
- **Inyección de dependencias:** Hilt
- **Navegación:** Navigation Compose
- **Asincronía:** Kotlin Coroutines + StateFlow

## Estructura del proyecto

```
app/src/main/java/com/deividus/ytplanner/
├── data/
│   ├── local/            # Entities, DAOs, Room database, TypeConverters
│   └── repository/       # Implementaciones de los repositorios + mappers
├── domain/
│   ├── model/             # Channel, Video, VideoStatus (modelos de negocio puros)
│   └── repository/        # Interfaces de repositorio (contratos del dominio)
├── di/                    # Módulos Hilt (Database, Repository)
├── ui/
│   ├── theme/              # Colores, tipografía y tema oscuro Material 3
│   ├── components/         # Componentes reutilizables (VideoCard, StatusBadge, ChannelDot…)
│   ├── navigation/         # Grafo de navegación y bottom bar
│   ├── board/               # Tablero Kanban de planificación + alta/edición de vídeo
│   ├── calendar/            # Calendario mensual de publicaciones
│   └── channels/            # Gestión de canales
├── util/                  # Utilidades de fecha y color
├── MainActivity.kt
└── YtPlannerApplication.kt
```

## Funcionalidades

- **Canales:** alta, edición y eliminación de canales de YouTube, cada uno con un color distintivo.
- **Planificación (tablero Kanban):** columnas por estado (`Idea → Guion → Edición → Programado → Publicado`), filtro por canal, avance rápido de estado y borrado.
- **Ficha de vídeo:** título, canal, estado, fecha programada (selector de fecha), URL de YouTube y notas/guion.
- **Calendario:** vista mensual con un punto de color por canal en cada día que tiene contenido programado o publicado; al tocar un día se listan sus vídeos.
- **Modo oscuro** por defecto con la paleta de marca (rojo YouTube sobre superficies oscuras) y colores semánticos por estado.

## Compilar y ejecutar

1. Abre la carpeta raíz del proyecto en Android Studio (Koala o superior).
2. Deja que Android Studio sincronice Gradle y descargue las dependencias (requiere acceso a `google()` y `mavenCentral()`).
3. Ejecuta la configuración `app` en un emulador o dispositivo con Android 8.0 (API 26) o superior.

> Este proyecto se generó en un entorno sin acceso a `dl.google.com`, por lo que no se pudo ejecutar `./gradlew assembleDebug` aquí. El código se revisó manualmente (imports, balance de llaves, firmas de API de Compose/Room/Hilt) pero conviene compilarlo en Android Studio antes del primer uso real.
