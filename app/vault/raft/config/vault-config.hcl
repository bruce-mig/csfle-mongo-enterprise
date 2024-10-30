ui            = true
cluster_addr  = "http://127.0.0.1:8201"
api_addr      = "http://127.0.0.1:8200"
disable_mlock = true

storage "raft" {
  path = "/vault/data"
  node_id = "raft_node_1"
}

listener "tcp" {
  address       = "0.0.0.0:8200"
  tls_cert_file = "/vault/certs/localhostcert.pem"
  tls_key_file  = "/vault/certs/localhostkey.pem"
  tls_disable = 1 # todo: enable TLS for prod
}