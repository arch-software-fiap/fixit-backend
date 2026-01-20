terraform {
  required_version = ">= 1.0.0"

  required_providers {
    kind = {
      source  = "tehcyx/kind"
      version = "~> 0.6"
    }
    kubernetes = {
      source  = "hashicorp/kubernetes"
      version = "~> 2.35"
    }
    helm = {
      source  = "hashicorp/helm"
      version = "~> 2.17"
    }
  }
}

resource "kind_cluster" "fixit" {
  name           = var.cluster_name
  wait_for_ready = true

  kind_config {
    kind        = "Cluster"
    api_version = "kind.x-k8s.io/v1alpha4"

    node {
      role = "control-plane"

      kubeadm_config_patches = [
        <<-EOT
        kind: InitConfiguration
        nodeRegistration:
          kubeletExtraArgs:
            node-labels: "ingress-ready=true"
        EOT
      ]

      extra_port_mappings {
        container_port = 80
        host_port      = 80
        protocol       = "TCP"
      }

      extra_port_mappings {
        container_port = 443
        host_port      = 443
        protocol       = "TCP"
      }

      extra_port_mappings {
        container_port = 30080
        host_port      = 8080
        protocol       = "TCP"
      }

      extra_port_mappings {
        container_port = 30432
        host_port      = 5432
        protocol       = "TCP"
      }

      extra_port_mappings {
        container_port = 30085
        host_port      = 8085
        protocol       = "TCP"
      }
    }

    node {
      role = "worker"
    }
  }
}

provider "kubernetes" {
  host                   = kind_cluster.fixit.endpoint
  client_certificate     = kind_cluster.fixit.client_certificate
  client_key             = kind_cluster.fixit.client_key
  cluster_ca_certificate = kind_cluster.fixit.cluster_ca_certificate
}

provider "helm" {
  kubernetes {
    host                   = kind_cluster.fixit.endpoint
    client_certificate     = kind_cluster.fixit.client_certificate
    client_key             = kind_cluster.fixit.client_key
    cluster_ca_certificate = kind_cluster.fixit.cluster_ca_certificate
  }
}

resource "kubernetes_namespace" "fixit" {
  metadata {
    name = var.namespace
  }

  depends_on = [kind_cluster.fixit]
}