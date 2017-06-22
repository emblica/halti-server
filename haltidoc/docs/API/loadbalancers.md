
# Loadbalancers

| Collection    | Base URL                | Format | Stability | Additional information |
|---------------|-------------------------|--------|-----------|------------------------|
| Loadbalancers | `/api/v1/loadbalancers` | JSON   | -         | -                      |

## List All Loadbalancers

| Method | Format | Response Status (OK) | Stability | Additional information |
|--------|--------|----------------------|-----------|------------------------|
| GET    | JSON   | 200                  | -         | -                      |

```
{
  "loadbalancers": [
    {
      "ports": {
        "https": true,
        "http": true
      },
      "force_https": false,
      "source_port": 80,
      "network": "172.16.0.0/12 80.69.169.128/26",
      "service_id": "ececb4a5-7d64-4b1a-b0cc-45e26e4c9603",
      "hostname": "hello.slush.org",
      "enabled": true,
      "name": "hello-world",
      "loadbalancer_id": "0308e104-adc2-4868-b154-d42cf75a720c"
    },
    {
      "ports": {
        "https": false,
        "http": true
      },
      "force_https": false,
      "source_port": 80,
      "service_id": "ececb4a5-7d64-4b1a-b0cc-45e26e4c9603",
      "hostname": "hello2.slush.org",
      "enabled": true,
      "name": "hello-world2",
      "loadbalancer_id": "a4ec5443-9a12-4fec-96b1-92bb2c2c6eb5"
    },
    {
      "ports": {
        "https": false,
        "http": true
      },
      "force_https": false,
      "source_port": 8080,
      "service_id": "5a1496e6-13e8-444e-a2ad-3f740a1be837",
      "hostname": "hello3.slush.org",
      "enabled": true,
      "name": "hello-world3",
      "loadbalancer_id": "1c53a7e4-d9ce-4b19-ac39-167dd80aca50"
    }
  ]
}
```

## Create a New Loadbalancer

Creating new loadbalancer takes name, hostname, service_id for service to be loadbalancer and source port of the service
There is also optional `network` argument, you can use it to limit the source address of incoming requests.
Used for example with internal services.

| Method | Format | Response Status (OK) | Stability | Additional information |
|--------|--------|----------------------|-----------|------------------------|
| POST   | JSON   | 201                  | -         | -                      |

#### Request
```
{
    "name": "hello-world2",
    "enabled": true,
    "hostname": "hello2.slush.org",
    "network": "172.16.0.0/12",
    "service_id": "ececb4a5-7d64-4b1a-b0cc-45e26e4c9603",
    "source_port": 80,
    "force_https": false,
    "ports": {"http": true, "https": false}
}
```
#### Response
```
{
  "loadbalancer": {
    "loadbalancer_id": "229b7a00-85d1-4f23-9238-277bc9d25337",
    "name": "hello-world2",
    "enabled": true,
    "hostname": "hello2.slush.org",
    "network": "172.16.0.0/12",
    "service_id": "ececb4a5-7d64-4b1a-b0cc-45e26e4c9603",
    "source_port": 80,
    "force_https": false,
    "ports": {
      "http": true,
      "https": false
    }
  }
}
```

# Single loadbalancer


| Collection          | Base URL                                  | Format | Stability | Additional information |
|---------------------|-------------------------------------------|--------|-----------|------------------------|
| Single loadbalancer | `/api/v1/loadbalancers/{loadbalancer_id}` | JSON   | -         | -                      |

## Read Loadbalancer

| Method | Format | Response Status (OK) | Stability | Additional information |
|--------|--------|----------------------|-----------|------------------------|
| GET    | JSON   | 200                  | -         | -                      |

```
{
  "loadbalancer": {
    "ports": {
      "https": false,
      "http": true
    },
    "force_https": false,
    "source_port": 80,
    "service_id": "ececb4a5-7d64-4b1a-b0cc-45e26e4c9603",
    "hostname": "hello2.slush.org",
    "network": "172.16.0.0/12 80.69.169.128/26",
    "enabled": true,
    "name": "hello-world2",
    "loadbalancer_id": "229b7a00-85d1-4f23-9238-277bc9d25337"
  }
}
```


## Update Loadbalancer
This is partial operation and you can just PUT different fields.
Changes are merged in server and returned the full document

| Method | Format | Response Status (OK) | Stability | Additional information |
|--------|--------|----------------------|-----------|------------------------|
| PUT    | JSON   | 200                  | -         | -                      |

#### Request
```
{
    "force_https": true
}
```

#### Response
```

{
  "loadbalancer": {
    "ports": {
      "https": false,
      "http": true
    },
    "force_https": true,
    "source_port": 80,
    "service_id": "ececb4a5-7d64-4b1a-b0cc-45e26e4c9603",
    "hostname": "hello2.slush.org",
    "enabled": true,
    "name": "hello-world2",
    "loadbalancer_id": "229b7a00-85d1-4f23-9238-277bc9d25337"
  }
}
```

# Loadbalancer configuration

| Collection          | Base URL                       | Format | Stability | Additional information                           |
|---------------------|--------------------------------|--------|-----------|--------------------------------------------------|
| Loadbalancer config | `/api/v1/loadbalancers/config` | JSON   | -         | Config endpoint for actual ingress loadbalancers |


## Get Loadbalancer configuration

| Method | Format | Response Status (OK) | Stability | Additional information |
|--------|--------|----------------------|-----------|------------------------|
| GET    | JSON   | 200                  | -         | -                      |


```
[{
  "force_https": false,
  "name": "hello-world-lb",
  "hostname": "hello.slush.org",
  "ports": {
    "https": true,
    "http": true
  },
  "service_id": "1e896f56-f14b-471b-b9e4-a7066fe8ac5d",
  "loadbalancer_id": "21d4f559-d021-4d6b-bec6-4c6251d804b2",
  "network": "172.16.0.0/12 80.69.169.128/26",
  "backends": [],
  "enabled": true,
  "source_port": 80
}, {
  "force_https": true,
  "name": "slush-pass",
  "hostname": "pass.slush.org",
  "ports": {
    "https": true,
    "http": true
  },
  "service_id": "3fbb0a9a-885f-4975-9ace-2ca0f2d7d24f",
  "loadbalancer_id": "270e7ac8-aa8a-4f15-b80f-f6b0e3132668",
  "backends": [{
    "instance_id": "f76f4365-2347-4a4b-8293-e31c68ed9551",
    "address": "10.4.1.209",
    "port": 32771
  }, {
    "instance_id": "f2a765f3-a722-4d69-a06f-cc5b0840a54e",
    "address": "10.4.1.201",
    "port": 32771
  }],
  "enabled": true,
  "source_port": 8080
}]
```
