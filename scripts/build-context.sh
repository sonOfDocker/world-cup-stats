#!/usr/bin/env bash

set -euo pipefail

OUTPUT="${1:-context-bundle.md}"
ROOT="${2:-.}"
INCLUDE_ADDITIONAL_MARKDOWN="${INCLUDE_ADDITIONAL_MARKDOWN:-false}"

ROOT_ABS="$(cd "$ROOT" && pwd)"
OUTPUT_PATH="$ROOT_ABS/$OUTPUT"

normalize_path() {
  local path_value="$1"
  local abs_path
  if [[ -d "$path_value" ]]; then
    abs_path="$(cd "$path_value" && pwd)"
  else
    local dir
    dir="$(dirname "$path_value")"
    local base
    base="$(basename "$path_value")"
    abs_path="$(cd "$dir" && pwd)/$base"
  fi
  echo "$abs_path"
}

is_excluded_path() {
  local full_path
  full_path="$(normalize_path "$1" | tr '[:upper:]' '[:lower:]')"

  [[ "$full_path" == *"/.git/"* ]] \
    || [[ "$full_path" == *"/node_modules/"* ]] \
    || [[ "$full_path" == *"/target/"* ]] \
    || [[ "$full_path" == *"/build/"* ]] \
    || [[ "$full_path" == *"/out/"* ]] \
    || [[ "$full_path" == *"/dist/"* ]] \
    || [[ "$full_path" == *"/.idea/"* ]] \
    || [[ "$full_path" == *"/.gradle/"* ]] \
    || [[ "$full_path" == *"/coverage/"* ]] \
    || [[ "$full_path" == *"/vendor/"* ]]
}

resolve_repo_file() {
  local relative_path="$1"
  local candidate="$ROOT_ABS/$relative_path"

  if [[ -f "$candidate" ]]; then
    normalize_path "$candidate"
  fi
}

add_section_header() {
  local path="$1"
  local title="$2"
  local description="$3"

  cat >> "$path" <<EOF

## $title

$description

EOF
}

add_file_to_bundle() {
  local bundle_path="$1"
  local file_path="$2"

  local full_path
  full_path="$(normalize_path "$file_path")"

  # Find the position of the root in the full path (case-insensitive)
  local root_abs_lower
  root_abs_lower="$(echo "$ROOT_ABS" | tr '[:upper:]' '[:lower:]')"
  local full_path_lower
  full_path_lower="$(echo "$full_path" | tr '[:upper:]' '[:lower:]')"

  local relative_path
  if [[ "$full_path_lower" == "$root_abs_lower"* ]]; then
    relative_path="${full_path:${#ROOT_ABS}}"
    relative_path="${relative_path#/}"
    relative_path="${relative_path#\\}"
  else
    relative_path="$full_path"
  fi

  cat >> "$bundle_path" <<EOF

---
# FILE: $relative_path
---

EOF

  cat "$full_path" >> "$bundle_path"
}

append_directory_markdown() {
  local bundle_path="$1"
  local section_title="$2"
  local section_description="$3"
  local relative_directory="$4"
  local section_files_added_ref="$5"

  local dir="$ROOT_ABS/$relative_directory"
  [[ -d "$dir" ]] || return 0

  while IFS= read -r file; do
    [[ -n "$file" ]] || continue

    local normalized
    normalized="$(normalize_path "$file")"
    local normalized_lower
    normalized_lower="$(echo "$normalized" | tr '[:upper:]' '[:lower:]')"

    if [[ "$normalized_lower" == "$OUTPUT_PATH_NORMALIZED" ]]; then
      continue
    fi

    if is_excluded_path "$normalized"; then
      continue
    fi

    if [[ -n "${INCLUDED_SET["$normalized_lower"]+x}" ]]; then
      continue
    fi

    if [[ "${!section_files_added_ref}" -eq 0 ]]; then
      add_section_header "$bundle_path" "$section_title" "$section_description"
    fi

    add_file_to_bundle "$bundle_path" "$normalized"
    INCLUDED_SET["$normalized_lower"]=1
    INCLUDED_FILES+=("$normalized")
    printf -v "$section_files_added_ref" '%d' "$(( ${!section_files_added_ref} + 1 ))"
  done < <(find "$dir" -maxdepth 1 -type f \( -iname "*.md" -o -iname "*.markdown" \) | sort -f)
}

rm -f "$OUTPUT_PATH"
OUTPUT_PATH_NORMALIZED="$(normalize_path "$OUTPUT_PATH" | tr '[:upper:]' '[:lower:]')"

declare -a INCLUDED_FILES=()
declare -a MISSING_EXPECTED_FILES=()
declare -A INCLUDED_SET=()

cat > "$OUTPUT_PATH" <<EOF
# Context Bundle

Generated: $(date '+%Y-%m-%d %H:%M:%S')
Repository Root: $ROOT_ABS
Mode: Curated$( [[ "$INCLUDE_ADDITIONAL_MARKDOWN" == "true" ]] && echo " + Additional Markdown" || true )

This bundle is intended for AI agents performing story refinement, implementation, testing, and review.
Files are ordered intentionally to present high-value context first.

---
EOF

