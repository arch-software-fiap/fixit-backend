resource "kubernetes_secret" "app_secrets" {
  metadata {
    name      = "fixit-backend-secret"
    namespace = kubernetes_namespace.fixit.metadata[0].name
  }

  data = {
    SPRING_DATASOURCE_URL             = "jdbc:postgresql://postgres:5432/${var.postgres_db}"
    SPRING_DATASOURCE_USERNAME        = var.postgres_user
    SPRING_DATASOURCE_PASSWORD        = var.postgres_password
    KEYCLOAK_SERVER_URL               = "http://keycloak:8080"
    LOGGING_LEVEL                     = var.logging_level
    FIXIT_EXTERNAL_AUTH_SHARED_SECRET = var.fixit_external_auth_shared_secret
    FIXIT_GATEWAY_LAMBDA_URL          = var.fixit_gateway_lambda_url
  }

  type = "Opaque"
}

resource "kubernetes_secret" "ghcr_secret" {
  metadata {
    name      = "ghcr-secret"
    namespace = kubernetes_namespace.fixit.metadata[0].name
  }

  type = "kubernetes.io/dockerconfigjson"

  data = {
    ".dockerconfigjson" = jsonencode({
      auths = {
        "ghcr.io" = {
          auth = base64encode("${var.ghcr_username}:${var.ghcr_token}")
        }
      }
    })
  }
}

resource "kubernetes_deployment" "app" {
  metadata {
    name      = "fixit-backend"
    namespace = kubernetes_namespace.fixit.metadata[0].name
  }

  spec {
    replicas = var.app_replicas

    selector {
      match_labels = {
        app = "fixit-backend"
      }
    }

    template {
      metadata {
        labels = {
          app = "fixit-backend"
        }
      }

      spec {
        image_pull_secrets {
          name = kubernetes_secret.ghcr_secret.metadata[0].name
        }

        container {
          name  = "fixit-backend"
          image = var.app_image

          port {
            container_port = 8080
          }

          env_from {
            secret_ref {
              name = kubernetes_secret.app_secrets.metadata[0].name
            }
          }

          resources {
            requests = {
              cpu    = "125m"
              memory = "512Mi"
            }
            limits = {
              memory = "1Gi"
            }
          }

          liveness_probe {
            http_get {
              path = "/fixit-backend/actuator/health"
              port = 8080
            }
            initial_delay_seconds = 60
            period_seconds        = 30
          }

          readiness_probe {
            http_get {
              path = "/fixit-backend/actuator/health"
              port = 8080
            }
            initial_delay_seconds = 30
            period_seconds        = 10
          }
        }
      }
    }
  }

  depends_on = [
    kubernetes_deployment.postgres,
    kubernetes_deployment.keycloak
  ]
}

resource "kubernetes_service" "app" {
  metadata {
    name      = "fixit-backend-service"
    namespace = kubernetes_namespace.fixit.metadata[0].name
  }

  spec {
    selector = {
      app = "fixit-backend"
    }

    port {
      name        = "http"
      port        = 80
      target_port = 8080
      node_port   = 30080
    }

    type = "NodePort"
  }
}

resource "kubernetes_horizontal_pod_autoscaler_v2" "app" {
  metadata {
    name      = "fixit-backend-hpa"
    namespace = kubernetes_namespace.fixit.metadata[0].name
  }

  spec {
    scale_target_ref {
      api_version = "apps/v1"
      kind        = "Deployment"
      name        = kubernetes_deployment.app.metadata[0].name
    }

    min_replicas = 1
    max_replicas = 5

    metric {
      type = "Resource"
      resource {
        name = "cpu"
        target {
          type                = "Utilization"
          average_utilization = 70
        }
      }
    }
  }
}
