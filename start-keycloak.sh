#!/bin/bash

podman run \
  --name keycloak -d --rm \
  -p 127.0.0.1:8080:8080 \
  -e KC_BOOTSTRAP_ADMIN_USERNAME=admin \
  -e KC_BOOTSTRAP_ADMIN_PASSWORD=admin \
  -v ./jobrunr-demo-realm.json:/opt/keycloak/data/import/jobrunr-demo-realm.json:Z \
  quay.io/keycloak/keycloak \
  start-dev --import-realm
