# Recursos de la Aplicación

## Archivos requeridos:

### Para el ejecutable (.exe):
- **icon.ico** - Icono del ejecutable (formato ICO, 32x32 o 48x48 píxeles)

### Para la aplicación:
- **logo.png** - Logo de la empresa (formato PNG, recomendado 200x200 píxeles)
- **splash.png** - Imagen del splash screen (formato PNG, 450x300 píxeles)

## Ubicación:
Coloca los archivos directamente en esta carpeta:
```
src/main/resources/
├── icon.ico
├── logo.png
└── splash.png
```

## Uso en código:
```java
// Cargar logo
Image logo = new Image(getClass().getResourceAsStream("/logo.png"));

// Cargar splash
Image splash = new Image(getClass().getResourceAsStream("/splash.png"));
```