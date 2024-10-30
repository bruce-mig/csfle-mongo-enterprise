# csfle-policy.hcl

path "kv/csfle/*" {
  capabilities = ["create", "read", "update", "delete", "list"]
}
