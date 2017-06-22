# Hosts / Instances

| Collection | Base URL            | Format | Stability | Additional information |
|------------|---------------------|--------|-----------|------------------------|
| Hosts      | `/api/v1/instances` | JSON   | -         | -                      |

## List All Hosts

| Method | Format | Response Status (OK) | Stability | Additional information |
|--------|--------|----------------------|-----------|------------------------|
| GET    | JSON   | 200                  | -         | -                      |

```
{
  "instances": [
    {
      "config": {
        "containers": [
          "c01d84bc-31f8-4357-8754-c60ec323d321",
          "bc922611-bfd1-44cb-8d8a-09148df0009a",
          "e8bfbb9d-4b0d-40b9-94ac-d509d116ee3c"
        ]
      },
      "last_heartbeat": "2016-04-11T22:12:13.293+03:00",
      "capabilities": ["SSD", "INTRANET"],
      "containers": [],
      "created": "2016-04-11T22:12:03.234+03:00",
      "instance_id": "d107ce15-db80-40fc-828c-edcb9fa79028",
      "system": {
        "system": "Darwin",
        "cpus": 8,
        "system_version": "Darwin Kernel Version 15.3.0: Thu Dec 10 18:40:58 PST 2015; root:xnu-3248.30.4~1/RELEASE_X86_64",
        "hostname": "L760"
      },
      "client": {
        "GoVersion": "go1.4.2",
        "Arch": "amd64",
        "GitCommit": "d12ea79",
        "Version": "1.8.1",
        "ApiVersion": "1.20",
        "BuildTime": "Thu Aug 13 02:49:29 UTC 2015",
        "Os": "linux",
        "KernelVersion": "4.0.9-boot2docker"
      },
      "info": {
        "ID": "ZKFK:FH64:SML4:IH6B:PEIX:XPKL:D4Y5:7U57:QITU:D7L2:D6LW:BYWW",
        "MemTotal": 2099998720,
        "BridgeNfIptables": true,
        "DriverStatus": [
          [
            "Root Dir",
            "/mnt/sda1/var/lib/docker/aufs"
          ],
          [
            "Backing Filesystem",
            "extfs"
          ],
          [
            "Dirs",
            "39"
          ],
          [
            "Dirperm1 Supported",
            "true"
          ]
        ],
        "Labels": [
          "provider=virtualbox"
        ],
        "Driver": "aufs",
        "LoggingDriver": "json-file",
        "NFd": 23,
        "CpuCfsQuota": true,
        "NGoroutines": 54,
        "Images": 35,
        "Containers": 2,
        "ExperimentalBuild": false,
        "InitSha1": "",
        "IndexServerAddress": "https://index.docker.io/v1/",
        "SystemTime": "2016-04-11T19:12:03.240898323Z",
        "HttpsProxy": "",
        "ExecutionDriver": "native-0.2",
        "SwapLimit": true,
        "Debug": true,
        "KernelVersion": "4.0.9-boot2docker",
        "NEventsListener": 1,
        "MemoryLimit": true,
        "BridgeNfIp6tables": true,
        "OomKillDisable": true,
        "Name": "default",
        "NoProxy": "",
        "InitPath": "/usr/local/bin/docker",
        "OperatingSystem": "Boot2Docker 1.8.1 (TCL 6.3); master : 7f12e95 - Thu Aug 13 03:24:56 UTC 2015",
        "IPv4Forwarding": true,
        "NCPU": 1,
        "CpuCfsPeriod": true,
        "DockerRootDir": "/mnt/sda1/var/lib/docker",
        "HttpProxy": ""
      }
    },
    {
      "config": {
        "containers": [
          "c01d84bc-31f8-4357-8754-c60ec323d321",
          "bc922611-bfd1-44cb-8d8a-09148df0009a",
          "e8bfbb9d-4b0d-40b9-94ac-d509d116ee3c"
        ]
      },
      "last_heartbeat": "2016-04-11T22:14:43.978+03:00",
      "containers": [],
      "created": "2016-04-11T22:12:23.605+03:00",
      "instance_id": "114d4230-bcbc-47b1-97ef-c7ffff39297b",
      "system": {
        "system": "Darwin",
        "cpus": 8,
        "system_version": "Darwin Kernel Version 15.3.0: Thu Dec 10 18:40:58 PST 2015; root:xnu-3248.30.4~1/RELEASE_X86_64",
        "hostname": "L760"
      },
      "client": {
        "GoVersion": "go1.4.2",
        "Arch": "amd64",
        "GitCommit": "d12ea79",
        "Version": "1.8.1",
        "ApiVersion": "1.20",
        "BuildTime": "Thu Aug 13 02:49:29 UTC 2015",
        "Os": "linux",
        "KernelVersion": "4.0.9-boot2docker"
      },
      "info": {
        "ID": "ZKFK:FH64:SML4:IH6B:PEIX:XPKL:D4Y5:7U57:QITU:D7L2:D6LW:BYWW",
        "MemTotal": 2099998720,
        "BridgeNfIptables": true,
        "DriverStatus": [
          [
            "Root Dir",
            "/mnt/sda1/var/lib/docker/aufs"
          ],
          [
            "Backing Filesystem",
            "extfs"
          ],
          [
            "Dirs",
            "39"
          ],
          [
            "Dirperm1 Supported",
            "true"
          ]
        ],
        "Labels": [
          "provider=virtualbox"
        ],
        "Driver": "aufs",
        "LoggingDriver": "json-file",
        "NFd": 23,
        "CpuCfsQuota": true,
        "NGoroutines": 54,
        "Images": 35,
        "Containers": 2,
        "ExperimentalBuild": false,
        "InitSha1": "",
        "IndexServerAddress": "https://index.docker.io/v1/",
        "SystemTime": "2016-04-11T19:12:23.611537998Z",
        "HttpsProxy": "",
        "ExecutionDriver": "native-0.2",
        "SwapLimit": true,
        "Debug": true,
        "KernelVersion": "4.0.9-boot2docker",
        "NEventsListener": 1,
        "MemoryLimit": true,
        "BridgeNfIp6tables": true,
        "OomKillDisable": true,
        "Name": "default",
        "NoProxy": "",
        "InitPath": "/usr/local/bin/docker",
        "OperatingSystem": "Boot2Docker 1.8.1 (TCL 6.3); master : 7f12e95 - Thu Aug 13 03:24:56 UTC 2015",
        "IPv4Forwarding": true,
        "NCPU": 1,
        "CpuCfsPeriod": true,
        "DockerRootDir": "/mnt/sda1/var/lib/docker",
        "HttpProxy": ""
      }
    },
    {
      "config": {
        "containers": [
          "ececb4a5-7d64-4b1a-b0cc-45e26e4c9603",
          "5a1496e6-13e8-444e-a2ad-3f740a1be837"
        ]
      },
      "last_heartbeat": "2016-04-13T09:57:07.311+03:00",
      "containers": [
        {
          "Status": "Up 3 hours",
          "Created": 1460502282,
          "Names": [
            "/5a1496e6-13e8-444e-a2ad-3f740a1be837"
          ],
          "Command": "/bin/sh -c 'php-fpm -d variables_order=\"EGPCS\" && (tail -F /var/log/nginx/access.log &) && exec nginx -g \"daemon off;\"'",
          "HostConfig": {
            "NetworkMode": "default"
          },
          "Id": "c777cca181d57c30910a42a13acf227da8b0b107aa351e378840cbdd98620c9c",
          "Labels": {
            "halti": "true",
            "version": "hello1",
            "service": "hello-world8000"
          },
          "Image": "tutum/hello-world",
          "Ports": [
            {
              "PublicPort": 32824,
              "IP": "192.168.99.100",
              "PrivatePort": 8080,
              "Type": "tcp"
            },
            {
              "PublicPort": 32825,
              "IP": "192.168.99.100",
              "PrivatePort": 80,
              "Type": "tcp"
            }
          ]
        },
        {
          "Status": "Up 3 hours",
          "Created": 1460502279,
          "Names": [
            "/ececb4a5-7d64-4b1a-b0cc-45e26e4c9603"
          ],
          "Command": "/bin/sh -c 'php-fpm -d variables_order=\"EGPCS\" && (tail -F /var/log/nginx/access.log &) && exec nginx -g \"daemon off;\"'",
          "HostConfig": {
            "NetworkMode": "default"
          },
          "Id": "c68436022a14755a581b010f613b0ca463b81a552f59bfda0200ec444eb5dcef",
          "Labels": {
            "halti": "true",
            "version": "hello4",
            "service": "hello-world3"
          },
          "Image": "tutum/hello-world",
          "Ports": [
            {
              "PublicPort": 32823,
              "IP": "192.168.99.100",
              "PrivatePort": 80,
              "Type": "tcp"
            }
          ]
        }
      ],
      "created": "2016-04-11T22:14:47.316+03:00",
      "instance_id": "c0bb9dc2-6c99-4c1b-9662-0602abbce695",
      "system": {
        "system": "Darwin",
        "cpus": 8,
        "system_version": "Darwin Kernel Version 15.3.0: Thu Dec 10 18:40:58 PST 2015; root:xnu-3248.30.4~1/RELEASE_X86_64",
        "hostname": "L760"
      },
      "client": {
        "GoVersion": "go1.4.2",
        "Arch": "amd64",
        "GitCommit": "d12ea79",
        "Version": "1.8.1",
        "ApiVersion": "1.20",
        "BuildTime": "Thu Aug 13 02:49:29 UTC 2015",
        "Os": "linux",
        "KernelVersion": "4.0.9-boot2docker"
      },
      "info": {
        "ID": "ZKFK:FH64:SML4:IH6B:PEIX:XPKL:D4Y5:7U57:QITU:D7L2:D6LW:BYWW",
        "MemTotal": 2099998720,
        "BridgeNfIptables": true,
        "DriverStatus": [
          [
            "Root Dir",
            "/mnt/sda1/var/lib/docker/aufs"
          ],
          [
            "Backing Filesystem",
            "extfs"
          ],
          [
            "Dirs",
            "39"
          ],
          [
            "Dirperm1 Supported",
            "true"
          ]
        ],
        "Labels": [
          "provider=virtualbox"
        ],
        "Driver": "aufs",
        "LoggingDriver": "json-file",
        "NFd": 23,
        "CpuCfsQuota": true,
        "NGoroutines": 54,
        "Images": 35,
        "Containers": 2,
        "ExperimentalBuild": false,
        "InitSha1": "",
        "IndexServerAddress": "https://index.docker.io/v1/",
        "SystemTime": "2016-04-11T19:14:47.323625842Z",
        "HttpsProxy": "",
        "ExecutionDriver": "native-0.2",
        "SwapLimit": true,
        "Debug": true,
        "KernelVersion": "4.0.9-boot2docker",
        "NEventsListener": 1,
        "MemoryLimit": true,
        "BridgeNfIp6tables": true,
        "OomKillDisable": true,
        "Name": "default",
        "NoProxy": "",
        "InitPath": "/usr/local/bin/docker",
        "OperatingSystem": "Boot2Docker 1.8.1 (TCL 6.3); master : 7f12e95 - Thu Aug 13 03:24:56 UTC 2015",
        "IPv4Forwarding": true,
        "NCPU": 1,
        "CpuCfsPeriod": true,
        "DockerRootDir": "/mnt/sda1/var/lib/docker",
        "HttpProxy": ""
      }
    }
  ]
}

```


