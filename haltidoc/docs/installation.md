
# Using ansible for new cluster

Playbooks are here:
https://github.com/emblica/halti-management-playbooks

Ansible installs halti-cluster on predefined machines.
It also installs monitoring and metrics collection setup.

After installation halti-master (and so the API) can be found at <private_ip_of_master>:4040

The Grafana by default is in <private_ip_of_master>:3000 and the InfluxDB <private_ip_of_master>:8083/8086

## Dependencies

* python2 (because ansible)
* install dependencies

```
pip install ansible
```


## Running

* install Halti against existing servers

```
ansible-playbook -i hosts site.yml --tags install
```

### Support for capabilities

You can add halti supported capability-setting by defining `capabilities` variable for each host.

ie. `capabilities=public,ssd`

### Notes

* `ansible_eth1` is assumed to be the private IP of the machine
	* edit halti-nodes.yml if this is not the case
* Halti master will run at port 4040 by default

### On Upcloud

Running whole playbook will provision and install a small Halti cluster on UpCloud.

You need to install additional dependencies if you want to provision the servers through UpClouds APIs

```
pip install upcloud_api
```
* check that `upcloud-ansible` submodule is loaded

#### Running

* provision servers and install Halti cluster

```
export UPCLOUD_API_USER=user
export UPCLOUD_API_PASSWD=pass
ansible-playbook -i ./upcloud-ansible/inventory/upcloud.py -M ./upcloud-ansible/modules/ site.yml
```

#### Notes

* `ansible_eth1` is assumed to be the private IP of the machine
	* edit halti-nodes.yml if this is not the case
* Halti master will run at port 4040 by default
* `utils/test_tagging.py` is used by `halti-servers` to ensure the user has tagging permissions
* `utils/destroy_cluster.py` can be used to clean up (main usecase is integration testing)


# From command line

## Master

Halti-server is supposed to be run in at least one server.

To run the latest version of halti-server issue following commands:

```
# Run mongodb
docker run --name mongodb -d mongo

# Run Halti server (master)
docker run -d --restart=always --name=halti-master --env="PRODUCTION=yes" --env="PORT=4040" --env="MONGO_URI=mongodb://mongodb/halti" -p <host-private-ip>:4040:4040/tcp --link mongodb emblica/halti-server
```
## Worker

Each worker node must have halti-agent running on. Halti agent takes care of the container management.

Halti-agent is stateless but it's recommended to mount volume for production usage so the state won't clutter with dead instances/hosts if container restarts by version upgrade or so.

**PORT_BIND_IP** is special environment variable for the agent it will bind the services to.

It must be accessible from the loadbalancing edge nodes so it usually is the LAN-address of the halti worker machine.

```
docker run -d --restart=always --privileged -v /var/run/docker.sock:/var/run/docker.sock -e DOCKER_HOST=unix:///var/run/docker.sock -e HALTI_SERVER=https://halti-master-address:4040 -e PORT_BIND_IP=192.168.1.100 emblica/halti-agent
```

## Luotsi - loadbalancing

Usually all halti worker nodes are also edge nodes but that is not a requirement.

Luotsi has following dependencies:
- latest stable Haproxy
- latest stable Node.js

### Installing luotsi
```
git clone https://github.com/emblica/luotsi.git
cd luotsi
# Install dependencies
npm install
```

### Running luotsi

Before luotsi can be run you must first setup all the needed environment variables:

```
SSL_ENABLED=true/false
CERT_PATH=absolute path to certificate pem
MAINTENANCE_PAGE=absolute path to haproxy compatible maintenance page
STATS_USER=username of haproxy status web gui
STATS_PASS=password of haproxy status web guy
HALTI_URL=url of the halti master
```


After environment variables are set just
```
node luotsi.js
```
