# Creating and managing services

Creating services is explained briefly in `Getting started` but it works
by issuing requests to service API endpoints.

API documentation can be found from **API/services**

## Structure of service description
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

- **name** is just arbitrary user recognizable name, it's recommended that it doesn't contain whitespaces though   
- **cpu** 1 CPU core of the host machine is same as 1 CPU core of halti. This is hard limit for scheduling but in reality the service isn't limited by this factor.
- **memory** as MB of ram of the host machine. Also hard limit for scheduling but doesn't limit the actual container at runtime.
- **instances** replica count of this service. Same instance is never running two replicas of the same instance in same time.
- **requirements** list of strings to describe the requirements for this service such as `ssd` or `intranet` or `gpu`. Service is never scheduled on that kind of instances where some of the requirements are missing.
- **version** version of the service. Changing this also forces docker image fetch. Useful when upgrading service.
- **image** registry name/path to the used docker image
- **command** (optional) field to use as command to run the image
- **ports** published ports declaration, this is the port service inside container runs for, can also be UDP-port
- **environment** environment variables as key-value objects


## Updating service

### Non-HA update

Steps:
1. Build docker image and push it to repository
2. Issue `PUT` request to the service description and update at least new **version** (different between previous, it can be a date for example) and also **image** path if you must.
3. Wait for new version of the container to deploy. In the time between pulling the new image and running that there will be small downtime.

### HA update

So called Green-Blue deployment is actually quite easy to handle

Steps:
1. Build docker image and push it to repository using new tag ie. `mywebserver:v1` -> `mywebserver:v2`
2. Create new service (blue) and refer to that image. Have same port configuration as the *green* service.
3. Wait for the new *green* service to deploy and maybe check it is working correctly by configuring some development loadbalancer to point to it. ie. `green.service.example.com` and `blue.service.example.com` and the production traffic will go to `production.service.example.com`
4. After you're happy, issue `PUT` into the production LB and change it to point to the new *green* service instead of the *blue*.
5. If you want to rollback just issue another `PUT` into the production LB and change it point back to *blue* service.

# Creating and managing loadbalancers

See section from API docs.

# Adding new instances to cluster

New instaces can be added to cluster by just configuring the *halti-agent* to run on that instance properly.
It will _call home_ to the pointed **master** and register itself to accept workload.

It's important to take care of **capabilities** of new instances.

# Removing instances from cluster

After instance is missing from the cluster it will reschedule all it's containers to other instances if possible. (following the limits and requirements). Just taking node offline or even shutting down the *halit-agent* will do that.

# Problem solving and common problems

## Service doesn't run on any instance

Usually the problem is in set of these:
- Instance limits reached (scheduling error)
- Docker image isn't pulled (registry or connection error)
- Problem with image or command and it shuts down instantly (container error)


If you can't see your container to spin up make sure it's first scheduled by seeing if the service has instances listed under `allocated_instances`. (See asking the service state from the API)

If there is `allocated_instances` scheduler has already scheduled it to run in some node(s).
So then the problem is outside Halti master, somewhere else.

To investigate scheduling problems you should curl the following endpoint:
```
curl http://halti-master.services.example.com:4040/api/v1/state | jq .
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
