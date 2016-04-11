from collections import defaultdict
from itertools import cycle
import random, copy

hosts = [
    {"name": "A", "cpu": 2, "memory": 1000},
    {"name": "B", "cpu": 1, "memory": 2000},
    {"name": "C", "cpu": 2, "memory": 4000},
]


apps = [
    {"name": "App A", "cpu": 0.8, "memory": 500, "instances": 3},
    {"name": "App B", "cpu": 0.1, "memory": 200, "instances": 2},
    {"name": "App C", "cpu": 1, "memory": 800, "instances": 1},
    {"name": "App D", "cpu": 0.01, "memory": 10, "instances": 5},
]

containers = []

## Container flatmap
for app in apps:
    for instance_id in range(app['instances']):
        instance = {"name": app['name'], "cpu": app['cpu'], "memory": app['memory'], "instance_id": instance_id}
        containers += [instance]

host_conf = []

for host in hosts:
    cpu = host['cpu']
    memory = host['memory']
    host['containers'] = []
    apps_to_run = set()
    available_containers = []
    for container in containers:
        app_name = container['name']
        if app_name in apps_to_run:
            # Ok not this time return it to pool!
            available_containers += [container]
        else:
            c_cpu = container['cpu']
            c_memory = container['memory']
            if host['cpu']-c_cpu >= 0 and host['memory']-c_memory >= 0:
                # Its ok ! we took this container!
                apps_to_run.add(app_name)
                # Calculate new available resources
                host['cpu'] -= c_cpu
                host['memory'] -= c_memory
                host['containers'] += [app_name + ':' + str(container['instance_id'])]
            else:
                available_containers += [container]
    containers = available_containers
    host_conf.append(host)

print(host_conf)
