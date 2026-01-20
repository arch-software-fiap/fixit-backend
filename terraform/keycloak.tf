resource "kubernetes_config_map" "keycloak_realm" {
  metadata {
    name      = "keycloak-realm"
    namespace = kubernetes_namespace.fixit.metadata[0].name
  }

  data = {
    "realm-export.json" = file("${path.module}/../.docker/keycloak/realm-export.json")
  }
}

resource "kubernetes_deployment" "keycloak" {
  metadata {
    name      = "keycloak"
    namespace = kubernetes_namespace.fixit.metadata[0].name
  }

  spec {
    replicas = 1

    selector {
      match_labels = {
        app = "keycloak"
      }
    }

    template {
      metadata {
        labels = {
          app = "keycloak"
        }
      }

      spec {
        container {
          name  = "keycloak"
          image = "quay.io/keycloak/keycloak:23.0"

          args = ["start-dev", "--import-realm"]

          port {
            container_port = 8080
          }

          env {
            name  = "KEYCLOAK_ADMIN"
            value = var.keycloak_admin_user
          }

          env {
            name  = "KEYCLOAK_ADMIN_PASSWORD"
            value = var.keycloak_admin_password
          }

          env {
            name  = "KC_HTTP_ENABLED"
            value = "true"
          }

          env {
            name  = "KC_HOSTNAME_STRICT"
            value = "false"
          }

          env {
            name  = "KC_HOSTNAME_STRICT_HTTPS"
            value = "false"
          }

          env {
            name  = "KC_HEALTH_ENABLED"
            value = "true"
          }

          resources {
            requests = {
              cpu    = "250m"
              memory = "512Mi"
            }
            limits = {
              memory = "1Gi"
            }
          }

          volume_mount {
            name       = "realm-config"
            mount_path = "/opt/keycloak/data/import"
            read_only  = true
          }

          liveness_probe {
            http_get {
              path = "/health/live"
              port = 8080
            }
            initial_delay_seconds = 120
            period_seconds        = 30
            failure_threshold     = 10
          }

          readiness_probe {
            http_get {
              path = "/health/ready"
              port = 8080
            }
            initial_delay_seconds = 60
            period_seconds        = 10
            failure_threshold     = 10
          }
        }

        volume {
          name = "realm-config"
          config_map {
            name = kubernetes_config_map.keycloak_realm.metadata[0].name
          }
        }
      }
    }
  }
}

resource "kubernetes_service" "keycloak" {
  metadata {
    name      = "keycloak"
    namespace = kubernetes_namespace.fixit.metadata[0].name
  }

  spec {
    selector = {
      app = "keycloak"
    }

    port {
      port        = 8080
      target_port = 8080
      node_port   = 30085
    }

    type = "NodePort"
  }
}
