#!/bin/bash
set -e

echo "🏗️  Building Docker images for s3demo project..."

# Go to the script's directory (s3demo/)
cd "$(dirname "$0")"

# Build filenode image
echo "🚀 Building filenode image..."
docker build -t local/file-node:latest ./filenode

# Build filemapper image
echo "🚀 Building filemapper image..."
docker build -t local/filemapper:latest ./filemapper

echo "✅ All images built successfully!"
echo ""
echo "To verify, run:"
echo "  docker images | grep local/"

# ----------------------------
# 2️⃣ Apply Kubernetes YAML
# ----------------------------
K8S_FILE="k8s-file-node.yaml"
echo "📦 Applying Kubernetes manifests..."
kubectl apply -f $K8S_FILE

# ----------------------------
# 3️⃣ Rollout restart to pick up new images
# ----------------------------
echo "🌀 Performing rolling restart of all services..."

echo "🔄 Deleting and recreating pod StatefulSet: file-node"
kubectl delete pod -l app=file-node

echo "🔄 Deleting and recreating pods: filemapper"
kubectl delete pod -l app=filemapper

echo "✅ All services restarted with latest images."