CURATED_SECTIONS=(
  "Repository Entry Point|Top-level repository overview and setup guidance.|README.md|"
  "Agent Workflow Core|Primary agent onboarding and execution guidance.|docs/agent-start-here.md,docs/agent-development-guide.md,docs/agent-task-template.md,docs/ai_context.md|"
  "Project Direction and Domain Context|Core project context, roadmap, domain definitions, and implementation guidance.|docs/vision.md,docs/roadmap.md,docs/implementation-order.md,docs/domain-model.md,docs/data-dictionary.md|"
  "Architecture|System architecture, API design, ingestion design, workflow design, and analytics layer.||docs/architecture"
  "Canonical Domain Definitions|Canonical entity specifications that define the domain model for ingestion and read behavior.||docs/canonical"
  "Contracts|Source-to-canonical and other data or behavior mappings.||docs/contracts"
  "Architecture Decision Records|ADRs that capture decisions and constraints agents should respect.||docs/adr"
  "Diagrams|Supplementary diagrams for architecture and system understanding.||docs/diagrams"
  "Dataset Documentation|Dataset-specific documentation that may support ingestion or interpretation.||docs/dataset,docs/datasets"
  "Task-Specific Notes|Task-oriented documentation and work planning notes.||docs/tasks"
)

for section in "${CURATED_SECTIONS[@]}"; do
  IFS='|' read -r section_title section_description section_files_csv section_directories_csv <<< "$section"
  section_files_added=0

  if [[ -n "$section_files_csv" ]]; then
    IFS=',' read -r -a section_files <<< "$section_files_csv"
    for relative_path in "${section_files[@]}"; do
      [[ -n "$relative_path" ]] || continue

      resolved="$(resolve_repo_file "$relative_path" || true)"

      if [[ -z "$resolved" ]]; then
        MISSING_EXPECTED_FILES+=("$relative_path")
        continue
      fi

      resolved_lower="$(echo "$resolved" | tr '[:upper:]' '[:lower:]')"

      if [[ "$resolved_lower" == "$OUTPUT_PATH_NORMALIZED" ]]; then
        continue
      fi

      if is_excluded_path "$resolved"; then
        continue
      fi

      if [[ -z "${INCLUDED_SET["$resolved_lower"]+x}" ]]; then
        if [[ "$section_files_added" -eq 0 ]]; then
          add_section_header "$OUTPUT_PATH" "$section_title" "$section_description"
        fi

        add_file_to_bundle "$OUTPUT_PATH" "$resolved"
        INCLUDED_SET["$resolved_lower"]=1
        INCLUDED_FILES+=("$resolved")
        section_files_added=$((section_files_added + 1))
      fi
    done
  fi

  if [[ -n "$section_directories_csv" ]]; then
    IFS=',' read -r -a section_directories <<< "$section_directories_csv"
    for relative_directory in "${section_directories[@]}"; do
      [[ -n "$relative_directory" ]] || continue
      append_directory_markdown "$OUTPUT_PATH" "$section_title" "$section_description" "$relative_directory" section_files_added
    done
  fi
done

if [[ "$INCLUDE_ADDITIONAL_MARKDOWN" == "true" ]]; then
  add_section_header \
    "$OUTPUT_PATH" \
    "Additional Markdown Files" \
    "Supplementary markdown files discovered automatically, excluding already included files and ignored directories."

  while IFS= read -r file; do
    [[ -n "$file" ]] || continue

    normalized="$(normalize_path "$file")"
    normalized_lower="$(echo "$normalized" | tr '[:upper:]' '[:lower:]')"

    if [[ "$normalized_lower" == "$OUTPUT_PATH_NORMALIZED" ]]; then
      continue
    fi

    if is_excluded_path "$normalized"; then
      continue
    fi

    if [[ -n "${INCLUDED_SET["$normalized_lower"]+x}" ]]; then
      continue
    fi

    add_file_to_bundle "$OUTPUT_PATH" "$normalized"
    INCLUDED_SET["$normalized_lower"]=1
    INCLUDED_FILES+=("$normalized")
  done < <(
    find "$ROOT_ABS" \
      \( \
        -path "*/.git/*" -o \
        -path "*/node_modules/*" -o \
        -path "*/target/*" -o \
        -path "*/build/*" -o \
        -path "*/out/*" -o \
        -path "*/dist/*" -o \
        -path "*/.idea/*" -o \
        -path "*/.gradle/*" -o \
        -path "*/coverage/*" -o \
        -path "*/vendor/*" \
      \) -prune -o \
      -type f \( -iname "*.md" -o -iname "*.markdown" \) -print | sort
  )
fi

if [[ "${#INCLUDED_FILES[@]}" -eq 0 ]]; then
  echo >> "$OUTPUT_PATH"
  echo "No curated markdown files were found." >> "$OUTPUT_PATH"
  echo "Warning: No curated markdown files were found under $ROOT_ABS"
  exit 0
fi

if [[ "${#MISSING_EXPECTED_FILES[@]}" -gt 0 ]]; then
  cat >> "$OUTPUT_PATH" <<EOF

## Missing Expected Files

The following curated files were not found and were skipped:

EOF

  printf '%s\n' "${MISSING_EXPECTED_FILES[@]}" | sort -u | sed 's/^/- /' >> "$OUTPUT_PATH"
  echo "Warning: Some curated files were not found. See 'Missing Expected Files' in the bundle."
fi

MISSING_COUNT="$(printf '%s\n' "${MISSING_EXPECTED_FILES[@]:-}" | sed '/^$/d' | sort -u | wc -l | tr -d ' ')"

cat >> "$OUTPUT_PATH" <<EOF

## Bundle Summary

- Curated files included: ${#INCLUDED_FILES[@]}
- Missing curated files skipped: $MISSING_COUNT
- Additional markdown included: $( [[ "$INCLUDE_ADDITIONAL_MARKDOWN" == "true" ]] && echo "Yes" || echo "No" )

EOF

echo "Context bundle created: $OUTPUT_PATH"
echo "Curated files included: ${#INCLUDED_FILES[@]}"
echo "Missing curated files skipped: $MISSING_COUNT"
echo "Additional markdown included: $( [[ "$INCLUDE_ADDITIONAL_MARKDOWN" == "true" ]] && echo "Yes" || echo "No" )"