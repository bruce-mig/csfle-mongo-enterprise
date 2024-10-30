# Set up vault

###  Run Hashicorp Vault
`docker compose up`

### connect to vault container
`docker exec -it mongodb-csfle-vault-1 /bin/sh`
### get vault status, initially vault is at sealed status(Sealed true)
`vault status`

### initialize vault cluster
### it will generates 5 master key shares and root token which can use to authenticate http client
`vault operator init`


### unseal vault server, we need to give 3 key shares unseal the vault
### exectued vault operator unseal command three times with three different key shares
`vault operator unseal`

# login to vault with root token
`vault login`

# enable kv secret engine
`vault secrets enable -path=csfle kv`

# Create a Policy
Define a policy that allows access to the KV secrets in the csfle path:

```bash
docker cp app/vault/policies/csfle-policy.hcl mongodb-csfle-vault-1:/csfle-policy.hcl
vault policy write csfle-policy csfle-policy.hcl
vault policy list
```

# Create a Token with the Policy
You can create a new token that includes this policy:
```bash
vault token create -policy=csfle-policy -ttl=12h
```