#!/bin/bash
set -e
echo "🌀 Exposing mapper service to outside world to test APIs"
kubectl port-forward svc/filemapper 8081:8080
