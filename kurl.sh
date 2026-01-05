#!/bin/bash

if ! command -v jq >/dev/null 2>&1; then
    echo "Instale o jq para continuar."
fi

export ACCESS_TOKEN=$(curl -s -X POST http://localhost:8085/realms/fixit/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=fixit_backend" \
  -d "username=user" \
  -d "password=123" \
  -d "grant_type=password" | jq -r .access_token)

curl -H "Authorization: Bearer $ACCESS_TOKEN" "$@"