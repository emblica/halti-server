

# Services

| Collection | Base URL           | Format | Stability | Additional information |
|------------|--------------------|--------|-----------|------------------------|
| Services   | `/api/v1/services` | JSON   | -         | -                      |

## List All Services
| Method | Format | Response Status (OK) | Stability | Additional information |
|--------|--------|----------------------|-----------|------------------------|
| GET    | JSON   | 200                  | -         | -                      |


#### Response
```
{
  "services": [
    {
      "name": "hello-world3",
      "memory": 700,
      "ports": [
                  {
                    "port": 80,
                    "protocol": "tcp"
                  }
      ],
      "instances": 1,
      "image": "tutum/hello-world",
      "requirements": ["SSD"],
      "environment": [
        {
          "value": "80",
          "key": "PORT"
        }
      ],
      "service_id": "ececb4a5-7d64-4b1a-b0cc-45e26e4c9603",
      "version": "hello4",
      "enabled": true,
      "cpu": 1
    },
    {
      "name": "hello-world8000",
      "memory": 700,
      "ports": [
          {
            "port": 80,
            "protocol": "tcp"
          },
          {
            "port": 8080,
            "protocol": "tcp"
          }
      ],
      "instances": 2,
      "image": "tutum/hello-world",
      "environment": [
        {
          "value": "80",
          "key": "PORT"
        }
      ],
      "service_id": "5a1496e6-13e8-444e-a2ad-3f740a1be837",
      "version": "hello1",
      "enabled": true,
      "cpu": 1
    },
    {
      "name": "hello-world8000",
      "memory": 700,
      "ports": [
          {
            "port": 80,
            "protocol": "tcp"
          },
          {
            "port": 8080,
            "protocol": "tcp"
          }
      ],
      "instances": 2,
      "image": "tutum/hello-world",
      "environment": [
        {
          "value": "80",
          "key": "PORT"
        }
      ],
      "service_id": "491b2a26-000f-4630-ab2f-fe0222d03a0a",
      "version": "hello1",
      "enabled": true,
      "cpu": 1
    }
  ]
}

```

## Create a New Service


Creating new service takes service name, image, ports, env, instance count and resource requirements.

CPU requirement is part of CPU:s ie. one host has 2 cores so then you can put 4 CPU=0.5 services in one host.
Memory requirements is MB of memory.

**instances** mark number of running containers in cluster. You can set this higher than the count of hosts in cluster but there will be only one instance of this service per host.

**version** must be different string than previous version. Affects only for already deployed services.

**image** full path to docker image repository

**requirements** optional requirements for host machine

**ports** you can specify just one integer between 0-65535 or define it with object notation.
Object notation also accepts protocol definition (defaults to tcp).

**command** optional command for docker image

| Method | Format | Response Status (OK) | Stability | Additional information |
|--------|--------|----------------------|-----------|------------------------|
| POST   | JSON   | 201                  | -         | -                      |

### Request
```
{
    "name": "hello-world8000",
    "cpu": 0.1,
    "enabled": true,
    "memory": 700,
    "command": "bash",
    "instances": 2,
    "requirements": ["SSD", "INTRANET"],
    "version": "hello1",
    "image": "tutum/hello-world",
    "ports": [
        80,
        {
            "port": 8080,
            "protocol": "udp"
        }
    ],
    "environment": [{"key": "PORT", "value": "80"}]
}
```
### Response


```
{
    "service": {
        "name": "hello-world8000",
        "memory": 700,
        "ports": [
          {
            "port": 80,
            "protocol": "tcp"
          },
          {
            "port": 8080,
            "protocol": "udp"
          }
        ],
        "instances": 2,
        "requirements": ["SSD", "INTRANET"],
        "image": "tutum/hello-world",
        "command": "bash",
        "environment": [
            {
                "key": "PORT",
                "value": "80"
            }
        ],
        "service_id": "491b2a26-000f-4630-ab2f-fe0222d03a0a",
        "version": "hello1",
        "enabled": true,
        "cpu": 1
    }
}
```

# Single service

| Collection     | Base URL                        | Format | Stability | Additional information |
|----------------|---------------------------------|--------|-----------|------------------------|
| Single Service | `/api/v1/services/{service_id}` | JSON   | -         | -                      |

`running_on` is readonly field


## Read Service

| Method | Format | Response Status (OK) | Stability | Additional information |
|--------|--------|----------------------|-----------|------------------------|
| GET    | JSON   | 200                  | -         | -                      |

### Response
```
{
  "service": {
    "name": "registration",
    "memory": 250,
    "running_on": [{
      "instance_id": "0532c5f6-6eda-4319-b739-5a94e5384b44",
      "address": null,
      "port": null,
      "source": 8000
    }, {
      "instance_id": "0532c5f6-6eda-4319-b739-5a94e5384b44",
      "address": "10.4.1.201",
      "port": 32776,
      "source": 80
    }],
    "ports": [80],
    "instances": 1,
    "requirements": ["SSD", "INTRANET"],
    "image": "repo1.slush.org:443\/registration",
    "environment": [{
      "key": "MONGO_URI",
      "value": "mongodb:\/\/10.1.0.226\/slushreg"
    }, {
      "key": "AWS_HOST",
      "value": "storage.slush.xyz"
    }, {
      "key": "SLUSHPASS_ACCESS_TOKEN_PATH",
      "value": "\/oauth\/v2\/token"
    }, {
      "key": "SLUSHPASS_AUTHORIZE_PATH",
      "value": "\/oauth\/v2\/authenticate"
    }, {
      "key": "SLUSHPASS_BASE_URL",
      "value": "https:\/\/pass.slush.org"
    }, {
      "key": "REG_BASE_URL",
      "value": "https:\/\/register.slush.org"
    }, {
      "key": "SHOP_BASE_URL",
      "value": "https:\/\/shop.slush.org"
    }, {
      "key": "SERVER_PORT",
      "value": "8000"
    }],
    "service_id": "294f7ba7-72d4-4318-b690-bf273a7c2d1c",
    "version": "1",
    "enabled": true,
    "cpu": 0.1
  }
}
```

## Update Service
This is partial operation and you can just PUT different fields.
Changes are merged in server and returned the full document
Changes are propagated to cluster asynchronously

| Method | Format | Response Status (OK) | Stability | Additional information |
|--------|--------|----------------------|-----------|------------------------|
| PUT    | JSON   | 202                  | -         | -                      |


### Request
```
{
    "enabled": false,
    "version": "hello2"
}
```

### Response
```
{
  "service": {
    "_id": "570c101387d10657d803a2f9",
    "name": "hello-world3",
    "memory": 700,
    "ports": [
      {
        "port": 80,
        "protocol": "tcp"
      }
    ],
    "instances": 1,
    "requirements": ["SSD"],
    "image": "tutum/hello-world",
    "environment": [
      {
        "key": "PORT",
        "value": "80"
      }
    ],
    "service_id": "ececb4a5-7d64-4b1a-b0cc-45e26e4c9603",
    "version": "hello2",
    "enabled": false,
    "cpu": 1
  }
}
```

## Delete Service

This removes service from halti. Note that you can also just disable service by setting enabled to false.
Process is asynchronous

| Method | Format | Response Status (OK) | Stability | Additional information |
|--------|--------|----------------------|-----------|------------------------|
| DELETE | JSON   | 202                  | -         | -                      |

#### Response (202)
```
{
  "ack": true
}
```

#### Response (400)

```
{
  "ack": false,
  "error": "Service not found or remove not acknowledged"
}
```
