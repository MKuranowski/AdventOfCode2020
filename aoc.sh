#!/bin/bash
DAY=${1:?No day provided}
DAY_NUMBER_ONLY=$(echo "$DAY" | sed 's/[a-z]//')

SUFFIX=".txt"
if [[ $2 == "test" ]]; then SUFFIX="test.txt"; fi

if [[ -e "input/${DAY}${SUFFIX}" ]]; then
    FILENAME="input/${DAY}${SUFFIX}"
else
    FILENAME="input/${DAY_NUMBER_ONLY}${SUFFIX}"
fi

if [[ ("$DAY_NUMBER_ONLY" -ge 19) && (-z "$NO_PYTHON") ]]; then
    python "src/day${DAY}.py" "${FILENAME}"
else
    clj -M -m "day${DAY}" "${FILENAME}"
fi
