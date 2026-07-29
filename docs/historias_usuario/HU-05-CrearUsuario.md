# HU-05 - CREAR USUARIO INTERNO

| Campo | Valor |
|---|---|
| **Código** | HU-05 |
| **Nombre** | Crear Usuario Interno |
| **Complejidad** | Alta |
| **HU Relacionada** | HU-03, HU-06, HU-07, HU-08 |
| **Módulo** | Módulo de Administración |

## Descripción

**Yo como** administrador del sistema
**Requiero** crear nuevos usuarios internos con roles específicos, datos completos y una contraseña que cumpla con los requisitos de seguridad establecidos
**Para** que otros usuarios puedan acceder al sistema según sus funciones asignadas con credenciales robustas

## Requerimiento

El sistema debe permitir al administrador crear usuarios internos del sistema, asignándoles un rol (Administrador, Gestor de Personal o Supervisor/Auditor) y un estado inicial (Activo/Inactivo). Cada usuario debe estar vinculado a un empleado existente (relación @OneToOne obligatoria), garantizando que todo usuario del sistema es también personal de la empresa. El email debe ser único en el sistema. La contraseña se genera automáticamente por el sistema utilizando PasswordGenerator, cumple con todos los requisitos de seguridad (mínimo 8 caracteres, mayúscula, minúscula, dígito, especial) y se muestra al administrador una sola vez en la respuesta. El usuario queda marcado con requirePasswordChange = true.

## Criterios de Aceptación

Condición 01

Dado: que el administrador está autenticado y completa el formulario con todos los datos válidos y un email único

Cuando: envía el formulario de creación

Entonces: el sistema genera una contraseña temporal automática que cumple los requisitos de seguridad, la encripta con BCrypt, guarda el usuario en PostgreSQL con requirePasswordChange = true, retorna HTTP 201 con los datos del usuario y la contraseña temporal (visible una sola vez)

Condición 02

Dado: que el administrador ingresa un email

Cuando: el email ya está registrado en el sistema

Entonces: el sistema retorna HTTP 409 y muestra el mensaje "El email ya está registrado"

Condición 03

Dado: que el administrador selecciona un empleado

Cuando: el empleado ya tiene un usuario de sistema asociado

Entonces: el sistema retorna HTTP 409 con el mensaje "El empleado ya tiene un usuario de sistema asociado"

## Tareas

| No | Descripción |
|---|---|
| 1 | Diseñar formulario de creación de usuario con campos: nombre, apellido, email, rol (select), empleado (selector que busca empleados existentes) |
| 2 | Implementar endpoint POST /admin/users en Spring Boot que genera contraseña temporal automáticamente |
| 3 | Generar contraseña temporal con PasswordGenerator (8+ chars, mayúscula, minúscula, dígito, especial) |
| 4 | Validar unicidad del email consultando PostgreSQL antes de la inserción |
| 5 | Validar que el employeeId enviado exista y no esté vinculado a otro usuario |
| 6 | Encriptar la contraseña temporal con BCrypt y marcar requirePasswordChange = true |
| 7 | Retornar HTTP 201 con datos del usuario + contraseña temporal (visible una sola vez) |
| 8 | Manejar respuestas de error: 409 para email duplicado o empleado ya vinculado, 400 para empleado inexistente |

## Control de Versiones

| Versión | Fecha | Autor | Revisión | Descripción | Aprobador |
|---|---|---|---|---|---|
| 1.0 | 2026-07-26 | | | Versión inicial | |
