# Calculator — Android (Kotlin)

A native Android Calculator built with Kotlin and Jetpack Compose, matching the
web Calculator: large live-expression display, standard keypad, chained
operations, repeat-equals, percent, sign toggle, divide-by-zero handling,
and an in-session history panel — tap any history result to reuse it.

## Features

- `+ − × ÷`, `%`, `+/−`, decimal point, backspace, `AC`, `=`
- Chained operations and repeat-equals (press `=` again to repeat the last operation)
- Divide-by-zero shows an error instead of crashing
- History list, tap a result to reuse it
- Light and dark theme (follows the system)

## Clone via SSH

```bash
git clone ssh://git@ssh.github.com:443/paominthng/Calculator.git
```

## Run it

1. Open this folder in Android Studio (Hedgehog or newer).
2. Let Gradle sync.
3. Run on an emulator or a physical device (minSdk 24).

## Structure

- `app/src/main/java/com/paominthng/calculator/MainActivity.kt` — entry point, Compose UI
- `app/src/main/java/com/paominthng/calculator/CalculatorViewModel.kt` — state, input handling, history
- `app/src/main/java/com/paominthng/calculator/CalculatorEngine.kt` — pure calculation logic
- `app/src/main/java/com/paominthng/calculator/ui/theme/` — Material 3 theme
