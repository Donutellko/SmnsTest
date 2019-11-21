#!/bin/bash

# shellcheck disable=SC2034
for i in {1..10} ; do
  curl -X POST 'http://localhost:8080/add' \
    --data "value=$((RANDOM / 100 % 90))&lat=$((RANDOM / 100 % 90))&lon=$((RANDOM / 100 % 90))"
done