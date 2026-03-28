# 🧮 Calculadora tipo iPhone

Aplicación Android de calculadora con diseño inspirado en la calculadora nativa de iOS, construida con **Jetpack Compose** y arquitectura **MVVM**.

---

## 📸 Vista previa

> Diseño oscuro con botones circulares, colores naranja para operadores y gris para funciones.

---

## 🏗️ Arquitectura — MVVM

El proyecto sigue el patrón **Model – View – ViewModel** con separación estricta de responsabilidades:

```
com.example.calculadoratipoiphone/
├── model/
│   └── CalculadoraModel.kt          # Lógica pura: evaluar() y formatear()
├── viewmodel/
│   └── CalculadoraViewModel.kt      # Estado (StateFlow) + acciones onKey()
├── view/
│   ├── CalculadoraScreen.kt         # Pantalla principal (display + teclado)
│   └── components/
│       └── CalcComponents.kt        # CalcRow y CalcCell reutilizables
├── ui/theme/                        # Colores, tipografía y tema Material3
└── MainActivity.kt                  # Punto de entrada Android (solo lanza la UI)
```

### Capas
* Model
  - Archivo: CalculadoraModel.kt
  - Responsabilidad: Evalúa expresiones matemáticas respetando jerarquía de operaciones (×÷ antes que +-).
* ViewModel
  - Archivo: CalculadoraViewModel.kt
  - Responsabilidad: Mantiene CalculadoraUiState como StateFlow inmutable; expone onKey(label).
* View
  ◦
  - Archivo: CalculadoraScreen.kt
  - Responsabilidad: Observa el estado con collectAsStateWithLifecycle(), no contiene lógica.
  
  •
 * Componentes
  - Archivo: CalcComponents.kt

  - Responsabilidad: CalcRow y CalcCell reutilizables, sin dependencia del ViewModel.

 * Entry point

 - Archivo: MainActivity.kt
 - Responsabilidad: Configura tema y Scaffold; crea el ViewModel con viewModel().



## ✨ Funcionalidades

- ✅ Operaciones básicas: suma `+`, resta `-`, multiplicación `×`, división `÷`
- ✅ Jerarquía de operaciones (× ÷ antes que + -)
- ✅ Encadenamiento de operaciones (`2+6×4÷2-1`)
- ✅ Cambio de signo `+/-`
- ✅ Porcentaje `%`
- ✅ Decimales `.`
- ✅ Borrado carácter a carácter `⌫`
- ✅ Limpiar todo `AC`
- ✅ Estado persistente ante rotación de pantalla (ViewModel)
- ✅ Manejo de errores (división entre cero, expresión inválida)

---

## 🛠️ Stack tecnológico

| Tecnología | Versión |
|---|---|
| Kotlin | 2.2.10 |
| Android Gradle Plugin | 9.1.0 |
| Jetpack Compose BOM | 2024.09.00 |
| Material3 | via BOM |
| Lifecycle / ViewModel | 2.10.0 |
| Activity Compose | 1.12.4 |
| Min SDK | 26 (Android 8.0) |
| Target SDK | 36 |

---

## 🚀 Cómo ejecutar el proyecto

### Prerrequisitos
- Android Studio Hedgehog o superior
- JDK 11
- Dispositivo o emulador con Android 8.0+ (API 26+)

### Pasos

```bash
# 1. Clonar el repositorio
git clone https://github.com/johnmesa-cell/calculadora.git

# 2. Entrar a la carpeta
cd calculadora

# 3. Cambiar a la rama MVVM
git checkout feature/mvvm
```

4. Abrir el proyecto en **Android Studio**
5. Esperar que Gradle sincronice (**Sync Now**)
6. Ejecutar con ▶️ **Run 'app'**

---



## 📁 Rama principal

| Rama | Descripción |
|---|---|
| `main` | Versión original (todo en `MainActivity.kt`) |

| `feature/mvvm` | Versión refactorizada con arquitectura MVVM |

---

## 👤 Autor

**John Mesa**  
**Juan Jose Restrepo**  
**Andres Yarce**  
 Universidad Tecnológica de Pereira  
 [@johnmesa-cell](https://github.com/johnmesa-cell)
