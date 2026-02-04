#!/bin/bash
set -euo pipefail

CERT_DIR="./certs"
CERT_FILE="$CERT_DIR/letsplay.p12"

CERT_PASSWORD="lets-play"
CERT_ALIAS="lets-play"
CERT_DNAME="CN=play, OU=zone, O=LetsPlay, L=Oujda, ST=Oriental, C=MA"
CERT_VALIDITY_DAYS="3650"

mkdir -p "$CERT_DIR"

if [ ! -f "$CERT_FILE" ]; then
  echo "Generating self-signed SSL certificate..."
  keytool -genkeypair \
    -alias "$CERT_ALIAS" \
    -keyalg RSA \
    -keysize 2048 \
    -storetype PKCS12 \
    -keystore "$CERT_FILE" \
    -storepass "$CERT_PASSWORD" \
    -validity "$CERT_VALIDITY_DAYS" \
    -dname "$CERT_DNAME"

  echo "✅ Certificate generated at: $CERT_FILE"
else
  echo "✅ Certificate already exists: $CERT_FILE"
fi

echo
echo "Now add this to .gitignore:"
echo "certs/"
echo
echo "Test with:"
echo "curl -k https://localhost:8443/products"