## Healthy Hosts / Instances

| Collection    | Base URL                    | Format | Stability | Additional information                           |
|---------------|-----------------------------|--------|-----------|--------------------------------------------------|
| Healthy Hosts | `/api/v1/instances/healthy` | JSON   | -         | Shows only currently active hosts of the cluster |

### List All Healthy Hosts

| Method | Format | Response Status (OK) | Stability | Additional information |
|--------|--------|----------------------|-----------|------------------------|
| GET    | JSON   | 200                  | -         | -                      |

```
{
  "instances": [{
      "config": {
        "containers": [
          "c01d84bc-31f8-4357-8754-c60ec323d321",
          "bc922611-bfd1-44cb-8d8a-09148df0009a",
          "e8bfbb9d-4b0d-40b9-94ac-d509d116ee3c"
        ]
      },
      "last_heartbeat": "2016-04-11T22:12:13.293+03:00",
      "containers": [],
      "capabilities": ["SSD", "INTRANET"],
      "created": "2016-04-11T22:12:03.234+03:00",
      "instance_id": "d107ce15-db80-40fc-828c-edcb9fa79028",
      "system": {
        "system": "Darwin",
        "cpus": 8,
        "system_version": "Darwin Kernel Version 15.3.0: Thu Dec 10 18:40:58 PST 2015; root:xnu-3248.30.4~1/RELEASE_X86_64",
        "hostname": "L760"
      },
      "client": {
        "GoVersion": "go1.4.2",
        "Arch": "amd64",
        "GitCommit": "d12ea79",
        "Version": "1.8.1",
        "ApiVersion": "1.20",
        "BuildTime": "Thu Aug 13 02:49:29 UTC 2015",
        "Os": "linux",
        "KernelVersion": "4.0.9-boot2docker"
      },
      "info": {
        "ID": "ZKFK:FH64:SML4:IH6B:PEIX:XPKL:D4Y5:7U57:QITU:D7L2:D6LW:BYWW",
        "MemTotal": 2099998720,
        "BridgeNfIptables": true,
        "DriverStatus": [
          [
            "Root Dir",
            "/mnt/sda1/var/lib/docker/aufs"
          ],
          [
            "Backing Filesystem",
            "extfs"
          ],
          [
            "Dirs",
            "39"
          ],
          [
            "Dirperm1 Supported",
            "true"
          ]
        ],
        "Labels": [
          "provider=virtualbox"
        ],
        "Driver": "aufs",
        "LoggingDriver": "json-file",
        "NFd": 23,
        "CpuCfsQuota": true,
        "NGoroutines": 54,
        "Images": 35,
        "Containers": 2,
        "ExperimentalBuild": false,
        "InitSha1": "",
        "IndexServerAddress": "https://index.docker.io/v1/",
        "SystemTime": "2016-04-11T19:12:03.240898323Z",
        "HttpsProxy": "",
        "ExecutionDriver": "native-0.2",
        "SwapLimit": true,
        "Debug": true,
        "KernelVersion": "4.0.9-boot2docker",
        "NEventsListener": 1,
        "MemoryLimit": true,
        "BridgeNfIp6tables": true,
        "OomKillDisable": true,
        "Name": "default",
        "NoProxy": "",
        "InitPath": "/usr/local/bin/docker",
        "OperatingSystem": "Boot2Docker 1.8.1 (TCL 6.3); master : 7f12e95 - Thu Aug 13 03:24:56 UTC 2015",
        "IPv4Forwarding": true,
        "NCPU": 1,
        "CpuCfsPeriod": true,
        "DockerRootDir": "/mnt/sda1/var/lib/docker",
        "HttpProxy": ""
      }
    }]
}
```
