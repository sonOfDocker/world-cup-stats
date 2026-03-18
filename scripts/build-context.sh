#!/usr/bin/env bash

set -euo pipefail

OUTPUT="${1:-context-bundle.md}"
ROOT="${2:-.}"

ROOT_ABS="$(cd "$ROOT" && pwd)"
OUTPUT_PATH="$ROOT_ABS/$OUTPUT"

TMP_FILE="$(mktemp)"

cleanup() {
  rm -f "$TMP_FILE"
}
trap cleanup EXIT

{
  echo "# Context Bundle"
  echo
  echo "Generated: $(date '+%Y-%m-%d %H:%M:%S')"
  echo "Repository Root: $ROOT_ABS"
  echo
  echo "---"
} > "$OUTPUT_PATH"

find "$ROOT_ABS" \
  \( \
    -path "*/.git/*" -o \
    -path "*/node_modules/*" -o \
    -path "*/target/*" -o \
    -path "*/build/*" -o \
    -path "*/out/*" -o \
    -path "*/dist/*" -o \
    -path "*/.idea/*" -o \
    -path "*/.gradle/*" \
  \) -prune -o \
  -type f \( -iname "*.md" -o -iname "*.markdown" \) -print | sort > "$TMP_FILE"

FOUND_ANY=0

while IFS= read -r file; do
  # Exclude the generated output file itself
  if [[ "$(cd "$(dirname "$file")" && pwd)/$(basename "$file")" == "$OUTPUT_PATH" ]]; then
    continue
  fi

  FOUND_ANY=1

  # Relative path from repo root
  relative_path="${file#$ROOT_ABS/}"

  {
    echo
    echo "---"
    echo "# FILE: $relative_path"
    echo "---"
    echo
    cat "$file"
  } >> "$OUTPUT_PATH"

done < "$TMP_FILE"

if [[ "$FOUND_ANY" -eq 0 ]]; then
  echo
  echo "No markdown files found." >> "$OUTPUT_PATH"
  echo "No markdown files found under $ROOT_ABS"
  exit 0
fi

echo "Context bundle created: $OUTPUT_PATH"
echo "Markdown files included: $(wc -l < "$TMP_FILE")"