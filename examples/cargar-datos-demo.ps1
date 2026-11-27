# Carga datos ficticios de demostracion en FestivalAPI.
# Ejecutar desde el proyecto: powershell -ExecutionPolicy Bypass -File .\examples\cargar-datos-demo.ps1
# La aplicacion debe estar iniciada antes de ejecutar este script.
# Es idempotente: si un registro de muestra ya existe, lo reutiliza.

param(
    [string]$BaseUrl = "http://localhost:8080"
)

$ErrorActionPreference = "Stop"
$BaseUrl = $BaseUrl.TrimEnd("/")
$archivoDatos = Join-Path $PSScriptRoot "datos-demo.json"

if (-not (Test-Path -LiteralPath $archivoDatos)) {
    throw "No se encontro $archivoDatos. Descarga ambos archivos de la carpeta examples."
}

$datos = Get-Content -LiteralPath $archivoDatos -Raw -Encoding UTF8 | ConvertFrom-Json

try {
    $null = Invoke-RestMethod -Method Get -Uri "$BaseUrl/api/eventos" -ErrorAction Stop
} catch {
    throw "FestivalAPI no responde en $BaseUrl. Inicia primero la aplicacion con .\mvnw.cmd spring-boot:run. Detalle: $($_.Exception.Message)"
}

function Registrar-Si-Falta {
    param(
        [string]$Recurso,
        [string]$CampoUnico,
        [object]$Registro
    )

    $existentes = @(Invoke-RestMethod -Method Get -Uri "$BaseUrl/api/$Recurso")
    $valorBuscado = $Registro.PSObject.Properties[$CampoUnico].Value

    $encontrado = $existentes | Where-Object {
        $null -ne $_ -and $_.PSObject.Properties[$CampoUnico].Value -eq $valorBuscado
    } | Select-Object -First 1

    if ($null -ne $encontrado) {
        Write-Host "[EXISTE] $Recurso : $valorBuscado (ID $($encontrado.id))"
        return $encontrado
    }

    $json = ConvertTo-Json -InputObject $Registro -Depth 6 -Compress
    $cuerpo = [System.Text.Encoding]::UTF8.GetBytes($json)

    try {
        $nuevo = Invoke-RestMethod -Method Post -Uri "$BaseUrl/api/$Recurso" -ContentType "application/json; charset=utf-8" -Body $cuerpo -ErrorAction Stop
    } catch {
        throw "No se pudo crear un registro en $Recurso ($valorBuscado). Detalle: $($_.Exception.Message)"
    }

    Write-Host "[CREADO] $Recurso : $valorBuscado (ID $($nuevo.id))"
    return $nuevo
}

$idsAsistentes = @{}
$idsOrganizadores = @{}
$idsEventos = @{}

Write-Host ""
Write-Host "=== 1. ASISTENTES ==="
foreach ($asistente in $datos.asistentes) {
    $registro = Registrar-Si-Falta -Recurso "asistentes" -CampoUnico "documento" -Registro $asistente
    $idsAsistentes[$asistente.documento] = $registro.id
}

Write-Host ""
Write-Host "=== 2. ORGANIZADORES ==="
foreach ($organizador in $datos.organizadores) {
    $registro = Registrar-Si-Falta -Recurso "organizadores" -CampoUnico "cuit" -Registro $organizador
    $idsOrganizadores[$organizador.cuit] = $registro.id
}

Write-Host ""
Write-Host "=== 3. EVENTOS ==="
foreach ($evento in $datos.eventos) {
    $registro = Registrar-Si-Falta -Recurso "eventos" -CampoUnico "nombre" -Registro $evento
    $idsEventos[$evento.nombre] = $registro.id
}

Write-Host ""
Write-Host "=== 4. ENTRADAS ==="
foreach ($entrada in $datos.entradas) {
    if (-not $idsAsistentes.ContainsKey($entrada.asistenteDocumento) -or
        -not $idsOrganizadores.ContainsKey($entrada.organizadorCuit) -or
        -not $idsEventos.ContainsKey($entrada.eventoNombre)) {
        throw "Referencia inexistente para la entrada $($entrada.codigo). Verifica datos-demo.json."
    }

    $cuerpo = [PSCustomObject]@{
        tipo = $entrada.tipo
        asistenteId = $idsAsistentes[$entrada.asistenteDocumento]
        organizadorId = $idsOrganizadores[$entrada.organizadorCuit]
        eventoId = $idsEventos[$entrada.eventoNombre]
        precio = $entrada.precio
        codigo = $entrada.codigo
    }
    $null = Registrar-Si-Falta -Recurso "entradas" -CampoUnico "codigo" -Registro $cuerpo
}

Write-Host ""
Write-Host "Datos de demostracion preparados en la base SQLite local."
Write-Host "Asistentes de ejemplo:   $($datos.asistentes.Count)"
Write-Host "Organizadores de ejemplo: $($datos.organizadores.Count)"
Write-Host "Eventos de ejemplo:      $($datos.eventos.Count)"
Write-Host "Entradas de ejemplo:     $($datos.entradas.Count)"
Write-Host ""
Write-Host "Podes consultar los resultados en:"
Write-Host "  $BaseUrl/api/asistentes"
Write-Host "  $BaseUrl/api/organizadores"
Write-Host "  $BaseUrl/api/eventos"
Write-Host "  $BaseUrl/api/entradas"
