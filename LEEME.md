# Gear HUD Bridge

Addon que muestra la marcha de Compact Gearshift (2.1.5) en el HUD de BetterSpeedHUD (1.2.1),
en el lugar de la "D". 0 = N, 1..10 = marcha, -1..-10 = R1..R10.
No modifica ni redistribuye ninguno de los dos mods: usa mixins sobre sus clases por nombre.

## Compilar
1. Descargá el MDK oficial de NeoForge para 1.21.1 (Java 21) y descomprimilo.
2. Borrá su carpeta `src/` y copiá la `src/` de este proyecto.
3. En `gradle.properties` del MDK poné `mod_id=gearhud_bridge` (el resto da igual, mods.toml ya está en resources;
   si tu MDK genera el toml desde plantilla, borrá esa tarea o la plantilla).
4. `./gradlew build` -> el jar queda en `build/libs/`.

## Usar
Instalá el jar junto a Create, Compact Gearshift y BetterSpeedHUD (cliente y servidor).
Abrí una vez la GUI del Gearshift: desde ese momento ese bloque queda vinculado a vos
y el HUD muestra su marcha mientras vas montado.

## Limitaciones
- El vínculo jugador<->Gearshift no se guarda: tras reiniciar el servidor hay que abrir la GUI de nuevo.
- Si no abriste la GUI, el HUD sigue mostrando "D".
- Hecho para Compact Gearshift 2.1.5 y BetterSpeedHUD 1.2.1; otras versiones pueden cambiar los nombres que parcheo.
- Sin probar: no pude compilarlo en este entorno.
