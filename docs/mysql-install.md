# 📦  `Instalación de MySQL`
Este documento describe cómo instalar y verificar MySQL Server para ejecutar el proyecto FlightOnTime en entorno local.

## Requisitos
- Sistema operativo Windows / macOS / Linux
- Permisos de administrador
- Acceso a terminal (CMD, PowerShell, Terminal o Git Bash)

## Pasos generales
## 1. Descargar MySQL desde el sitio oficial:
```
https://dev.mysql.com/downloads/
```
Seleccione su sistema operativo y continúe con el instalador recomendado.

## 2. Instalar MySQL Server

- Durante la instalación:
- Seleccione MySQL Server 
- Mantenga la configuración por defecto (recomendado)
- Defina una contraseña para el usuario root 
  - ⚠️ Guárdela, será necesaria más adelante

En Windows:
- Asegúrese de que el servicio MySQL Server quede configurado como Running
En macOS / Linux:
- El servicio suele iniciar automáticamente tras la instalación
---

## 3. Verificar que MySQL esté activo
Abra una terminal y ejecute:
```
mysql -u root -p
```
Ingrese la contraseña definida durante la instalación.


Si la instalación fue correcta, verá un prompt similar a:

```
mysql>
```
Esto confirma que MySQL está funcionando correctamente.

---

## ❌ Posibles errores comunes

- mysql: command not found
  - MySQL no está en el PATH
  - Reinicie la terminal o revise la instalación
- Access denied for user 'root'
  - Verifique la contraseña
  - Confirme que el usuario sea root

---

## ✅ Siguiente paso

Una vez instalado MySQL, continúe con:

👉 [Creación de la base de datos](docs/mysql-database-setup.md)
