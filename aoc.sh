#!/bin/bash
DAY=${1:?No day provided}

SUFFIX=".txt"
if [[ $2 == "test" ]]; then SUFFIX="test.txt"; fi

if [[ -e "input/${DAY}${SUFFIX}" ]]; then
    FILENAME="input/${DAY}${SUFFIX}"
else
    DAY_NUMBER_ONLY=$(echo "$DAY" | sed 's/[a-z]//')
    FILENAME="input/${DAY_NUMBER_ONLY}${SUFFIX}"
fi

clj -M -m "day${DAY}" "${FILENAME}"
