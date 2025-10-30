#!/bin/bash
set -e

# ----------------------------
# 2️⃣ Deleting existing statefulset zookeeper
# ----------------------------
kubectl delete statefulset zookeeper --ignore-not-found
kubectl delete statefulset zookeeper -n zookeeper-demo --ignore-not-found

# ----------------------------
# 2️⃣ Apply Kubernetes YAML for Zookeeper
# ----------------------------
K8S_FILE_ZK="zookeeper-cluster.yaml"
echo "📦 Applying Kubernetes manifests for zookeeper..."
kubectl apply -f $K8S_FILE_ZK

# Name of your StatefulSet
STATEFULSET_NAME="zookeeper"
NAMESPACE="default"  # Change if your namespace is different

# Wait for all pods in the StatefulSet to be ready
echo "Waiting for Zookeeper pods to be ready..."
kubectl rollout status statefulset/$STATEFULSET_NAME -n $NAMESPACE

# Optional: extra safety, check each pod individually
PODS=$(kubectl get pods -l app=$STATEFULSET_NAME -n $NAMESPACE -o name)
for pod in $PODS; do
    echo "Waiting for pod $pod to be ready..."
    kubectl wait --for=condition=Ready $pod -n $NAMESPACE --timeout=180s
done

echo "Zookeeper is up and running!"

echo "🏗️  Building Docker images for s3demo project..."

# Go to the script's directory (s3demo/)
cd "$(dirname "$0")"

# Build filenode image
echo "🚀 Building filenode image..."
docker build -t local/file-node:latest ./filenode

# Build filemapper image
echo "🚀 Building filemapper image..."
docker build -t local/filemapper:latest ./filemapper

# Build storage-service image
echo "🚀 Building storage-service image..."
docker build -t local/storage-service:latest ./storage-service

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

echo "🔄 Deleting and recreating pods: storage-service"
kubectl delete pod -l app=storage-service

echo "✅ All services restarted with latest images."

echo "⏳ Waiting for pods to be ready..."

