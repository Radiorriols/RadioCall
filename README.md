# RadioCall 0.1 — prototipo experimental

Aplicación Android libre para probar grabación de audio en HTC U11 con Android 9.

## Compilación
1. Instalar Android Studio en Ubuntu, con SDK Platform 35 y JDK 17.
2. Abrir la carpeta `RadioCall` como proyecto.
3. Esperar a que Gradle descargue las dependencias (requiere Internet).
4. Menú **Build > Build APK(s)**.
5. Copiar `app/build/outputs/apk/debug/app-debug.apk` al HTC e instalarlo.

## Uso
1. Conceder permiso de micrófono.
2. Iniciar prueba MIC, abrir el marcador y llamar con altavoz activado.
3. Regresar a RadioCall y detener. Copiar el archivo .m4a mediante USB.
4. Repetir con VOICE_COMMUNICATION si se desea comparar.

## Limitaciones importantes
- Android puede silenciar o bloquear el micrófono de otra aplicación durante una llamada.
- No graba directamente el flujo de voz telefónica. No se garantiza grabar ambos interlocutores.
- No hay servicio en primer plano: Android puede detener la grabación al cambiar de aplicación.
- No usar para entrevistas importantes sin verificar antes el resultado.
- La grabación no se inicia automáticamente ni se sube a Internet.
- Esta versión no está compilada ni probada en un HTC U11 real.

## Licencia
Código fuente publicado bajo MIT (ver LICENSE).

## Compilar desde el móvil con GitHub Actions

1. Crear un repositorio nuevo en GitHub (por ejemplo `radiocall`) desde el navegador.
2. Subir **el contenido de la carpeta RadioCall** a la raíz del repositorio, incluyendo `.github/workflows/build-apk.yml`. Atención: GitHub oculta archivos que comienzan por punto en algunos selectores.
3. En la pestaña **Actions**, seleccionar **Compilar APK RadioCall** y pulsar **Run workflow**. También se ejecuta al subir archivos a la rama principal.
4. Cuando termine con marca verde, abrir la ejecución y descargar el artefacto **RadioCall-debug-APK** (ZIP); extraer `app-debug.apk` e instalarlo en Android.

**Limitación importante:** esta versión es un prototipo de grabación del micrófono, NO una grabadora de llamadas fiable. Al abrir el marcador o cambiar de aplicación puede detenerse al destruirse la actividad; durante una llamada Android puede silenciar o impedir la captura. No se ha probado en HTC U11.
