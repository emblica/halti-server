# halti-server

Halti is container orchestration service built by simplicity and high availability in mind.


## Build

```

docker build -t emb/halti-server .
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

### Bugs

not known bugs currently

## License

Copyright © 2016 Emblica / Teemu Heikkilä
