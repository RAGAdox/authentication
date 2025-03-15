## GENERATE KEYS FOR LOCAL DEVELOPMENT

```bash
openssl genpkey -algorithm RSA -out ./certs/private.pem -pkeyopt rsa_keygen_bits:2048

openssl rsa -in ./certs/private.pem -pubout -out ./certs/public.pem

openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in ./certs/private.pem -out ./certs/private-pkcs8.pem

cat ./certs/private-pkcs8.pem | sed '1d;$d' | tr -d '\n' > ./certs/private_key_base64.txt
cat ./certs/public.pem | sed '1d;$d' | tr -d '\n' > ./certs/public_key_base64.txt

echo "JWT_PRIVATE_KEY_BASE64=$(cat ./certs/private_key_base64.txt)" >> .env.local
echo "JWT_PUBLIC_KEY_BASE64=$(cat ./certs/public_key_base64.txt)" >> .env.local
```
