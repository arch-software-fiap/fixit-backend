#!/bin/bash

echo "Aguardando SonarQube iniciar..."

# Aguardar SonarQube estar pronto
until curl -s http://localhost:9000/api/system/status | grep -q '"status":"UP"'; do
    echo "SonarQube ainda não está pronto..."
    sleep 5
done

echo "SonarQube está pronto!"

SONAR_URL="http://localhost:9000"
SONAR_USER="admin"
SONAR_PASS="admin"

NEW_PASSWORD="NovaSenh@123"

echo "Trocando senha padrão..."
curl -u $SONAR_USER:$SONAR_PASS -X POST "$SONAR_URL/api/users/change_password" \
  -d "login=$SONAR_USER&previousPassword=$SONAR_PASS&password=$NEW_PASSWORD"

# Atualizar credenciais
SONAR_PASS=$NEW_PASSWORD

# Criar projeto
PROJECT_KEY="fixit-backend"
PROJECT_NAME="FixIt Backend"

echo "Criando projeto $PROJECT_NAME..."
curl -u $SONAR_USER:$SONAR_PASS -X POST "$SONAR_URL/api/projects/create" \
  -d "project=$PROJECT_KEY&name=$PROJECT_NAME"

# Gerar token
TOKEN_NAME="fixit-token"

echo "Gerando token de acesso..."
TOKEN_RESPONSE=$(curl -u $SONAR_USER:$SONAR_PASS -X POST "$SONAR_URL/api/user_tokens/generate" \
  -d "name=$TOKEN_NAME")

TOKEN=$(echo $TOKEN_RESPONSE | grep -o '"token":"[^"]*' | cut -d'"' -f4)

echo ""
echo "Configuração concluída!"
echo "================================"
echo "Seu token: $TOKEN"
echo "Salve em um lugar seguro!"
echo "================================"
echo ""
echo "Para executar a análise:"
echo "mvn clean verify sonar:sonar -Dsonar.login=$TOKEN"
echo ""
echo "Para acessar a analise"
echo "http://localhost:9000/dashboard?id=fixit-backend"
