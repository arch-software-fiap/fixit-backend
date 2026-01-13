#!/usr/bin/env bash

set -e

SECRET_NAME="ghcr-secret"
NAMESPACE="default"
REGISTRY="ghcr.io"

echo "🔐 Criando secret para GitHub Container Registry (GHCR)"
echo

read -p "GitHub username: " GH_USERNAME
read -s -p "GitHub token (read:packages): " GH_TOKEN
echo

if [ -z "$GH_USERNAME" ] || [ -z "$GH_TOKEN" ]; then
  echo "❌ Username ou token não informado"
  exit 1
fi

kubectl delete secret $SECRET_NAME -n $NAMESPACE --ignore-not-found

kubectl create secret docker-registry $SECRET_NAME \
  --docker-server=$REGISTRY \
  --docker-username=$GH_USERNAME \
  --docker-password=$GH_TOKEN \
  --docker-email="local@dev.com" \
  -n $NAMESPACE

echo
echo "✅ Secret '$SECRET_NAME' criado com sucesso no namespace '$NAMESPACE'"
