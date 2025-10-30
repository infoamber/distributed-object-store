#!/bin/bash
set -e

echo "🌀 Exposing storage service to outside world to test APIs"

kubectl port-forward svc/storage-service 8080:8080

