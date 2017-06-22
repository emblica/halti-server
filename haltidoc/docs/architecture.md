### Architecture

Halti has two essential components. Master-nodes and Worker-nodes.

#### Masters
- Run Halti-server
- Provide API for the cluster
- Schedule and optimize services between healthy worker nodes
- Keep only one master scheduler as active in same time

#### Workers
- Run Halti agent
- Send heartbeat for master
- Try to maintain state that master hopes they have

##### Loadbalancers
- Routes TCP-traffic from outside to services
- Terminates SSL-connections
