#!/bin/sh
set -e
BASE="$(dirname "$0")"
SRC="$BASE/middleware_src/src"
OUT="$BASE/middleware_build"
JAR="$BASE/lib/middleware/RentExpres.jar"
mkdir -p "$OUT"
find "$OUT" -name '*.class' -delete
# Extraer dependencias y recursos del JAR existente (si existe)
if [ -f "$JAR" ]; then
    unzip -qo "$JAR" 'lib/*' -d "$OUT" >/dev/null
    unzip -qo "$JAR" '*.properties' -d "$OUT" >/dev/null
fi

# Construir classpath con las dependencias extraídas
CP=$(find "$OUT/lib" -name '*.jar' | tr '\n' ':')

# Compilar las clases de middleware incluyendo las dependencias
javac -cp "$CP" -d "$OUT" $(find "$SRC" -name '*.java')

# Generar nuevo JAR con las clases compiladas y las dependencias
jar cf "$JAR" -C "$OUT" .
rm -rf "$OUT"
echo "Built $JAR"
