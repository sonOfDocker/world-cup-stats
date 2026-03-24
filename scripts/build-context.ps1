#!/usr/bin/env pwsh

$ErrorActionPreference = "Stop"

$Output = if ($args.Length -ge 1) { $args[0] } else { "context-bundle.md" }
$Root = if ($args.Length -ge 2) { $args[1] } else { "." }

$RootAbs = (Resolve-Path $Root).Path
$OutputPath = Join-Path $RootAbs $Output

# Remove existing output file if it exists
if (Test-Path $OutputPath) {
    Remove-Item $OutputPath -Force
}

$TempFile = New-TemporaryFile

try {
    @"
# Context Bundle

Generated: $(Get-Date -Format "yyyy-MM-dd HH:mm:ss")
Repository Root: $RootAbs

---
"@ | Set-Content -Path $OutputPath

    Get-ChildItem -Path $RootAbs -Recurse -File -Include *.md, *.markdown |
            Where-Object {
                $_.FullName -notmatch "\\.git\\" -and
                        $_.FullName -notmatch "\\node_modules\\" -and
                        $_.FullName -notmatch "\\target\\" -and
                        $_.FullName -notmatch "\\build\\" -and
                        $_.FullName -notmatch "\\out\\" -and
                        $_.FullName -notmatch "\\dist\\" -and
                        $_.FullName -notmatch "\\.idea\\" -and
                        $_.FullName -notmatch "\\.gradle\\"
            } |
            Sort-Object FullName |
            Select-Object -ExpandProperty FullName |
            Set-Content $TempFile

    $FoundAny = $false

    Get-Content $TempFile | ForEach-Object {
        $file = $_

        if ((Resolve-Path $file).Path -eq $OutputPath) {
            return
        }

        $FoundAny = $true

        $relativePath = $file.Substring($RootAbs.Length + 1)

        @"

---
# FILE: $relativePath
---

"@ | Add-Content -Path $OutputPath

        Get-Content $file | Add-Content -Path $OutputPath
    }

    if (-not $FoundAny) {
        Add-Content -Path $OutputPath "`nNo markdown files found."
        Write-Host "No markdown files found under $RootAbs"
        exit 0
    }

    $count = (Get-Content $TempFile | Measure-Object).Count

    Write-Host "Context bundle created: $OutputPath"
    Write-Host "Markdown files included: $count"
}
finally {
    Remove-Item $TempFile -Force
}