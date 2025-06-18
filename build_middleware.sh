#!/bin/sh
set -e
BASE="$(dirname "$0")"
SRC="$BASE/middleware_src/src"
OUT="$BASE/middleware_build"
JAR="$BASE/lib/middleware/RentExpres.jar"
mkdir -p "$OUT"
find "$OUT" -name '*.class' -delete
javac -d "$OUT" $(find "$SRC" -name '*.java')
jar cf "$JAR" -C "$OUT" .
rm -rf "$OUT"
echo "Built $JAR"
