# halti-server

Halti is container orchestration service built by simplicity and high availability in mind.


## Build

```

docker build -t emblica/halti-server .
```


## Usage

```
PORT=4040
MONGO_URI=mongodb://192.168.99.100:32768/halti
PRODUCTION=no
```

```
docker run -d -p 10.4.1.224:4040:4040 --name halti-server --restart=always -e PORT=4040 -e MONGO_URI=mongodb://172.17.0.1/halti -e PRODUCTION=yes emb/halti-server
```


## Scheduler

Halti scheduler is implemented with Redhats Optaplanner (http://www.optaplanner.org/)
The implementation is the only part written in Java.

Scheduler tries to work with these limits and optimize the container configuration:

- For already running containers don't move  them if you don't have to
- Try to find best usage of resources
- Try to put dependent services close to each other (same neighborhood if possible)
- Try to distribute services in different locations if possible
- Try to keep single instances of service in different machines
- If there is capability that is needed for service try to put it to machine that provides those capabilities


### Missing features

- Scheduler implements locations and neighbourhood but the feature isn't yet enabled.
- Security and authentication with all connections between cluster

### Bugs

not known bugs currently

## License
`See LICENCE`
Copyright © 2016 Emblica / Teemu Heikkilä
