#!/usr/bin/env bash
rm -rf target && mvn -Pnative clean package && ./target/native-kotlin
