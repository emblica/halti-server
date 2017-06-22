# How to deploy Hello world?

To deploy Hello world we're expecting that you already have running Halti cluster with at least one master node and one worker node.
They may locate in single physical (or virtual) host but both systems have to be running.

## Requirements

- Working Halti cluster running
- curl
- jq (for managing and formatting the output of curl)

## Service

First part is to create service - the running http process that can serve the Hello World-page.

Easiest way is to use `busybox` (https://hub.docker.com/_/busybox/) image.
The image is using port 8080 to run web server.

First operation is to define the `hello-world-service` as JSON:

```
{
    "name": "hello-world-service",
    "cpu": 0.1,
    "enabled": true,
    "memory": 128,
    "instances": 1,
    "requirements": [],
    "version": "v1",
    "image": "busybox",
    "command": "httpd -f -vv -p 8080",
    "ports": [
        8080
    ],
    "environment": []
}
```

You can find that service description from 'examples/' of the halti-server
After that run following command from command line to create new service:

```
HELLO_WORLD_SERVICE=$(curl -XPOST http://halti-master.example.com:4040/api/v1/services -d @examples/hello_world_service.json -H 'Content-Type: application/json' | jq -r .service.service_id)
#% Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
#                               Dload  Upload   Total   Spent    Left  Speed
#100   507  100   268  100   239  21972  19594 --:--:-- --:--:-- --:--:-- 22333
echo $HELLO_WORLD_SERVICE
# 4897c316-c7dd-4238-919f-cb8efce569ae
```

Check that your service is scheduled to run in some server:
```
curl "http://halti-master.services.upcloud.com:4040/api/v1/services/${HELLO_WORLD_SERVICE}" | jq .
```
Prints out:
```
{
  "service": {
    "requirements": [],
    "name": "hello-world-service",
    "allocated_instances": [
      "c25269a3-7968-48e6-b5cf-b5ef68747675"
    ],
    "memory": 128,
    "command": "httpd -f -vv -p 8080",
    "running_on": [],
    "ports": [
      {
        "protocol": "tcp",
        "port": 8080
      }
    ],
    "instances": 1,
    "image": "busybox",
    "environment": [],
    "service_id": "4897c316-c7dd-4238-919f-cb8efce569ae",
    "version": "v1",
    "enabled": true,
    "cpu": 0.1
  }
}
```
The `allocated_instances` shows all the instances the container is scheduled to run. It will take a little time to pull the image from repository.

If you are curious what is happening inside the instance you can do following:

```
# Get latest 5 events from the allocated instance
curl "http://halti-master.example.com:4040/api/v1/instances/c25269a3-7968-48e6-b5cf-b5ef68747675/events" | jq '.events | .[-5:-1]'
```
Events look like this:
```
[
  {
    "meta": "busybox",
    "event_type": "INFO",
    "event": "PULL_START",
    "instance_id": "c25269a3-7968-48e6-b5cf-b5ef68747675",
    "timestamp": "2017-06-20T23:27:40.386Z"
  },
  {
    "meta": "4897c316-c7dd-4238-919f-cb8efce569ae",
    "event_type": "INFO",
    "event": "START_CONTAINER",
    "instance_id": "c25269a3-7968-48e6-b5cf-b5ef68747675",
    "timestamp": "2017-06-20T23:29:35.365Z"
  }
]
```
There you can see that the instance started to pull the image and after that it started the container.


If you now issue the same command again than earlier you should see following output:
```
{
  "service": {
    "requirements": [],
    "name": "hello-world-service",
    "allocated_instances": [
      "c25269a3-7968-48e6-b5cf-b5ef68747675"
    ],
    "memory": 128,
    "command": "httpd -f -vv -p 8080",
    "running_on": [
      {
        "instance_id": "c25269a3-7968-48e6-b5cf-b5ef68747675",
        "address": "10.1.5.189",
        "port": 32768,
        "source": 8080
      }
    ],
    "ports": [
      {
        "protocol": "tcp",
        "port": 8080
      }
    ],
    "instances": 1,
    "image": "busybox",
    "environment": [],
    "service_id": "4897c316-c7dd-4238-919f-cb8efce569ae",
    "version": "v1",
    "enabled": true,
    "cpu": 0.1
  }
}
```
As you can see now the service is running on instance **c25269a3-7968-48e6-b5cf-b5ef68747675** at port **10.1.5.189:32768** and it's accessible from internal network only.

To publish the service you must have **loadbalancer**

## Loadbalancer

Loadbalancer is using **Luotsi** component to route HTTP traffic into services.
It works by reading the loadbalancer config endpoint and autoconfiguring **Haproxy** from that.

To get access to our just created **hello-world-service** we have to create loadbalancer for that.

1. First edit your loadbalancer declaration at `examples/hello_world_loadbalancer.json` and add service id of the `hello-world-service` into that.

```
{
    "name": "hello-world-loadbalancer",
    "enabled": true,
    "hostname": "hello.example.com",
    "service_id": "<hello-world-service-id>",
    "source_port": 8080,
    "force_https": false,
    "ports": {"http": true, "https": false}
}
```

The source port (`source_port`) is same as defined in service declaration (8080). We also disable HTTPS for this service because we're not using SSL-certificates in this getting started.

`force_https` is forcing HTTPS redirect in loadbalancer layer and it should be used in production.

`hostname` is going to be matched into HTTPs `Host`-header and the routing is made based on that.

2. Add loadbalancer declaration to halti with curl

```
HELLO_WORLD_LB=$(curl -XPOST http://halti-master.example.com:4040/api/v1/loadbalancers -d @examples/hello_world_loadbalancer.json -H 'Content-Type: application/json' | jq -r .loadbalancer.loadbalancer_id)
```

3. After a few seconds the routing should be online and if you are pointing your dns into the edge nodes you should be able to access the `hello-world-service` by browsing `http://hello.example.com`

## Tips

If you can't see your container to spin up make sure it's first scheduled by seeing if the service has instances listed under `allocated_instances`.

If there is `allocated_instances` scheduler has already scheduled it to run in some node(s).
So then the problem is outside Halti master, somewhere else.

To investigate scheduling problems you should curl the following endpoint:
```
curl http://halti-master.example.com:4040/api/v1/state | jq .
```

```
{
  "unscheduled": [
    {
      "problem": {
        "host": {
          "services": [
            "1411c846-3353-45f0-a5d5-0fc35afa20bc",
            "4897c316-c7dd-4238-919f-cb8efce569ae",
            "a60c6800-80f0-45e1-a985-5eaf7b83afcd",
            "6eaad1ba-e14b-46cc-adfb-6d79cc786420"
          ],
          "containers": [
            "6eaad1ba-e14b-46cc-adfb-6d79cc786420",
            "a60c6800-80f0-45e1-a985-5eaf7b83afcd",
            "4897c316-c7dd-4238-919f-cb8efce569ae",
            "1411c846-3353-45f0-a5d5-0fc35afa20bc"
          ],
          "capabilities": [
            "public"
          ],
          "memory": 402,
          "cpu": 0.30000000000000004,
          "instance-id": "c25269a3-7968-48e6-b5cf-b5ef68747675"
        },
        "reason": "not-enough-memory"
      },
      "container-id": 0,
      "requirements": [],
      "cpu": 0.1,
      "memory": 1280,
      "service-id": "8744f08e-db63-40d3-b47d-87bbda8555a1"
    }
  ]
}
```

As you can see there is one service (*8744f08e-db63-40d3-b47d-87bbda8555a1*) unscheduled
because it was otherwise suitable to run in instance `c25269a3-7968-48e6-b5cf-b5ef68747675`
but the intance didn't have any more memory available. To schedule that service you have to change memory requirements, vertically scale your nodes or add more nodes to your cluster.

In this situation there is only *402MB* free memory but the requirement is *1280MB*



#### List all of your services
```
curl http://halti-master.example.com:4040/api/v1/services | jq '.services | .[] | {name: .name, service: .service_id}'
```
