#!/bin/bash
set -e

echo "🌀 Building Cluster Map as it gets lost when zookeeper restarts"
curl --location --request POST 'localhost:8081/buildClusterMap'
